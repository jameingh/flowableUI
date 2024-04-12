package com.kim.flowableui.service.servicetask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author JinXin
 * @date 2024/4/9 14:02
 * @description
 */
@Slf4j
@Component
public class PrintService {

    public void printMessage() {
        log.info("方法调用：Hello World!");
    }
}
