package com.kim.flowable.model.dto;

import lombok.Data;

/**
 * @author JinXin
 * @date 2024/4/7 21:19
 * @description
 */
@Data
public class TaskDto {
    private String id;
    private String name;
    private String assignee;
    private String createTime;
    private String processInstanceId;
}