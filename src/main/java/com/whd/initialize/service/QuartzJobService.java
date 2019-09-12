package com.whd.initialize.service;

import com.whd.initialize.domain.QuartzJob;

/**
 * @author WHD
 * @date 2019/9/11 16:44
 */
public interface QuartzJobService {

    QuartzJob getJob(QuartzJob job);

    boolean addJob(QuartzJob job);

    boolean pauseJob(QuartzJob scheduleJob);

    boolean resumeJob(QuartzJob scheduleJob);

    boolean deleteJob(QuartzJob scheduleJob);

    boolean carryOutJob(QuartzJob scheduleJob);

    boolean updateCronExpression(QuartzJob job);

}
