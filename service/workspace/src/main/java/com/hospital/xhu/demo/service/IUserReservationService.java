package com.hospital.xhu.demo.service;

import com.hospital.xhu.demo.entity.UserReservation;
import com.hospital.xhu.demo.utils.CommonResult;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
public interface IUserReservationService {

    /**
     * 查询预约订单信息
     *
     * @param map        Optional 查询条件
     * @param orderedKey Optional 排序的字段
     * @param isDesc     Optional 是否反向
     * @param pageNum    Optional 页码
     * @param pageSize   Optional 页大小
     * @return 符合条件的预约订单信息列表
     */
    CommonResult<?> selectUserReservation(
            Map<String, Object> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc);

    /**
     * 查询预约订单信息
     *
     * @param map 查询条件
     * @return 符合条件的预约订单信息列表
     */
    CommonResult<?> selectCountUserReservation(Map<String, Object> map);

    /**
     * 更新预约订单的数据
     *
     * @param selectKey   查询预约订单信息需要更新的值
     * @param newValueMap 修改的值
     * @return 修改的结果
     */
    CommonResult<?> updateUserReservation(Map<String, Object> selectKey, Map<String, Object> newValueMap);

    /**
     * 插入新的预约订单数据
     *
     * @param userReservations 预约订单数据列表
     * @return 插入的结果
     */
    CommonResult<?> insertUserReservation(List<UserReservation> userReservations);

    /**
     * 删除预约订单的数据
     *
     * @param deleteKey 需要删除的预约订单数据
     * @return 删除的结果
     */
    CommonResult<?> deleteUserReservation(Map<String, Object> deleteKey);
}
