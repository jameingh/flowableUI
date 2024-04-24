package com.kim.flowable;

import com.kim.flowable.util.ProcessSupport;
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
        // 流程解读：
        // 执行到事件网关，流程暂停。订阅alert信号事件（订阅的信号名是信号 1）并创建一个定时1分钟的定时器
        // 如果信号在10分钟内被触发，则会取消定时器的执行，流程转向信号路径继续执行，
        // 激活名称为“处理警报”的用户任务，如果10分钟内信号没有被触发，
        // 则会沿定时器路径执行并取消信号订阅。
        // 这个使用的场景是：当某个事件发生时，需要等待一段时间，如果事件在等待时间内没有被处理，则执行默认处理。
        String processDefinitionKey = "catchSignalProcess";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        log.info("process instance id: " + processInstance.getId());
        processSupport.generateProcessDiagram(processDefinitionKey);
    }
}
