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
    DATA_EXCEPTION(9999),
    // 数据库异常
    SQL_EXCEPTION(3000),
    // 用户信息相关错误
    USER_INFO(3001),
    // 医院信息相关错误
    HOSPITAL_INFO(3002),
    // 医院科室信息相关错误
    DEPARTMENT_INFO(3003),
    // 医生信息相关错误
    DOCTOR_INFO(3004),
    // 用户预约订单相关错误
    USER_RESERVATION(3005),

    ;


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
