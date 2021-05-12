package com.hospital.xhu.demo.service;

import com.hospital.xhu.demo.entity.DepartmentInfo;
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
public interface IDepartmentInfoService {
    /**
     * 查询医院科室信息
     *
     * @param map        Optional 查询条件
     * @param orderedKey Optional 排序的字段
     * @param isDesc     Optional 是否反向
     * @param pageNum    Optional 页码
     * @param pageSize   Optional 页大小
     * @return 符合条件的医院信息列表
     */
    CommonResult<?> selectDepartmentInfo(
            Map<String, Object> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc);

    /**
     * 更新医院科室的数据
     *
     * @param selectKey   查询医院科室信息需要更新的值
     * @param newValueMap 修改的值
     * @return 修改的结果
     */
    CommonResult<?> updateDepartmentInfo(Map<String, Object> selectKey, Map<String, Object> newValueMap);

    /**
     * 插入新的医院科室数据
     *
     * @param departmentInfos 医院科室数据列表
     * @return 插入的结果
     */
    CommonResult<?> insertDepartmentInfo(List<DepartmentInfo> departmentInfos);

    /**
     * 删除医院科室的数据
     *
     * @param deleteKey 需要删除的医院科室数据
     * @return 删除的结果
     */
    CommonResult<?> deleteDepartmentInfo(Map<String, Object> deleteKey);
}
