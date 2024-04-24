package com.kim.flowable.service.servicetask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author JinXin
 * @date 2024/4/9 14:02
 * @description
 */
@Slf4j
@Component
public class InvokerService {

    private String data;

    // 会被ServiceTask的表达式声明调用
    public String getData() {
        log.info("属性调用");
        return "属性调用方式：从数据库/远程调用查出来的值，也可以是动态配置的值";
    }

    public void setData(String data) {
        this.data = data;
    }
}
