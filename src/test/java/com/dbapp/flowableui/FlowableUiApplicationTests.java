package com.dbapp.flowableui;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("taskListenerProcess");
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
//        taskService.claim(task.getId(), "kim");
// 只能对unclaim的任务进行claim。否则报错already claimed by someone else
        taskService.unclaim(task.getId());
        taskService.claim(task.getId(), "tom");
        task = taskService.createTaskQuery().singleResult();
        log.info("task assignee: " + task.getAssignee());
        taskService.complete(task.getId());
        assertEquals(0, taskService.createTaskQuery().count());
    }

    /**
     * 测试serviceTask
     */
    @Test
    @Deployment
    @Transactional
    public void oneServiceTaskProcessTest() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("serviceTaskProcess");
        log.info("process instance id: " + processInstance.getId());
        assertEquals(0, runtimeService.createProcessInstanceQuery().count());
    }

}
