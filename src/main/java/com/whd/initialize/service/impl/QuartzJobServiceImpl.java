package com.whd.initialize.service.impl;

import com.whd.initialize.config.QuartzJobFactory;
import com.whd.initialize.config.QuartzJobFactoryDisallowConcurrentExecution;
import com.whd.initialize.domain.QuartzJob;
import com.whd.initialize.service.QuartzJobService;
import com.whd.initialize.util.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author WHD
 * @date 2019/9/11 16:44
 */
@Slf4j
public class QuartzJobServiceImpl implements QuartzJobService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 获取单个任务 详细执行信息
     *
     * @return
     * @throws SchedulerException
     */
    @Override
    public QuartzJob getJob(QuartzJob job) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getJobClassName());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (null != trigger) {
//                String desc = "【触发器:" + trigger.getKey() + "】";
                job.setNextTime(trigger.getNextFireTime()); //下次触发时间
                job.setPreviousTime(trigger.getPreviousFireTime());//上次触发时间
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setRunningStatus(triggerState.name());
//                if (trigger instanceof CronTrigger) {
//                    CronTrigger cronTrigger = (CronTrigger) trigger;
//                    String cronExpression = cronTrigger.getCronExpression();
//                    desc += "【执行方法:" + job.getMethodName() + "】" + "【执行时间:" + cronExpression + "】";
//                }
//                job.setNotes(desc);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("查询作业=> [作业名称：" + job.getName() + " 作业组：" + job.getJobClassName() + "]=> [失败]异常信息：{}", e);
        }

        return job;
    }


    /**
     * 添加任务
     *
     * @param job
     */
    @Override
    public boolean addJob(QuartzJob job) {
        if (job == null) {
            return false;
        }

        if (!TaskUtils.isValidExpression(job.getCronExpression())) {
            log.error("时间表达式错误（" + job.getName() + "," + job.getJobClassName() + "）  " + job.getCronExpression());
            throw new RuntimeException("时间表达式错误（" + job.getName() + "," + job.getJobClassName() + "）  " + job.getCronExpression());
        } else {
            try {
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                // 任务名称和任务组设置规则：    // 名称：task_1 ..    // 组 ：group_1 ..
                TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getJobClassName());
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                // 不存在，创建一个
                if (null == trigger) {
                    //是否允许并发执行
                    Class<? extends Job> clazz = job.isConcurrent() ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;
                    JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getName(), job.getJobClassName()).build();
                    jobDetail.getJobDataMap().put("scheduleJob", job);
                    // 表达式调度构建器
                    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                    // 按新的表达式构建一个新的trigger
                    trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                    scheduler.scheduleJob(jobDetail, trigger);
                    log.info("创建作业=> [作业名称：" + job.getName() + " 作业组：" + job.getJobClassName() + "] ==》》》ADD");
                } else {
                    // trigger已存在，则更新相应的定时设置
                    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                    // 按新的cronExpression表达式重新构建trigger
                    trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                    // 按新的trigger重新设置job执行
                    scheduler.rescheduleJob(triggerKey, trigger);
                    log.info("创建作业=> [作业名称：" + job.getName() + " 作业组：" + job.getJobClassName() + "] ===》》》UPDATE");
                }

                if (scheduler.isShutdown()) {
                    scheduler.isStarted();
                }

            } catch (SchedulerException e) {
                e.printStackTrace();
                log.error("创建作业=> [作业名称：" + job.getName() + " 作业组：" + job.getJobClassName() + "]=> [失败]异常信息：{}", e);
                throw new RuntimeException("创建作业=> [作业名称：" + job.getName() + " 作业组：" + job.getJobClassName() + "]=> [失败]");
            }

        }
        return true;
    }

    /**
     * 暂停任务
     *
     * @param scheduleJob
     * @return
     */
    @Override
    public boolean pauseJob(QuartzJob scheduleJob) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getJobClassName());
        try {
            scheduler.pauseJob(jobKey);
            log.info("暂停作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "] ");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("暂停作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "]=> [失败]异常信息：{}", e);
            throw new RuntimeException("暂停作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "]=> [失败]");
        }
    }

    /**
     * 恢复任务
     *
     * @param scheduleJob
     * @return
     */
    @Override
    public boolean resumeJob(QuartzJob scheduleJob) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getJobClassName());
        try {
            scheduler.resumeJob(jobKey);
            log.info("恢复作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "] ");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("恢复作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "]=> [失败]异常信息：{}", e);
            throw new RuntimeException("恢复作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "]=> [失败]");
        }
    }

    /**
     * 停止任务
     *
     * @param scheduleJob
     * @return
     */
    @Override
    public boolean deleteJob(QuartzJob scheduleJob) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey tk = TriggerKey.triggerKey(scheduleJob.getName(), scheduleJob.getJobClassName());
        JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getJobClassName());
        try {
            //停止触发器
            scheduler.pauseTrigger(tk);
            //移除触发器
            scheduler.unscheduleJob(tk);
            //删除作业
            scheduler.deleteJob(jobKey);
            log.info("删除作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "] ");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("删除作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "]=> [失败]异常信息：{}", e);
            throw new RuntimeException("删除作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "]=> [失败]");
        }
    }

    /**
     * 执行一次
     *
     * @param scheduleJob
     * @return
     */
    @Override
    public boolean carryOutJob(QuartzJob scheduleJob) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getJobClassName());
        try {
            scheduler.triggerJob(jobKey);
            log.info("立即执行一次作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "] ");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("立即执行一次作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "]=> [失败]异常信息：{}", e);
            throw new RuntimeException("立即执行一次作业=> [作业名称：" + scheduleJob.getName() + " 作业组：" + scheduleJob.getJobClassName() + "]=> [失败]");
        }
    }

    /**
     * 更新时间表达式
     *
     * @param job
     * @return
     */
    @Override
    public boolean updateCronExpression(QuartzJob job) {

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getJobClassName());
        try {
            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            //获取trigger
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //构造任务触发器
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            log.info("修改作业触发时间=> [作业名称：" + job.getName() + " 作业组：" + job.getJobClassName() + "] ");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("修改作业触发时间=> [作业名称：" + job.getName() + " 作业组：" + job.getJobClassName() + "]=> [失败]异常信息：{}", e);
            throw new RuntimeException("修改作业触发时间=> [作业名称：" + job.getName() + " 作业组：" + job.getJobClassName() + "]=> [失败]");
        }
    }


    public boolean start() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.start();
            log.info("启动调度器 ");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("启动调度器=> [失败]异常信息：{}", e);
        }
        return false;
    }

    public boolean shutdown() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.shutdown();
            log.info("停止调度器 ");
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("停止调度器=> [失败]异常信息：{}", e);
        }
        return false;
    }

}
