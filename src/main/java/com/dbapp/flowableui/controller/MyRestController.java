package com.dbapp.flowableui.controller;

import com.dbapp.flowableui.service.MyService;
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

    class TaskDto {
        private String id;
        private String name;
        private String assignee;
        private String createTime;
        private String processInstanceId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAssignee() {
            return assignee;
        }

        public void setAssignee(String assignee) {
            this.assignee = assignee;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getProcessInstanceId() {
            return processInstanceId;
        }

        public void setProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
        }
    }
}
