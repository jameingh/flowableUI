package com.dbapp.flowableui.service;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JinXin
 * @date 2024/4/7 18:57
 * @description
 */
@Service
public class LeaveService {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;

    public void startProcess() {
        final List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leaveProcess").orderByProcessDefinitionId().desc().listPage(0, 10);
        final String proDefId = processDefinitionList.get(0).getId();
        final Map<String, Object> formProperties = new HashMap();
        formProperties.put("startTime", LocalDate.now());
        formProperties.put("endTime", LocalDate.now());
        formProperties.put("reason", "请假");
        String outcome = "outStr";
        runtimeService.startProcessInstanceWithForm(proDefId, outcome, formProperties, "表单任务");
    }
}
