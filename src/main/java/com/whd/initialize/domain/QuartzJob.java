package com.whd.initialize.domain;

import com.whd.initialize.enums.QuartzJobRunStatusEnum;
import com.whd.initialize.enums.QuartzJobStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author WHD
 * @date 2019/9/11 16:39
 */
@Data
public class QuartzJob {

    private Integer id;
    private String name;
    private String jobClassName;
    private String methodName;
    private String cronExpression;
    private String parameter;
    private Integer jobStatus = QuartzJobStatusEnum.STATUS_NORMAL.getId();
    private boolean isConcurrent = false;

    // 下面的属性不存数据库

    private Date previousTime;
    private Date nextTime;
    private String runningStatus = QuartzJobRunStatusEnum.NONE.getId();

}
