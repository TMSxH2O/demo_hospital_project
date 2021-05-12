package com.hospital.xhu.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * 用于用户显示的预约订单信息
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/10
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TempUserReservation extends UserReservation {
    private String username;
    private String doctorName;
    private String departmentName;

    @Override
    public String toString() {
        return "TempUserReservation{" +
                "username='" + username + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", id='" + id + '\'' +
                ", userId=" + userId +
                ", doctorId=" + doctorId +
                ", reservationDate=" + reservationDate +
                ", reservationPrice=" + reservationPrice +
                ", reservationStatus=" + reservationStatus +
                '}';
    }
}
