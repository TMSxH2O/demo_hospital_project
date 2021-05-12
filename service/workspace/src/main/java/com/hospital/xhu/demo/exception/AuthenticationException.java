package com.hospital.xhu.demo.exception;

import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/4
 */
public class AuthenticationException extends Exception {
    private static final ExceptionCode CODE = ExceptionCode.AUTHENTICATION_ERROR;
    private final String detailMessage;

    public AuthenticationException(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public ExceptionCode getCode() {
        return CODE;
    }

    public String getMsg() {
        return detailMessage;
    }

    public CommonResult<?> getResult() {
        return new CommonResult<>(CODE.getCode(), detailMessage);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " >> " + Arrays.toString(super.getStackTrace());
    }
}
