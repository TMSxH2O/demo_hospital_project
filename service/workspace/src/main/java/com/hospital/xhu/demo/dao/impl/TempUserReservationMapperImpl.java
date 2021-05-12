package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.ITempUserReservationMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapperImpl;
import com.hospital.xhu.demo.entity.TempUserReservation;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/10
 */
@Repository("tempUserReservationMapperImpl")
public class TempUserReservationMapperImpl extends GeneralMapperImpl<TempUserReservation> {

    private static final Map<String, String> TEMP_USER_RESERVATION_MAP = new HashMap<>();

    static {
        TEMP_USER_RESERVATION_MAP.put("id", "reservation_id");
        TEMP_USER_RESERVATION_MAP.put("userId", "user_id");
        TEMP_USER_RESERVATION_MAP.put("username", "username");
        TEMP_USER_RESERVATION_MAP.put("doctorId", "doctor_id");
        TEMP_USER_RESERVATION_MAP.put("reservationDate", "reservation_date");
        TEMP_USER_RESERVATION_MAP.put("reservationPrice", "reservation_price");
        TEMP_USER_RESERVATION_MAP.put("reservationStatus", "reservation_status");
    }

    public TempUserReservationMapperImpl(
            @Qualifier("tempUserReservationMapper") ITempUserReservationMapper tempUserReservationMapper) {
        super(tempUserReservationMapper);
    }

    @Override
    protected String getSqlName() {
        return "user_hospital_reservation_info(doctor_info, department_info)";
    }

    @Override
    protected Map<String, String> getMap() {
        return TEMP_USER_RESERVATION_MAP;
    }

    @Override
    protected ExceptionCode getExceptionCode() {
        return ExceptionCode.USER_RESERVATION;
    }
}
