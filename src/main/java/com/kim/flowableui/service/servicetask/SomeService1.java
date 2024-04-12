package com.kim.flowableui.service.servicetask;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

/**
 * @author JinXin
 * @date 2024/4/9 14:28
 * @description
 */
@Slf4j
@Component
public class SomeService1 {
    public void consumeMethod(DelegateExecution execution) {
        log.info("event name:{}", execution.getEventName());
        // 获取theTask2服务任务的返回值
        final Object result = execution.getVariable("result");
        log.info("result:{}", result);
    }
}
