package com.hospital.xhu.demo.advice;

import com.alipay.api.AlipayApiException;
import com.hospital.xhu.demo.exception.AuthenticationException;
import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.enumerate.CommonCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created with IntelliJ IDEA.
 * 全局异常的拦截，控制返回给客户端的都只是
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/4
 */
@RestControllerAdvice
@Slf4j
public class ApplicationControllerAdvice {

    /**
     * 权限异常的错误拦截
     *
     * @param e 权限异常
     * @return 输出异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public CommonResult<?> authenticationException(AuthenticationException e) {
        log.error(
                "authenticationException [{}]{} > {}",
                e.getClass().getSimpleName(),
                e.getMsg(),
                e.getStackTrace());
        return e.getResult();
    }

    /**
     * 用于监听阿里接口相关错误的监听
     *
     * @param e 阿里接口错误
     * @return 标准返回信息
     */
    @ExceptionHandler(AlipayApiException.class)
    public CommonResult<?> alipayException(AlipayApiException e) {
        log.error(
                "alipayException [{}]{} > {}",
                e.getClass().getSimpleName(),
                e.getErrCode(),
                e.getErrMsg());
        return new CommonResult<>(
                CommonCode.SERVICE_ERROR.getCode(),
                e.getClass().getSimpleName() + " > " + e.getErrCode(),
                e.getErrMsg());
    }

    /**
     * 拦截所有的异常，防止直接跳转错误界面
     *
     * @param e 拦截到的异常对象
     * @return 输出日志，并返回结果
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<?> errorHandler(Exception e) {
        log.error("errorHandler [{}]{} > {}", e.getClass().getSimpleName(), e.getLocalizedMessage(), e.getStackTrace());
        return new CommonResult<>(
                CommonCode.SERVICE_ERROR.getCode(),
                e.getClass().getSimpleName(),
                e.getMessage());
    }
}
