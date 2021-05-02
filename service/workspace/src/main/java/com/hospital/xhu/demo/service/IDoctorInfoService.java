package com.hospital.xhu.demo.service;

import com.hospital.xhu.demo.entity.DoctorInfo;
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
public interface IDoctorInfoService {

    /**
     * 查询医生信息
     *
     * @param map        Optional 查询条件
     * @param orderedKey Optional 排序的字段
     * @param isDesc     Optional 是否反向
     * @param pageNum    Optional 页码
     * @param pageSize   Optional 页大小
     * @return 符合条件的医生信息列表
     */
    CommonResult<Object> selectDoctorInfo(
            Map<String, Object> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc);

    /**
     * 查询指定科室下的所有医生信息
     *
     * @param departmentId Optional 查询条件
     * @param orderedKey   Optional 排序的字段
     * @param isDesc       Optional 是否反向
     * @param pageNum      Optional 页码
     * @param pageSize     Optional 页大小
     * @return 符合条件的医生列表
     */
    CommonResult<Object> selectDepartmentDoctors(
            Long departmentId, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc);

    /**
     * 更新医生的数据
     *
     * @param selectKey   查询医生信息需要更新的值
     * @param newValueMap 修改的值
     * @return 修改的结果
     */
    CommonResult<Object> updateDoctorInfo(Map<String, Object> selectKey, Map<String, Object> newValueMap);

    /**
     * 插入新的医生数据
     *
     * @param doctorInfos 医生数据列表
     * @return 插入的结果
     */
    CommonResult<Object> insertDoctorInfo(List<DoctorInfo> doctorInfos);

    /**
     * 删除医生的数据
     *
     * @param deleteKey 需要删除的医生数据
     * @return 删除的结果
     */
    CommonResult<Object> deleteDoctorInfos(Map<String, Object> deleteKey);
}
