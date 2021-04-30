package com.hospital.xhu.demo.utils;

import org.apache.tomcat.jni.Time;

/**
 * Created with IntelliJ IDEA.
 * 实体的缓存对象，保存时间戳和对象信息
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/18
 */
public class EntityCache<T> {
    private final T data;
    private long time;

    /**
     * 初始化时自动存储当前时间
     *
     * @param data 缓存数据
     */
    public EntityCache(T data) {
        this.time = Time.now();
        this.data = data;
    }

    /**
     * 【默认】获取数据同时更新时间戳
     *
     * @return 缓存的数据
     */
    public T getData() {
        time = Time.now();
        return data;
    }

    /**
     * 获取缓冲的更新时间
     *
     * @return 时间
     */
    public long getTime() {
        return time;
    }

    /**
     * 获取数据不更新时间戳
     *
     * @return 缓存的数据
     */
    public T getDataNotUpdate() {
        return data;
    }
}
