package com.hospital.xhu.demo.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * 返回值的标准协议
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
@AllArgsConstructor
@Getter
@ToString
public class CommonResult<T> implements Serializable {
    private final int code;
    private final String msg;
    private final T data;

    public CommonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }
}
