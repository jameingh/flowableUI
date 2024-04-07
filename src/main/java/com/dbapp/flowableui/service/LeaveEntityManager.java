package com.dbapp.flowableui.service;

import com.dbapp.flowableui.mapper.LeaveMapper;
import com.dbapp.flowableui.modle.LeaveEntity;
import org.flowable.engine.delegate.DelegateExecution;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author JinXin
 * @date 2024/4/7 17:49
 * @description
 */
@Service
public class LeaveEntityManager {
    @Autowired
    private LeaveMapper leaveMapper;

    @Transactional
    public LeaveEntity newLeave(DelegateExecution execution) {
        LeaveEntity leaveEntity = new LeaveEntity();
        leaveEntity.setProcessInstanceId(execution.getProcessInstanceId());
        leaveEntity.setStartTime(execution.getVariable("startTime", LocalDate.class));
        leaveEntity.setEndTime(execution.getVariable("endTime", LocalDate.class));
        leaveEntity.setReason(execution.getVariable("reason").toString());
        leaveMapper.insert(leaveEntity);
        return leaveEntity;
    }
}
