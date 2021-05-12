package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.IHospitalInfoMapper;
import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapperImpl;
import com.hospital.xhu.demo.entity.HospitalInfo;
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
 * @date 2021/4/28
 */
@Repository("hospitalInfoMapperImpl")
@Slf4j
public class HospitalInfoMapperImpl extends GeneralMapperImpl<HospitalInfo> {
    /**
     * 存放类属性到数据库字段的映射
     */
    private static final Map<String, String> HOSPITAL_INFO_MAP = new HashMap<>();

    static {
        HOSPITAL_INFO_MAP.put("id", "hospital_id");
        HOSPITAL_INFO_MAP.put("hospitalName", "hospital_name");
        HOSPITAL_INFO_MAP.put("hospitalAddress", "hospital_address");
        HOSPITAL_INFO_MAP.put("phone", "hospital_phone");
    }

    public HospitalInfoMapperImpl(@Qualifier("hospitalInfoMapper") IHospitalInfoMapper hospitalInfoMapper) {
        super(hospitalInfoMapper);
    }

    @Override
    protected String getSqlName() {
        return "hospital_info";
    }

    @Override
    protected Map<String, String> getMap() {
        return HOSPITAL_INFO_MAP;
    }

    @Override
    protected ExceptionCode getExceptionCode() {
        return ExceptionCode.HOSPITAL_INFO;
    }
}
