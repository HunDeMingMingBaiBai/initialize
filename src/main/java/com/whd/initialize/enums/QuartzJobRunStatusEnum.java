package com.whd.initialize.enums;

import lombok.AllArgsConstructor;

/**
 * quartz job 运行状态
 * NONE：Trigger已经完成，且不会在执行，或者找不到该触发器，或者Trigger已经被删除
 * NORMAL:正常状态
 * PAUSED：暂停状态
 * COMPLETE：触发器完成，但是任务可能还正在执行中
 * BLOCKED：线程阻塞状态
 *
 * @author WHD
 * @date 2019/9/11 17:14
 */
@AllArgsConstructor
public enum QuartzJobRunStatusEnum {

    NONE("NONE", "未知"),
    NORMAL("NORMAL", "正常运行"),
    PAUSED("PAUSED", "暂停状态"),
    COMPLETE("COMPLETE", "触发完成"),
    ERROR("ERROR", "错误状态"),
    BLOCKED("BLOCKED", "锁定状态"),;

    public static QuartzJobRunStatusEnum of(String id) {
        if (id.equals(NONE.getId())) {
            return NONE;
        }
        if (id.equals(NORMAL.getId())) {
            return NORMAL;
        }
        if (id.equals(PAUSED.getId())) {
            return PAUSED;
        }
        if (id.equals(COMPLETE.getId())) {
            return COMPLETE;
        }
        if (id.equals(ERROR.getId())) {
            return ERROR;
        }
        if (id.equals(BLOCKED.getId())) {
            return BLOCKED;
        }
        return null;
    }

    /**
     * 判断值是否在枚举中存在
     *
     * @param id
     * @return
     */
    public static boolean exist(String id) {
        boolean flag = false;
        for (QuartzJobRunStatusEnum e : QuartzJobRunStatusEnum.values()) {
            if (e.getId() == id) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
