package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.IDoctorInfoMapper;
import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapperImpl;
import com.hospital.xhu.demo.entity.DoctorInfo;
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
@Repository("doctorInfoMapperImpl")
@Slf4j
public class DoctorInfoMapperImpl extends GeneralMapperImpl<DoctorInfo> {

    private static final Map<String, String> DOCTOR_INFO_MAP = new HashMap<>();

    static {
        DOCTOR_INFO_MAP.put("id", "doctor_id");
        DOCTOR_INFO_MAP.put("departmentId", "department_id");
        DOCTOR_INFO_MAP.put("doctorName", "doctor_name");
        DOCTOR_INFO_MAP.put("reservationPrice", "reservation_price");
        DOCTOR_INFO_MAP.put("remainingCapacity", "remaining_capacity");
        DOCTOR_INFO_MAP.put("doctorImageUri", "doctor_image_uri");
    }

    public DoctorInfoMapperImpl(@Qualifier("doctorInfoMapper") IDoctorInfoMapper doctorInfoMapper) {
        super(doctorInfoMapper);
    }

    @Override
    protected String getSqlName() {
        return "doctor_info";
    }

    @Override
    protected Map<String, String> getMap() {
        return DOCTOR_INFO_MAP;
    }

    @Override
    protected ExceptionCode getExceptionCode() {
        return ExceptionCode.DOCTOR_INFO;
    }
}
