package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.IUserReservationMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapperImpl;
import com.hospital.xhu.demo.entity.UserReservation;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
@Repository("userReservationMapperImpl")
@Slf4j
public class UserReservationMapperImpl extends GeneralMapperImpl<UserReservation> {
    /**
     * 存放类属性到数据库字段的映射
     */
    private static final Map<String, String> USER_RESERVATION_MAP = new HashMap<>();

    static {
        USER_RESERVATION_MAP.put("id", "reservation_id");
        USER_RESERVATION_MAP.put("userId", "user_id");
        USER_RESERVATION_MAP.put("doctorId", "doctor_id");
        USER_RESERVATION_MAP.put("reservationDate", "reservation_date");
        USER_RESERVATION_MAP.put("reservationPrice", "reservation_price");
        USER_RESERVATION_MAP.put("reservationStatus", "reservation_status");
    }

    public UserReservationMapperImpl(@Qualifier("userReservationMapper") IUserReservationMapper userReservationMapper) {
        super(userReservationMapper);
    }

    @Override
    protected String getSqlName() {
        return "user_hospital_reservation_info";
    }

    @Override
    protected Map<String, String> getMap() {
        return USER_RESERVATION_MAP;
    }

    @Override
    protected ExceptionCode getExceptionCode() {
        return ExceptionCode.USER_RESERVATION;
    }
}
