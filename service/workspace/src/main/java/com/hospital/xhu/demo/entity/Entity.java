package com.hospital.xhu.demo.entity;

import com.hospital.xhu.demo.exception.ProjectException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
public interface Entity {
    /**
     * 用于初始化对象数据，防止插入数据库报错
     */
    void init() throws ProjectException;
}
