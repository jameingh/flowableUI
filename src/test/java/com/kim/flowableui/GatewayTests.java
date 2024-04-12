package com.kim.flowableui;

import com.kim.flowableui.util.ProcessSupport;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JinXin
 * @date 2024/4/12 15:08
 * @description 网关测试用例
 */
@Slf4j
@SpringBootTest
public class GatewayTests {
    @Test
    void contextLoads() {
    }
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessSupport processSupport;

    @Test
    @Deployment
    @Transactional
    public void gatewayProcessTest() {
        String processDefinitionKey = "catchSignalProcess";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        log.info("process instance id: " + processInstance.getId());
        processSupport.generateProcessDiagram(processDefinitionKey);
    }
}
