package com.hospital.xhu.demo.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
public enum ExceptionCode {
    // 数据库异常
    SQLEXCEPTION(3000);

    private final int code;

    ExceptionCode(int i) {
        code = i;
    }

    /**
     * 获取对应的错误码
     * @return 错误码
     */
    public int getCode() {
        return code;
    }
}
