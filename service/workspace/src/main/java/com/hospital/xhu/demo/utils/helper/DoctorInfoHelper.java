package com.hospital.xhu.demo.utils.helper;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/1
 */
@Slf4j
public class DoctorInfoHelper {

    /**
     * 获得doctor_id的Map
     *
     * @param doctorId 所需查询的医生id
     * @return { "id": doctorId }
     */
    public static Map<String, Object> tempDoctorIdMap(long doctorId) {
        return Collections.singletonMap("id", doctorId);
    }

    /**
     * 获得departmentId的Map
     *
     * @param departmentId 所需查询的科室id
     * @return { "departmentId": departmentId }
     */
    public static Map<String, Object> tempDepartmentIdMap(long departmentId) {
        return Collections.singletonMap("departmentId", departmentId);
    }
}
