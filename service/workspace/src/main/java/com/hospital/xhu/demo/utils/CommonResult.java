package com.hospital.xhu.demo.utils;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 * 返回值的标准协议
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
@AllArgsConstructor
@ToString
public class CommonResult<T> {
    private final int code;
    private final String msg;
    private final T data;
}
