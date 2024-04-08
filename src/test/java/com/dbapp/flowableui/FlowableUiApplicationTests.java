package com.dbapp.flowableui;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = FlowableUiApplication.class)
class FlowableUiApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    @Deployment(resources = { "processes/one-task-process.bpmn20.xml" })
    @Transactional
    public void simpleProcessTest() {
        final HashMap<String, Object> formProperties = new HashMap<>();
        formProperties.put("dateVariable", "2024-04-03");
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess", formProperties);
        assertEquals(1, runtimeService.createProcessInstanceQuery().count());
//        Task task = taskService.createTaskQuery().singleResult();
//        assertEquals("my task", task.getName());

//        taskService.complete(task.getId());
//        assertEquals(0, runtimeService.createProcessInstanceQuery().count());
        long count = taskService.createTaskQuery().count();
        System.out.println(count);
    }

}
