package com.whd.initialize.config;

import com.whd.initialize.domain.QuartzJob;
import com.whd.initialize.util.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job有状态实现类，不允许并发执行
 * 若一个方法一次执行不完下次轮转时则等待该方法执行完后才执行下一次操作
 * 主要是通过注解：@DisallowConcurrentExecution
 *
 * @author WHD
 * @date 2019/9/11 17:10
 */
@DisallowConcurrentExecution
@Slf4j
public class QuartzJobFactoryDisallowConcurrentExecution implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        QuartzJob job = (QuartzJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        log.info("执行任务=> [作业名称：" + job.getName() + " 作业组：" + job.getJobClassName() + "] ");
        TaskUtils.invokMethod(job);
    }
}
