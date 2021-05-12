package com.hospital.xhu.demo.utils.payment;

import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;

/**
 * Created with IntelliJ IDEA.
 * 订单状态的枚举类
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/10
 */
public enum ReservationStatus {
    // 未支付
    UNPAID(0) {
        @Override
        public boolean tryStatus(ReservationStatus status) {
            return status == PAID || status == CANCEL;
        }
    },
    // 已支付
    PAID(1) {
        @Override
        public boolean tryStatus(ReservationStatus status) {
            return status == REPORTED || status == CANCEL;
        }
    },
    // 已报道
    REPORTED(2) {
        @Override
        public boolean tryStatus(ReservationStatus status) {
            return status == PROCESSED || status == CANCEL;
        }
    },
    // 已处理
    PROCESSED(3) {
        @Override
        public boolean tryStatus(ReservationStatus status) {
            return false;
        }
    },
    // 已取消
    CANCEL(4) {
        @Override
        public boolean tryStatus(ReservationStatus status) {
            return false;
        }
    },
    ;

    private final int status;

    ReservationStatus(int status) {
        this.status = status;
    }

    public int get() {
        return status;
    }

    public abstract boolean tryStatus(ReservationStatus status);

    public static ReservationStatus getInstance(int code) throws ProjectException {
        for (ReservationStatus status : ReservationStatus.values()) {
            if (code == status.get()) {
                return status;
            }
        }
        throw new ProjectException(ExceptionCode.DATA_EXCEPTION, "订单状态中没有对应的值");
    }
}
