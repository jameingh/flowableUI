package com.kim.flowableui.controller;

import com.kim.flowableui.modle.dto.TaskDto;
import com.kim.flowableui.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JinXin
 * @date 2024/4/3 13:18
 * @description
 */
@RestController
@RequestMapping("/rest")
public class MyRestController {
    @Autowired
    private MyService myService;

    @PostMapping("/process")
    public void startProcess() {
        myService.startProcess();
    }

    @GetMapping("/tasks")
    public List<TaskDto> getTasks(@RequestParam String assignee) {
        return myService.getTasks(assignee).stream().map(task -> {
            TaskDto taskDto = new TaskDto();
            taskDto.setId(task.getId());
            taskDto.setName(task.getName());
            taskDto.setAssignee(task.getAssignee());
            taskDto.setCreateTime(task.getCreateTime().toString());
            taskDto.setProcessInstanceId(task.getProcessInstanceId());
            return taskDto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/tasks/count")
    public long getTaskCount(@RequestParam String assignee) {
        return myService.getTaskCount(assignee);
    }

    @GetMapping("/tasks/countwithoutduedate")
    public long getTaskCountWithoutTaskDueDate(@RequestParam String assignee) {
        return myService.getTaskCountWithoutTaskDueDate(assignee);
    }

}
