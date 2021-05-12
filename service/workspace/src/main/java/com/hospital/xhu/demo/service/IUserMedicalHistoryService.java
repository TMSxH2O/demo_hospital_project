package com.hospital.xhu.demo.service;

import com.hospital.xhu.demo.entity.UserMedicalHistory;
import com.hospital.xhu.demo.utils.CommonResult;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/3
 */
public interface IUserMedicalHistoryService {

    /**
     * 查询病例信息
     *
     * @param map        Optional 查询条件
     * @param orderedKey Optional 排序的字段
     * @param isDesc     Optional 是否反向
     * @param pageNum    Optional 页码
     * @param pageSize   Optional 页大小
     * @return 符合条件的病例信息列表
     */
    CommonResult<?> selectUserMedicalHistory(
            Map<String, Object> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc);

    /**
     * 更新病例的数据
     *
     * @param selectKey 查询病例信息需要更新的值
     * @param updateMap 修改的值
     * @return 修改的结果
     */
    CommonResult<?> updateUserMedicalHistory(Map<String, Object> selectKey, Map<String, Object> updateMap);

    /**
     * 插入新的病例数据
     *
     * @param medicals 病例列表
     * @return 插入的结果
     */
    CommonResult<?> insertUserMedicalHistory(List<UserMedicalHistory> medicals);

    /**
     * 删除病例的数据
     *
     * @param deleteKey 需要删除的病例数据
     * @return 删除的结果
     */
    CommonResult<?> deleteUserMedicalHistory(Map<String, Object> deleteKey);

}
