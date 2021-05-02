package com.hospital.xhu.demo.service.impl;

import com.hospital.xhu.demo.dao.impl.DoctorInfoMapperImpl;
import com.hospital.xhu.demo.dao.impl.UserInfoMapperImpl;
import com.hospital.xhu.demo.dao.impl.UserReservationMapperImpl;
import com.hospital.xhu.demo.entity.DoctorInfo;
import com.hospital.xhu.demo.entity.UserReservation;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.service.IAppointmentService;
import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.helper.DoctorInfoHelper;
import com.hospital.xhu.demo.utils.helper.UserInfoHelper;
import com.hospital.xhu.demo.utils.helper.UserReservationHelper;
import com.hospital.xhu.demo.utils.resultcode.CommonCode;
import com.hospital.xhu.demo.utils.resultcode.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/1
 */
@Service
@Slf4j
public class AppointmentServiceImpl implements IAppointmentService {
    @Resource(name = "userInfoMapperImpl")
    private UserInfoMapperImpl userInfoMapper;
    @Resource(name = "doctorInfoMapperImpl")
    private DoctorInfoMapperImpl doctorInfoMapper;
    @Resource(name = "userReservationMapperImpl")
    private UserReservationMapperImpl userReservationMapper;

    /**
     * 预约方法
     *
     * @param userId          用户id
     * @param doctorId        医生id
     * @param reservationDate 预约时间
     * @return 预约结果
     * - 成功
     * { code: 200, msg: 预约成功, data: 预约订单号 }
     * - 失败
     * { code: ExceptionCode, msg: 预约失败信息, data: null }
     */
    @Override
    public CommonResult<Object> reservation(
            Long userId, Long doctorId, LocalDate reservationDate) {
        if (null == userId || null == doctorId || null == reservationDate) {
            String temp = "预约订单失败，缺少必要的参数，请检查参数是否正确: %d %d %s";
            String msg = String.format(temp, userId, doctorId, reservationDate);
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.APPOINTMENT.getCode(), msg);
        }

        try {
            Map<String, Object> tempUserIdMap = UserInfoHelper.tempUserIdMap(userId);
            // 只是为了检查用户编号是否正确，用户的信息不重要
            userInfoMapper.selectPrimary(tempUserIdMap);

            Map<String, Object> tempDoctorIdMap = DoctorInfoHelper.tempDoctorIdMap(doctorId);
            // 查询医生信息
            DoctorInfo doctorInfo = doctorInfoMapper.selectPrimary(tempDoctorIdMap);

            Map<String, Object> tempReservationDataMap =
                    UserReservationHelper.tempUserReservationDate(null, reservationDate);
            UserReservationHelper.tempUserReservationDoctorIdMap(tempReservationDataMap, doctorId);
            // 查询指定日期的医生订单数量
            int count = userReservationMapper.selectCount(tempReservationDataMap);

            if (count >= doctorInfo.getRemainingCapacity()) {
                String temp = "预约订单失败，目标医生%d在[%s]容量已满，请选择其他时间或其他医生";
                String msg = String.format(temp, doctorId, reservationDate);
                log.warn(msg);
                return new CommonResult<>(ExceptionCode.APPOINTMENT.getCode(), msg);
            } else {
                // 生成随机的id
                String reservationId = UserReservationHelper.generateReservationId(userId, doctorId, reservationDate);
                Float price = doctorInfo.getReservationPrice();
                // 创建新的 UserReservation，订单状态使用默认状态（未支付）
                UserReservation tempUserReservation =
                        new UserReservation(reservationId, userId, doctorId, reservationDate, price, null);
                // 插入数据库
                userReservationMapper.insert(Collections.singletonList(tempUserReservation));

                String msg = "预约订单成功 > " + tempUserReservation;
                log.debug(msg);
                return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg);
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 查询医生在指定日期的剩余容量
     *
     * @param doctorId 医生编号
     * @param date     指定日期
     * @return 查询的结果
     * - 成功
     * { code: 200, msg: 查询成功, data: 剩余容量 }
     * - 失败
     * { code: ExceptionCode, msg: 查询失败信息, date: null }
     */
    @Override
    public CommonResult<Object> selectDoctorReservationAvailableCapacity(Long doctorId, LocalDate date) {
        if (null == doctorId || null == date) {
            String temp = "查询剩余容量失败，缺少必要的参数，请检查参数是否正确: %d %s";
            String msg = String.format(temp, doctorId, date);
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.APPOINTMENT.getCode(), msg);
        }

        try {
            Map<String, Object> tempDoctorIdMap = DoctorInfoHelper.tempDoctorIdMap(doctorId);
            // 查询医生信息
            DoctorInfo doctorInfo = doctorInfoMapper.selectPrimary(tempDoctorIdMap);

            // 预约订单的查询条件
            Map<String, Object> tempUserReservationMap =
                    UserReservationHelper.tempUserReservationDoctorIdMap(null, doctorId);
            UserReservationHelper.tempUserReservationDate(tempUserReservationMap, date);
            // 查询符合条件的订单的数量
            int count = userReservationMapper.selectCount(tempUserReservationMap);
            int remaining = doctorInfo.getRemainingCapacity() - count;

            if (remaining < 0) {
                log.error("查询{}医生在{}的剩余容量为 > {} 请检查", doctorId, date, remaining);
            }

            String temp = "查询剩余容量成功，%d医生在%s还剩余 > %d";
            String msg = String.format(temp, doctorId, date, remaining);
            log.debug(msg);
            return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg);
        } catch (ProjectException e) {
            return e.getResult();
        }
    }
}
