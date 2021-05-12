package com.hospital.xhu.demo.utils.helper;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hospital.xhu.demo.entity.UserReservation;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/1
 */
@Slf4j
public class UserReservationHelper {

    private static final ObjectIdGenerators.UUIDGenerator UUID_GENERATOR = new ObjectIdGenerators.UUIDGenerator();

    /**
     * 获得用于查询预约订单对象的id对应数据
     *
     * @param id 预约订单id
     * @return { "id": id }
     */
    public static Map<String, Object> tempUserReservationId(String id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", id);
        return map;
    }

    /**
     * 获取查询对应时间的预约订单信息
     *
     * @param map  原Map，如果为空，新建HashMap；如果存在原有的map
     * @param date 具体时间，如果传入的值为null，将查询今天的预约信息
     * @return { "reservationDate": date }
     */
    public static Map<String, Object> tempUserReservationDate(Map<String, Object> map, LocalDate date) {
        Map<String, Object> result;
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

    /**
     * 获取订单医生id的查询Map
     *
     * @param map      原Map，如果为空，新建HashMap；如果存在原有的map，则直接在其基础上进行添加
     * @param doctorId 医生id
     * @return { "doctorId": doctorId }
     */
    public static Map<String, Object> tempUserReservationDoctorIdMap(Map<String, Object> map, long doctorId) {
        Map<String, Object> result;
        if (null == map) {
            result = new HashMap<>(1);
        } else {
            result = map;
        }
        result.put("doctorId", doctorId);
        return result;
    }

    /**
     * 获取订单状态的查询Map
     *
     * @param map    原Map，如果为空，新建HashMap；如果存在原有的map，则直接在其基础上进行添加
     * @param status 订单状态
     * @return { "reservationStatus", status }
     */
    public static Map<String, Object> tempUserReservationStatusMap(Map<String, Object> map, int status) {
        Map<String, Object> result;
        if (null == map) {
            result = new HashMap<>(1);
        } else {
            result = map;
        }
        result.put("reservationStatus", status);
        return result;
    }

    /**
     * 生成订单的id
     * 根据传入的数据，生辰唯一的id
     *
     * @param userId   用户id
     * @param doctorId 医生id
     * @param date     日期
     * @return 生成的订单唯一id
     */
    public static String generateReservationId(long userId, long doctorId, LocalDate date) {
        String randomName = userId + String.valueOf(doctorId) + date;
        String result = UUID_GENERATOR.generateId(randomName).toString();
        log.debug("生成订单号 userId:{} doctorId:{} date:{} > {}", userId, doctorId, date, result);
        return result;
    }

    public static String getReservationBody(UserReservation userReservation) {
        return String.format(
                "预约%s，共%f",
                userReservation.getReservationDate(),
                userReservation.getReservationPrice());
    }
}
