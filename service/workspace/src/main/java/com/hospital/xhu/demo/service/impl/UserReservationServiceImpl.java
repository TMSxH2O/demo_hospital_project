package com.hospital.xhu.demo.service.impl;

import com.hospital.xhu.demo.dao.impl.UserReservationMapperImpl;
import com.hospital.xhu.demo.entity.UserReservation;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.service.IUserReservationService;
import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.enumerate.CommonCode;
import com.hospital.xhu.demo.utils.enumerate.CommonServiceMsg;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 用户订单信息相关操作
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 * @deprecated 数据库中存放的用户订单信息较少，替换为TempUserReservationServiceImpl更好
 */
@Service
@Slf4j
public class UserReservationServiceImpl implements IUserReservationService {
    private static final String CLASS_INFO_NAME = "预约订单 user_hospital_reservation";
    private final UserReservationMapperImpl userReservationMapper;

    public UserReservationServiceImpl(@Qualifier("userReservationMapperImpl") UserReservationMapperImpl userReservationMapper) {
        this.userReservationMapper = userReservationMapper;
    }

    /**
     * 查询预约订单信息
     *
     * @param map        Optional 查询条件
     * @param pageNum    Optional 页码
     * @param pageSize   Optional 页大小
     * @param orderedKey Optional 排序的字段
     * @param isDesc     Optional 是否反向
     * @return 符合条件的预约订单信息列表
     * - 成功
     * { code: 200, msg: 查询成功, data: 医院科室信息列表 }
     * - 失败
     * { code: ExceptionCode, msg: 查询失败信息, data: null }
     */
    @Override
    public CommonResult<?> selectUserReservation(
            Map<String, Object> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc) {
        try {
            List<UserReservation> result =
                    userReservationMapper.select(map, orderedKey, isDesc, pageNum, pageSize);
            String msg =
                    CommonServiceMsg.SELECT_SUCCESS.getMsg(CLASS_INFO_NAME, map, orderedKey, isDesc, pageNum, pageSize);
            return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, result);
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    @Override
    public CommonResult<?> selectCountUserReservation(Map<String, Object> map) {
        try {
            int result = userReservationMapper.selectCount(map);
            String msg =
                    CommonServiceMsg.SELECT_COUNT_SUCCESS.getMsg(CLASS_INFO_NAME, map);
            return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, result);
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 更新预约订单信息
     *
     * @param selectKey   查询预约订单需要更新的值
     * @param newValueMap 修改的值
     * @return 更新的结果
     * - 成功
     * { code: 200, msg: 更新成功, data: 更新的数量 }
     * - 失败
     * { code: ExceptionCode, msg: 更新失败信息, data: null }
     */
    @Override
    public CommonResult<?> updateUserReservation(Map<String, Object> selectKey, Map<String, Object> newValueMap) {
        if (CollectionUtils.isEmpty(selectKey) || CollectionUtils.isEmpty(newValueMap)) {
            return new CommonResult<>(
                    ExceptionCode.USER_RESERVATION.getCode(),
                    CommonServiceMsg.UPDATE_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME, selectKey, newValueMap));
        }

        try {
            int size = userReservationMapper.update(selectKey, newValueMap);
            if (size > 0) {
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(), CommonServiceMsg.UPDATE_SUCCESS.getMsg(CLASS_INFO_NAME), size);

            } else {
                return new CommonResult<>(
                        ExceptionCode.USER_RESERVATION.getCode(),
                        CommonServiceMsg.UPDATE_FAILED.getMsg(CLASS_INFO_NAME, selectKey, newValueMap));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 插入预约订单数据
     *
     * @param userReservations 预约订单列表
     * @return 插入的结果
     * - 成功
     * { code: 200, msg: 插入成功, data: { "count": 插入的数量, "result": 初始化后的数据列表 } }
     * - 失败
     * { code: ExceptionCode, msg: 插入失败信息, data: null }
     */
    @Override
    public CommonResult<?> insertUserReservation(List<UserReservation> userReservations) {
        if (CollectionUtils.isEmpty(userReservations)) {
            return new CommonResult<>(
                    ExceptionCode.USER_RESERVATION.getCode(),
                    CommonServiceMsg.INSERT_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME));
        }

        try {
            // 初始化所有的预约订单科室信息
            for (UserReservation userReservation : userReservations) {
                userReservation.init();
            }
            int result = userReservationMapper.insert(userReservations);
            if (result == userReservations.size()) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("count", result);
                map.put("result", userReservations);
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(),
                        CommonServiceMsg.INSERT_SUCCESS.getMsg(CLASS_INFO_NAME),
                        map);
            } else {
                return new CommonResult<>(
                        ExceptionCode.USER_RESERVATION.getCode(),
                        CommonServiceMsg.INSERT_FAILED.getMsg(CLASS_INFO_NAME, userReservations));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 删除预约订单数据
     *
     * @param deleteKey 需要删除的预约订单数据
     * @return 删除预约订单信息
     * - 成功
     * { code: 200, msg: 删除成功, data: 删除的数量 }
     * - 失败
     * { code: ExceptionCode, msg: 删除失败信息, data: null }
     */
    @Override
    public CommonResult<?> deleteUserReservation(Map<String, Object> deleteKey) {
        if (CollectionUtils.isEmpty(deleteKey)) {
            return new CommonResult<>(
                    ExceptionCode.USER_RESERVATION.getCode(),
                    CommonServiceMsg.DELETE_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME));
        }

        try {
            int result = userReservationMapper.delete(deleteKey);
            if (result > 0) {
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(),
                        CommonServiceMsg.DELETE_SUCCESS.getMsg(CLASS_INFO_NAME),
                        result);
            } else {
                return new CommonResult<>(
                        ExceptionCode.USER_RESERVATION.getCode(),
                        CommonServiceMsg.DELETE_FAILED.getMsg(CLASS_INFO_NAME, deleteKey));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }
}
