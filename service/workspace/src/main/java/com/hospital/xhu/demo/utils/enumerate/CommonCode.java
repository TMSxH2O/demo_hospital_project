package com.hospital.xhu.demo.utils.enumerate;

/**
 * Created with IntelliJ IDEA.
 * 返回结果的code
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/18
 */
public enum CommonCode {
    // 正常
    SUCCESS(200),
    // 出现异常
    SERVICE_ERROR(444),
    ;

    CommonCode(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }
}
