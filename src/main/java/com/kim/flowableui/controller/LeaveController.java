package com.kim.flowableui.controller;

import com.kim.flowableui.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JinXin
 * @date 2024/4/7 19:03
 * @description
 */
@RestController
@RequestMapping("/leave")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;
    @PostMapping("/process")
    public void startProcess() {
        leaveService.startProcess();
    }

    @GetMapping("/complete")
    public void complete(String groupName) {
        leaveService.complete(groupName);
    }
}
