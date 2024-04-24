package com.kim.flowable.service;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author JinXin
 * @date 2024/4/3 13:16
 * @description
 */
@Service
public class MyService {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Transactional
    public void startProcess() {
//        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        final HashMap<String, Object> formProperties = new HashMap<>();
        formProperties.put("dateVariable", "2024-04-03");
        runtimeService.startProcessInstanceByKey("oneTaskProcess", formProperties);
    }

    public List<Task> getTasks(String assignee) {

        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

    public long getTaskCount(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).count();
    }

    public long getTaskCountWithoutTaskDueDate(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).withoutTaskDueDate().count();
    }
}
