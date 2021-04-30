package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.IHospitalInfoMapper;
import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapper;
import com.hospital.xhu.demo.entity.HospitalInfo;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.resultcode.ExceptionCode;
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
public class HospitalInfoMapper extends GeneralMapper<HospitalInfo, IGeneralMapper<HospitalInfo>> {
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

    public HospitalInfoMapper(@Qualifier("hospitalInfoMapper") IHospitalInfoMapper hospitalInfoMapper) {
        super(hospitalInfoMapper);
    }

    /**
     * 将Map中的key从类属性转换为数据库字段名
     *
     * @param map 转换前的Map
     * @return 转换后的Map
     */
    @Override
    protected Map<String, Object> rebuildMap(Map<String, Object> map) throws ProjectException {
        Map<String, Object> result = new HashMap<>(map.size());
        for (String key : map.keySet()) {
            if (HOSPITAL_INFO_MAP.containsKey(key)) {
                result.put(HOSPITAL_INFO_MAP.get(key), map.get(key));
            }
            else {
                throw new ProjectException(ExceptionCode.HOSPITAL_INFO, "医院数据字段失败");
            }
        }
        log.debug("数据转换 >> before:" + map + " after:" + result);

        return result;
    }

    @Override
    protected String getMapString(String key) throws ProjectException {
        if (HOSPITAL_INFO_MAP.containsKey(key)) {
            return HOSPITAL_INFO_MAP.get(key);
        }
        else {
            throw new ProjectException(ExceptionCode.HOSPITAL_INFO, "医院数据字段失败");
        }
    }
}
