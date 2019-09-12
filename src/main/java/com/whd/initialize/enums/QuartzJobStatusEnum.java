package com.whd.initialize.enums;

import lombok.AllArgsConstructor;

/**
 * @author WHD
 * @date 2019/9/11 16:41
 */
@AllArgsConstructor
public enum QuartzJobStatusEnum {

    STATUS_DISABLE(-1, "暂停"),
    STATUS_NORMAL(0, "正常"),;

    public static QuartzJobStatusEnum of(Integer id) {
        if (id.equals(STATUS_DISABLE.getId())) {
            return STATUS_DISABLE;
        }
        if (id.equals(STATUS_NORMAL.getId())) {
            return STATUS_NORMAL;
        }
        return null;
    }

    /**
     * 判断值是否在枚举中存在
     *
     * @param id
     * @return
     */
    public static boolean exist(int id) {
        boolean flag = false;
        for (QuartzJobStatusEnum e : QuartzJobStatusEnum.values()) {
            if (e.getId() == id) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
