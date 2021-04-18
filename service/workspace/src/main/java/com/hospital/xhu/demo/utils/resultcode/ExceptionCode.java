package com.hospital.xhu.demo.utils.resultcode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
public enum ExceptionCode {
    // 数据异常
    DATAEXCEPTION(9999),
    // 数据库异常
    SQLEXCEPTION(3000),
    // 用户信息相关错误
    USERINFO(3100);

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
