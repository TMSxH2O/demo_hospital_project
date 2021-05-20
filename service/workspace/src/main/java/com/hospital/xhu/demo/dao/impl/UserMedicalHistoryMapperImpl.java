package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.IUserMedicalHistoryMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapperImpl;
import com.hospital.xhu.demo.entity.TempUserMedicalHistory;
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
 * @date 2021/5/2
 */
@Repository
@Slf4j
public class UserMedicalHistoryMapperImpl
        extends GeneralMapperImpl<TempUserMedicalHistory> {
    /**
     * 存放类属性到数据库字段的映射
     */
    private static final Map<String, String> USER_MEDICAL_HISTORY_MAP = new HashMap<>();

    static {
        USER_MEDICAL_HISTORY_MAP.put("id", "medical_history_id");
        USER_MEDICAL_HISTORY_MAP.put("userId", "user_id");
        USER_MEDICAL_HISTORY_MAP.put("doctorId", "doctor_id");
        USER_MEDICAL_HISTORY_MAP.put("medical_history_date", "medicalDate");
        USER_MEDICAL_HISTORY_MAP.put("medical_history_uri", "medicalHistoryUri");
    }

    public UserMedicalHistoryMapperImpl(
            @Qualifier("userMedicalHistoryMapper") IUserMedicalHistoryMapper userMedicalHistoryMapper) {
        super(userMedicalHistoryMapper);
    }


    @Override
    protected String getSqlName() {
        return "user_medical_history";
    }

    @Override
    protected Map<String, String> getMap() {
        return USER_MEDICAL_HISTORY_MAP;
    }

    @Override
    protected ExceptionCode getExceptionCode() {
        return ExceptionCode.USER_MEDICAL_HISTORY;
    }
}
