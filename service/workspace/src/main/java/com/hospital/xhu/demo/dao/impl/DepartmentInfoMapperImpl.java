package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.IDepartmentInfoMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapperImpl;
import com.hospital.xhu.demo.entity.DepartmentInfo;
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
@Repository("departmentInfoMapperImpl")
@Slf4j
public class DepartmentInfoMapperImpl extends GeneralMapperImpl<DepartmentInfo> {

    private static final Map<String, String> DEPARTMENT_INFO_MAP = new HashMap<>();

    static {
        DEPARTMENT_INFO_MAP.put("id", "department_id");
        DEPARTMENT_INFO_MAP.put("hospitalId", "hospital_id");
        DEPARTMENT_INFO_MAP.put("departmentName", "department_name");
    }

    public DepartmentInfoMapperImpl(@Qualifier("departmentInfoMapper") IDepartmentInfoMapper mapper) {
        super(mapper);
    }

    @Override
    protected String getSqlName() {
        return "department_info";
    }

    @Override
    protected Map<String, String> getMap() {
        return DEPARTMENT_INFO_MAP;
    }

    @Override
    protected ExceptionCode getExceptionCode() {
        return ExceptionCode.DEPARTMENT_INFO;
    }
}
