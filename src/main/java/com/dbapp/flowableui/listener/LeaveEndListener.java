package com.dbapp.flowableui.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JinXin
 * @date 2024/4/7 21:26
 * @description
 */
@Service
@Transactional
public class LeaveEndListener implements ExecutionListener {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void notify(DelegateExecution execution) {
        final String processInstanceId = execution.getProcessInstanceId();
        // 通过jdbcTemplate直接执行sql，删除流程变量
        jdbcTemplate.update("delete from act_hi_varinst where proc_inst_id_ = ?", processInstanceId);
    }
}
