package com.kim.flowableui;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class FlowableUiApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    // 在processes目录下的流程定义文件会自动部署，不需要特别声明
//    @Deployment(resources = { "processes/one-task-process.bpmn20.xml" })
    @Deployment
    @Test
    @Transactional
    public void simpleProcessTest() {
        final HashMap<String, Object> formProperties = new HashMap<>();
        formProperties.put("dateVariable", "2024-04-03");
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess", formProperties);
        log.info("process instance id: " + processInstance.getId());
        assertEquals(1, runtimeService.createProcessInstanceQuery().count());
        Task task = taskService.createTaskQuery().singleResult();
        assertEquals("my task", task.getName());

        taskService.complete(task.getId());
        assertEquals(0, runtimeService.createProcessInstanceQuery().count());
    }

    @Test
    @Deployment
    @Transactional
    public void taskListenerProcessTest() {
        String processDefinitionKey = "taskListenerProcess";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        log.info("process instance id: " + processInstance.getId());
        assertEquals(1, runtimeService.createProcessInstanceQuery().count());
        Task task = taskService.createTaskQuery().singleResult();
        assertEquals("my task", task.getName());

        // 流程实例的任务数
        final long count = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).count();
        assertEquals(1, count);

        // 查询潜在用户和用户组(候选人和候选组)，参与人（即AssignmentListener中设置的所有人）
        final List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink : identityLinksForTask) {
            log.info("identity link user: {}" ,identityLink.getUserId());
        }
        // 根据参与人查询任务列表
        final Task task1 = taskService.createTaskQuery().taskInvolvedUser("tom").singleResult();
        final Task task2 = taskService.createTaskQuery().taskInvolvedGroups(Arrays.asList("leader")).singleResult();
        assertEquals(true, task2.getId().equals(task1.getId()));
        // 根据候选人查不出来，但是设置了候选人，不知道是不是姿势不对
        final Task task3 = taskService.createTaskQuery().taskCandidateUser("tom").singleResult();
        assertEquals(null, task3);
//        taskService.claim(task.getId(), "kim");
// 只能对unclaim的任务进行claim。否则报错already claimed by someone else
        taskService.unclaim(task.getId());
        taskService.claim(task.getId(), "tom");
        // 任务委派给kim
        taskService.delegateTask(task.getId(), "kim");
        // 查询被委派且未签收的任务（委派的底层实现是修改任务的委派状态，有 pending和resolved两种状态）
        final List<Task> list = taskService.createTaskQuery().taskDelegationState(DelegationState.PENDING).taskAssignee("kim").list();
        // todo 有没有其他方式查询委派任务，和查询办理人的api统一一下逻辑？
        // todo 任务办理人提前办理任务会怎样？此时委派人还未处理任务
//        taskService.complete(task.getId());
        // 被委派人处理完成任务
        taskService.resolveTask(task.getId());

        log.info("task assignee: " + task.getAssignee());
        taskService.complete(task.getId());
        assertEquals(0, taskService.createTaskQuery().count());
        generateProcessDiagram(processDefinitionKey);
    }

    /**
     * 测试serviceTask
     */
    @Test
    @Deployment
    @Transactional
    public void serviceTaskProcessTest() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("serviceTaskProcess");
        log.info("process instance id: " + processInstance.getId());
        assertEquals(0, runtimeService.createProcessInstanceQuery().count());
    }

    @Test
    @Deployment
//    @Transactional
    public void subTaskProcessTest() {
        String processDefinitionKey = "subTaskProcess";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        log.info("process instance id: " + processInstance.getId());
        assertEquals(1, runtimeService.createProcessInstanceQuery().count());
        Task task = taskService.createTaskQuery().singleResult();
        assertEquals("my task", task.getName());

        // 创建子任务,new task的任务必须调用saveTask方法才能持久化
        final Task subTask1 = taskService.newTask();
        // 设置父任务id,建立父子关系
        subTask1.setParentTaskId(task.getId());
        // 设置子任务的其他属性
        subTask1.setAssignee("tom");
        subTask1.setName("subTask 1");
        subTask1.setOwner("tom");

        // 保存子任务
        taskService.saveTask(subTask1);
        taskService.complete(subTask1.getId());

        // 根据父任务查询子任务
        final List<HistoricTaskInstance> subTasks = historyService.createHistoricTaskInstanceQuery().taskParentTaskId(task.getId()).list();
        log.info("sub tasks: " + subTasks);
        // 查询父任务
        final HistoricTaskInstance historicTaskInstance =
                historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();
        log.info("parent task: " + historicTaskInstance);

        // 子任务未办理，父任务可以完成吗？可以
        taskService.complete(task.getId());
        assertEquals(0, runtimeService.createProcessInstanceQuery().count());
        generateProcessDiagram(processDefinitionKey);
    }

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    public void generateProcessDiagram(String processDefinitionKey) {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pd.getId());
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().singleResult();
        if (hpi == null) {
            return;
        }
        List<String> highLightedActivities = new ArrayList<>();
        List<String> hightLightedFlows = new ArrayList<>();
        double scaleFactor = 1.0;
        boolean drawSqquenceFlowNameWithNoLabelDI = true;
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(hpi.getId()).list();
        for (HistoricActivityInstance hai : list) {
            if (hai.getActivityType().equals("sequenceFlow")) {
                hightLightedFlows.add(hai.getActivityId());
            } else {
                highLightedActivities.add(hai.getActivityId());
            }
        }
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        InputStream inputStream = generator.generateDiagram(bpmnModel, "PNG", highLightedActivities, hightLightedFlows, scaleFactor, drawSqquenceFlowNameWithNoLabelDI);
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File(processDefinitionKey+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
