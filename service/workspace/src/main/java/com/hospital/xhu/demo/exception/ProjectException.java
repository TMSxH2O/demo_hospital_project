package com.hospital.xhu.demo.exception;

import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
public class ProjectException extends Exception {
    private final ExceptionCode code;
    private final String detailMessage;

    public ProjectException(ExceptionCode code, String msg) {
        this.code = code;
        this.detailMessage = msg;
    }

    public ExceptionCode getCode() {
        return code;
    }

    public String getMsg() {
        return detailMessage;
    }

    public CommonResult<?> getResult() {
        return new CommonResult<>(code.getCode(), detailMessage);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " >> " + Arrays.toString(super.getStackTrace());
    }
}
