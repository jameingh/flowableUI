package com.dbapp.flowableui.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * @author JinXin
 * @date 2024/4/8 14:08
 * @description
 */
public class AssignmentListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("admin");
        delegateTask.addCandidateUser("tom");
        delegateTask.addCandidateGroup("leader");
    }
}
