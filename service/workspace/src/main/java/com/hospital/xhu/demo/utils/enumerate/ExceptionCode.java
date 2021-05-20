package com.hospital.xhu.demo.utils.enumerate;

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
    // 文件相关错误
    FILE_EXCEPTION(9998),
    // 权限错误
    AUTHENTICATION_ERROR(9997),
    // 支付相关的错误
    PAYMENT_ERROR(9996),
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
    // 用户病例信息相关错误
    USER_MEDICAL_HISTORY(3006),
    // 管理员相关错误
    ADMIN(3007),
    // 预约操作相关操作
    APPOINTMENT(4000),
    // 模板异常
    TEMPLATE(4001),
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
