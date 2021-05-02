package com.hospital.xhu.demo.entity;

import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.resultcode.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Created with IntelliJ IDEA.
 * 用户预约订单信息
 *
 * - id 是随机生成的字符串编号
 * - reservationPrice 订单的价格，默认对应的医生信息中的价格
 * - reservationStatus 预约订单的状态，0未支付，1已支付，2已报道，3已处理，9已取消
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserReservation implements Entity {
    private String id;
    private Long userId;
    private Long doctorId;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDate reservationDate;
    private Float reservationPrice;
    private Integer reservationStatus;

    @Override
    public void init() throws ProjectException {
        if (null == userId) {
            throw new ProjectException(ExceptionCode.USER_RESERVATION, "预约订单对应的用户id不能为空");
        }
        if (null == doctorId) {
            throw new ProjectException(ExceptionCode.USER_RESERVATION, "预约订单对应的医生id不能为空");
        }
        if (null == reservationDate) {
            throw new ProjectException(ExceptionCode.USER_RESERVATION, "预约订单对应的时间不能为空");
        }
        if (null == reservationPrice) {
            // 订单的价格通过查询对应医生的价格来填写，此处单独出来，如果有需要，可以支持打折
            reservationPrice = 0.0f;
        }
        if (null == reservationStatus) {
            // 默认设置状态为未支付
            reservationStatus = 0;
        }
    }
}
