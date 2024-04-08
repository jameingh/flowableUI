package com.dbapp.flowableui.modle;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dbapp.flowableui.config.LocalDateTypeHandler;
import lombok.Data;
import org.joda.time.LocalDate;
import java.io.Serializable;


/**
 * @author JinXin
 * @date 2024/4/7 17:43
 * @description
 */
@Data
@TableName("t_leave")
public class LeaveEntity implements Serializable {

    private Long id;
    // mybatis-plus默认支持驼峰和下划线命名转换
    private String processInstanceId;
    // 指定joda time 转换处理器
    @TableField(value = "start_time", typeHandler = LocalDateTypeHandler.class)
    private LocalDate startTime;
    @TableField(value = "end_time", typeHandler = LocalDateTypeHandler.class)
    private LocalDate endTime;
    private String reason;
    private String leaderApproved;
}
