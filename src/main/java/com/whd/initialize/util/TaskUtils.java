package com.whd.initialize.util;

import com.whd.initialize.config.ApplicationContextRegister;
import com.whd.initialize.domain.QuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;

/**
 * @author WHD
 * @date 2019/9/11 16:48
 */
@Slf4j
public class TaskUtils {

    /**
     * 通过反射调用scheduleJob中定义的方法
     * 通过 spring  管理的方式获取实例
     *
     * @param scheduleJob
     */
    public static void invokMethod(QuartzJob scheduleJob) {
        Class<?> clazz = null;
        ApplicationContext context = ApplicationContextRegister.getApplicationContext();
        try {
            clazz = context.getBean(scheduleJob.getJobClassName()).getClass();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("任务名称 = [" + scheduleJob.getName() + "]---------------未启动成功，请检查执行类是否配置正确！！！异常信息：{}", e);
            return;
        }
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
        } catch (NoSuchMethodException e) {
            log.error("任务名称 = [" + scheduleJob.getName() + "]---------------未启动成功，请检查执行类的方法名是否设置错误！！！异常信息：{}", e);
        } catch (SecurityException e) {
            e.printStackTrace();
            log.error("任务名称 = [" + scheduleJob.getName() + "]---------------未启动成功，请检查执行类的方法名是否设置错误！！！异常信息：{}", e);
        }
        if (method != null) {
            try {
                method.invoke(context.getBean(scheduleJob.getJobClassName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                log.error("任务名称 = [" + scheduleJob.getName() + "]---------------未启动成功，请检查执行类的方法 是Private访问权限。异常信息：{}", e);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                log.error("任务名称 = [" + scheduleJob.getName() + "]---------------未启动成功，请检查执行类的方法 不合法的参数异常。异常信息：{}", e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                log.error("任务名称 = [" + scheduleJob.getName() + "]---------------未启动成功，请检查执行类的方法 调用的方法的内部抛出了异常而没有被捕获。异常信息：{}", e);
            }
        }
    }

    /**
     * 判断cron时间表达式正确性
     *
     * @param cronExpression
     * @return
     */
    public static boolean isValidExpression(final String cronExpression) {
        CronTriggerImpl trigger = new CronTriggerImpl();
        try {
            trigger.setCronExpression(cronExpression);
            Date date = trigger.computeFirstFireTime(null);
            return date != null && date.after(new Date());
        } catch (ParseException e) {
        }
        return false;
    }


    /**
     * 将cronExpression转换成中文
     *
     * @param cronExp
     * @return
     */
    public static String translateToChinese(String cronExp) {
        if (cronExp == null || cronExp.length() < 1) {
            return "cron表达式为空";
        }
        CronExpression exp = null;
        // 初始化cron表达式解析器
        try {
            exp = new CronExpression(cronExp);
        } catch (ParseException e) {
            return "corn表达式不正确";
        }
        String[] tmpCorns = cronExp.split(" ");
        StringBuffer sBuffer = new StringBuffer();
        if (tmpCorns.length == 6) {
            //解析月
            if (!"*".equals(tmpCorns[4])) {
                sBuffer.append(tmpCorns[4]).append("月");
            } else {
                sBuffer.append("每月");
            }
            //解析周
            if (!"*".equals(tmpCorns[5]) && !"?".equals(tmpCorns[5])) {
                char[] tmpArray = tmpCorns[5].toCharArray();
                for (char tmp : tmpArray) {
                    switch (tmp) {
                        case '1':
                            sBuffer.append("星期天");
                            break;
                        case '2':
                            sBuffer.append("星期一");
                            break;
                        case '3':
                            sBuffer.append("星期二");
                            break;
                        case '4':
                            sBuffer.append("星期三");
                            break;
                        case '5':
                            sBuffer.append("星期四");
                            break;
                        case '6':
                            sBuffer.append("星期五");
                            break;
                        case '7':
                            sBuffer.append("星期六");
                            break;
                        case '-':
                            sBuffer.append("至");
                            break;
                        default:
                            sBuffer.append(tmp);
                            break;
                    }
                }
            }

            //解析日
            if (!"?".equals(tmpCorns[3])) {
                if (!"*".equals(tmpCorns[3])) {
                    sBuffer.append(tmpCorns[3]).append("日");
                } else {
                    sBuffer.append("每日");
                }
            }

            //解析时
            if (!"*".equals(tmpCorns[2])) {
                sBuffer.append(tmpCorns[2]).append("时");
            } else {
                sBuffer.append("每时");
            }

            //解析分
            if (!"*".equals(tmpCorns[1])) {
                sBuffer.append(tmpCorns[1]).append("分");
            } else {
                sBuffer.append("每分");
            }

            //解析秒
            if (!"*".equals(tmpCorns[0])) {
                sBuffer.append(tmpCorns[0]).append("秒");
            } else {
                sBuffer.append("每秒");
            }
        }

        return sBuffer.toString();
    }

}
