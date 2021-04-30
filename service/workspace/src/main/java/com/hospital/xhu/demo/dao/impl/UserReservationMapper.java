package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.IUserReservationMapper;
import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapper;
import com.hospital.xhu.demo.entity.UserReservation;
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
 * @date 2021/4/30
 */
@Repository("userReservationMapperImpl")
@Slf4j
public class UserReservationMapper extends GeneralMapper<UserReservation, IGeneralMapper<UserReservation>> {
    /**
     * 存放类属性到数据库字段的映射
     */
    private static final Map<String, String> USER_RESERVATION_MAP = new HashMap<>();

    static {
        USER_RESERVATION_MAP.put("id", "doctor_id");
        USER_RESERVATION_MAP.put("departmentId", "department_id");
        USER_RESERVATION_MAP.put("doctorName", "doctor_name");
        USER_RESERVATION_MAP.put("reservationPrice", "reservation_price");
        USER_RESERVATION_MAP.put("remainingCapacity", "remaining_capacity");
        USER_RESERVATION_MAP.put("doctorImageUri", "doctor_image_uri");
    }

    public UserReservationMapper(@Qualifier("userReservationMapper") IUserReservationMapper userReservationMapper) {
        super(userReservationMapper);
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
            if (USER_RESERVATION_MAP.containsKey(key)) {
                result.put(USER_RESERVATION_MAP.get(key), map.get(key));
            }
            else {
                throw new ProjectException(ExceptionCode.USER_RESERVATION, "预约订单数据字段失败");
            }
        }
        log.debug("数据转换 >> before:" + map + " after:" + result);

        return result;
    }

    @Override
    protected String getMapString(String key) throws ProjectException {
        if (USER_RESERVATION_MAP.containsKey(key)) {
            return USER_RESERVATION_MAP.get(key);
        }
        else {
            throw new ProjectException(ExceptionCode.USER_RESERVATION, "预约订单数据字段失败");
        }
    }
}
