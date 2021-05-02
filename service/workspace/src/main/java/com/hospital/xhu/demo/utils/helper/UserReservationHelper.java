package com.hospital.xhu.demo.utils.helper;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/1
 */
@Slf4j
public class UserReservationHelper {

    /**
     * 获取查询对应时间的预约订单信息
     *
     * @param map  原Map，如果为空，新建HashMap；如果存在原有的map
     * @param date 具体时间，如果传入的值为null，将查询今天的预约信息
     * @return { "reservationDate": date }
     */
    public static Map<String, Object> tempUserReservationDate(Map<String, Object> map, LocalDate date) {
        Map<String, Object> result = null;
        if (null == map) {
            result = new HashMap<>(1);
        } else {
            result = map;
        }
        if (null != date) {
            result.put("reservationDate", date);
        } else {
            result.put("reservationDate", LocalDate.now());
        }
        return result;
    }

    public static Map<String, Object> tempUserReservationDoctorIdMap(Map<String, Object> map, long doctorId) {
        Map<String, Object> result = null;
        if (null == map) {
            result = new HashMap<>(1);
        } else {
            result = map;
        }
        result.put("doctorId", doctorId);
        return result;
    }

    public static String generateReservationId(long userId, long doctorId, LocalDate date) {
        String randomName = String.valueOf(userId) + String.valueOf(doctorId) + String.valueOf(date);
        return UUID.fromString(randomName).toString();
    }
}
