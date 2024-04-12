package com.kim.flowableui.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author JinXin
 * @date 2024/4/8 12:24
 * @description
 */
public interface LeaveService {
    void startProcess();

    @Transactional
    void complete(String groupName);
}
