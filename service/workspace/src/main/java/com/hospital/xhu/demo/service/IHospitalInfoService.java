package com.hospital.xhu.demo.service;

import com.hospital.xhu.demo.entity.HospitalInfo;
import com.hospital.xhu.demo.utils.CommonResult;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/28
 */
public interface IHospitalInfoService {
    /**
     * 查询医院信息
     *
     * @param map        Optional 查询条件
     * @param orderedKey Optional 排序的字段
     * @param isDesc     Optional 是否反向
     * @param pageNum    Optional 页码
     * @param pageSize   Optional 页大小
     * @return 符合条件的医院信息列表
     */
    CommonResult<Object> selectHospitalInfo(
            Map<String, Object> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc);

    /**
     * 更新医院的数据
     *
     * @param selectKey   查询医院信息需要更新的值
     * @param newValueMap 修改的值
     * @return 修改的结果
     */
    CommonResult<Object> updateHospitalInfo(Map<String, Object> selectKey, Map<String, Object> newValueMap);

    /**
     * 插入新的医院数据
     *
     * @param hospitalInfos 医院数据列表
     * @return 插入的结果
     */
    CommonResult<Object> insertHospitalInfo(List<HospitalInfo> hospitalInfos);

    /**
     * 删除医院的数据
     *
     * @param deleteKey 需要删除的医院数据
     * @return 删除的结果
     */
    CommonResult<Object> deleteHospitalInfo(Map<String, Object> deleteKey);
}
