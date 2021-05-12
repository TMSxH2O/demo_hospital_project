package com.hospital.xhu.demo.service;

import com.hospital.xhu.demo.utils.CommonResult;

import java.time.LocalDate;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/1
 */
public interface IAppointmentService {
    /**
     * 预约方法，通过用户、医生以及时间来进行预约，生成唯一的预约订单号，并更新数据库
     *
     * @param userId          用户id
     * @param doctorId        医生id
     * @param reservationDate 预约时间
     * @return 预约结果
     */
    CommonResult<?> reservation(Long userId, Long doctorId, LocalDate reservationDate);

    /**
     * 查询医生在指定日期的剩余容量
     *
     * @param doctorId 医生编号
     * @param date     指定日期
     * @return 查询结果
     */
    CommonResult<?> selectDoctorReservationAvailableCapacity(Long doctorId, LocalDate date);
}
