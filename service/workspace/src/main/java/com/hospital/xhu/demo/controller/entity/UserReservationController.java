package com.hospital.xhu.demo.controller.entity;

import com.hospital.xhu.demo.entity.TempUserReservation;
import com.hospital.xhu.demo.service.ITempUserReservationService;
import com.hospital.xhu.demo.utils.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/5
 */
@RestController
public class UserReservationController {
    @Resource(name = "tempUserReservationServiceImpl")
    private ITempUserReservationService userReservationService;

    @PostMapping("reservations")
    public CommonResult<?> selectUserReservation(
            @RequestBody(required = false) Map<String, Object> params) {
        if (null == params) {
            params = new HashMap<>(0);
        }
        Map<String, Object> p = (Map<String, Object>) params.getOrDefault("p", Collections.emptyMap());
        Integer pageNum = (Integer) params.getOrDefault("pageNum", null);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", null);
        String orderedKey = (String) params.getOrDefault("ordered", null);
        Boolean isDesc = (Boolean) params.getOrDefault("desc", null);
        return userReservationService.selectUserReservation(p, pageNum, pageSize, orderedKey, isDesc);
    }

    @PostMapping("reservations/count")
    public CommonResult<?> selectCountUserReservation(
            @RequestBody(required = false) Map<String, Object> params) {
        if (null == params) {
            params = new HashMap<>(0);
        }
        Map<String, Object> p = (Map<String, Object>) params.getOrDefault("p", Collections.emptyMap());
        return userReservationService.selectCountUserReservation(p);
    }

    @PostMapping("reservations/update")
    public CommonResult<?> updateUserReservation(
            @RequestBody Map<String, Object> params) {
        Map<String, Object> beforeValue = (Map<String, Object>) params.getOrDefault("before", Collections.emptyMap());
        Map<String, Object> afterValue = (Map<String, Object>) params.getOrDefault("after", Collections.emptyMap());
        return userReservationService.updateUserReservation(beforeValue, afterValue);
    }

    @PostMapping("reservations/insert")
    public CommonResult<?> insertUserReservation(
            @RequestBody List<TempUserReservation> userReservations) {
        return userReservationService.insertUserReservation(userReservations);
    }

    @PostMapping("reservations/delete")
    public CommonResult<?> deleteUserReservation(
            @RequestBody Map<String, Object> delete) {
        return userReservationService.deleteUserReservation(delete);
    }
}
