package com.hospital.xhu.demo.exception;

import com.hospital.xhu.demo.utils.CommonResult;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
public class ProjectException extends Exception {
    private final ExceptionCode code;
    private String msg;

    public ProjectException(ExceptionCode code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResult<Object> getResult() {
        return new CommonResult<>(code.getCode(), msg, null);
    }
}
