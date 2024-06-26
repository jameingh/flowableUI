package com.kim.flowable.service;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JinXin
 * @date 2024/4/7 18:57
 * @description
 */
@Slf4j
@Service
public class LeaveServiceImpl implements LeaveService{
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void startProcess() {
        final List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leaveProcess").orderByProcessDefinitionId().desc().listPage(0, 10);
        final String proDefId = processDefinitionList.get(0).getId();
        final Map<String, Object> formProperties = new HashMap<>();
        formProperties.put("startTime", LocalDate.now());
        formProperties.put("endTime", LocalDate.now());
        formProperties.put("reason", "请假");
        String outcome = "outStr";
        runtimeService.startProcessInstanceWithForm(proDefId, outcome, formProperties, "表单任务");
    }

    @Override
    @Transactional
    public void complete(String groupName){
        final List<Task> taskList = taskService.createTaskQuery().taskCandidateGroup(groupName).orderByTaskCreateTime().desc().list();
        final String taskId = taskList.get(0).getId();
        Map<String, Object> processVariables = taskService.getVariables(taskId);
        // 打印流程变量，查看自定义对象是否反序列化成功
        log.info("processVariables:{}", processVariables);
        final HashMap<String, Object> param = new HashMap<>();
        param.put("leaderApproved", true);
        taskService.complete(taskId, param);
    }


}
