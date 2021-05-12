package com.hospital.xhu.demo.service.impl;

import com.hospital.xhu.demo.dao.impl.DoctorInfoMapperImpl;
import com.hospital.xhu.demo.entity.DoctorInfo;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.service.IDoctorInfoService;
import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.helper.DoctorInfoHelper;
import com.hospital.xhu.demo.utils.enumerate.CommonCode;
import com.hospital.xhu.demo.utils.enumerate.CommonServiceMsg;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
@Service
public class DoctorInfoServiceImpl implements IDoctorInfoService {

    private static final String CLASS_INFO_NAME = "医生 doctor_info";
    private final DoctorInfoMapperImpl doctorInfoMapper;

    public DoctorInfoServiceImpl(@Qualifier("doctorInfoMapperImpl") DoctorInfoMapperImpl doctorInfoMapper) {
        this.doctorInfoMapper = doctorInfoMapper;
    }

    /**
     * 查询医生信息
     *
     * @param map        Optional 查询条件
     * @param pageNum    Optional 页码
     * @param pageSize   Optional 页大小
     * @param orderedKey Optional 排序的字段
     * @param isDesc     Optional 是否反向
     * @return 符合条件的医生信息列表
     * - 成功
     * { code: 200, msg: 查询成功, data: 医院科室信息列表 }
     * - 失败
     * { code: ExceptionCode, msg: 查询失败信息, data: null }
     */
    @Override
    public CommonResult<?> selectDoctorInfo(
            Map<String, Object> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc) {
        try {
            List<DoctorInfo> result =
                    doctorInfoMapper.select(map, orderedKey, isDesc, pageNum, pageSize);
            String msg =
                    CommonServiceMsg.SELECT_SUCCESS.getMsg(CLASS_INFO_NAME, map, orderedKey, isDesc, pageNum, pageSize);
            return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, result);
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 查询指定科室下的医生列表
     *
     * @param departmentId Optional 查询条件
     * @param pageNum      Optional 页码
     * @param pageSize     Optional 页大小
     * @param orderedKey   Optional 排序的字段
     * @param isDesc       Optional 是否反向
     * @return 符合条件的医生信息列表
     * - 成功
     * { code: 200, msg: 查询成功, data: 医院科室信息列表 }
     * - 失败
     * { code: ExceptionCode, msg: 查询失败信息, data: null }
     */
    @Override
    public CommonResult<?> selectDepartmentDoctors(
            Long departmentId, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc) {
        Map<String, Object> tempDepartmentIdMap = DoctorInfoHelper.tempDepartmentIdMap(departmentId);
        return selectDoctorInfo(tempDepartmentIdMap, pageNum, pageSize, orderedKey, isDesc);
    }

    /**
     * 更新医生信息
     *
     * @param selectKey   查询医生信息需要更新的值
     * @param newValueMap 修改的值
     * @return 更新的结果
     * - 成功
     * { code: 200, msg: 更新成功, data: 更新的数量 }
     * - 失败
     * { code: ExceptionCode, msg: 更新失败信息, data: null }
     */
    @Override
    public CommonResult<?> updateDoctorInfo(Map<String, Object> selectKey, Map<String, Object> newValueMap) {
        if (CollectionUtils.isEmpty(selectKey) || CollectionUtils.isEmpty(newValueMap)) {
            return new CommonResult<>(
                    ExceptionCode.DOCTOR_INFO.getCode(),
                    CommonServiceMsg.UPDATE_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME, selectKey, newValueMap));
        }

        try {
            int size = doctorInfoMapper.update(selectKey, newValueMap);
            if (size > 0) {
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(), CommonServiceMsg.UPDATE_SUCCESS.getMsg(CLASS_INFO_NAME), size);

            } else {
                return new CommonResult<>(
                        ExceptionCode.DOCTOR_INFO.getCode(),
                        CommonServiceMsg.UPDATE_FAILED.getMsg(CLASS_INFO_NAME, selectKey, newValueMap));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 插入医生数据
     *
     * @param doctorInfos 医生列表
     * @return 插入的结果
     * - 成功
     * { code: 200, msg: 插入成功, data: { "count": 插入的数量, "result": 初始化后的数据列表 } }
     * - 失败
     * { code: ExceptionCode, msg: 插入失败信息, data: null }
     */
    @Override
    public CommonResult<?> insertDoctorInfo(List<DoctorInfo> doctorInfos) {
        if (CollectionUtils.isEmpty(doctorInfos)) {
            return new CommonResult<>(
                    ExceptionCode.DOCTOR_INFO.getCode(),
                    CommonServiceMsg.INSERT_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME));
        }

        try {
            // 初始化所有的医院科室信息
            for (DoctorInfo doctorInfo : doctorInfos) {
                doctorInfo.init();
            }
            int result = doctorInfoMapper.insert(doctorInfos);
            if (result == doctorInfos.size()) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("count", result);
                map.put("result", doctorInfos);
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(),
                        CommonServiceMsg.INSERT_SUCCESS.getMsg(CLASS_INFO_NAME),
                        map);
            } else {
                return new CommonResult<>(
                        ExceptionCode.DOCTOR_INFO.getCode(),
                        CommonServiceMsg.INSERT_FAILED.getMsg(CLASS_INFO_NAME, doctorInfos));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 删除医生数据
     *
     * @param deleteKey 需要删除的医生数据
     * @return 删除医生信息
     * - 成功
     * { code: 200, msg: 删除成功, data: 删除的数量 }
     * - 失败
     * { code: ExceptionCode, msg: 删除失败信息, data: null }
     */
    @Override
    public CommonResult<?> deleteDoctorInfos(Map<String, Object> deleteKey) {
        if (CollectionUtils.isEmpty(deleteKey)) {
            return new CommonResult<>(
                    ExceptionCode.DOCTOR_INFO.getCode(),
                    CommonServiceMsg.DELETE_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME));
        }

        try {
            int result = doctorInfoMapper.delete(deleteKey);
            if (result > 0) {
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(),
                        CommonServiceMsg.DELETE_SUCCESS.getMsg(CLASS_INFO_NAME),
                        result);
            } else {
                return new CommonResult<>(
                        ExceptionCode.DOCTOR_INFO.getCode(),
                        CommonServiceMsg.DELETE_FAILED.getMsg(CLASS_INFO_NAME, deleteKey));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }
}
