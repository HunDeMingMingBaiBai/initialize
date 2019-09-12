package com.whd.initialize.config;

import com.whd.initialize.domain.QuartzJob;
import com.whd.initialize.util.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 不管上次定时任务是否执行完都再次执行
 *
 * @author WHD
 * @date 2019/9/11 17:08
 */
@Slf4j
public class QuartzJobFactory implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        QuartzJob job = (QuartzJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        log.info("执行任务=> [作业名称：" + job.getName() + " 作业组：" + job.getJobClassName() + "] ");
        TaskUtils.invokMethod(job);
    }
}
