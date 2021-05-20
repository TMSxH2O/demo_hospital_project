package com.hospital.xhu.demo.service.impl;

import com.hospital.xhu.demo.dao.impl.UserMedicalHistoryMapperImpl;
import com.hospital.xhu.demo.entity.TempUserMedicalHistory;
import com.hospital.xhu.demo.entity.UserMedicalHistory;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.service.IUserMedicalHistoryService;
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
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/3
 */
@Service
@Slf4j
public class UserMedicalHistoryServiceImpl implements IUserMedicalHistoryService {
    private static final String CLASS_INFO_NAME = "病例 user_medical_history";
    private final UserMedicalHistoryMapperImpl userMedicalHistoryMapper;

    public UserMedicalHistoryServiceImpl(
            @Qualifier("userMedicalHistoryMapperImpl") UserMedicalHistoryMapperImpl userMedicalHistoryMapper) {
        this.userMedicalHistoryMapper = userMedicalHistoryMapper;
    }

    /**
     * 查询病例信息
     *
     * @param map        Optional 查询条件
     * @param pageNum    Optional 页码
     * @param pageSize   Optional 页大小
     * @param orderedKey Optional 排序的字段
     * @param isDesc     Optional 是否反向
     * @return 符合条件的病例信息列表
     * - 成功
     * { code: 200, msg: 查询成功, data: 病例信息列表 }
     * - 失败
     * { code: ExceptionCode, msg: 查询失败信息, data: null }
     */
    @Override
    public CommonResult<?> selectUserMedicalHistory(
            Map<String, Object> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc) {
        try {
            List<TempUserMedicalHistory> result =
                    userMedicalHistoryMapper.select(map, orderedKey, isDesc, pageNum, pageSize);
            String msg =
                    CommonServiceMsg.SELECT_SUCCESS.getMsg(CLASS_INFO_NAME, map, orderedKey, isDesc, pageNum, pageSize);
            return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, result);
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    @Override
    public CommonResult<?> selectCountUserMedicalHistory(Map<String, Object> map) {
        try {
            int result = userMedicalHistoryMapper.selectCount(map);
            String msg =
                    CommonServiceMsg.SELECT_COUNT_SUCCESS.getMsg(CLASS_INFO_NAME, map);
            return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, result);
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 更新病例信息
     *
     * @param selectKey 查询病例信息需要更新的值
     * @param updateMap 修改的值
     * @return 更新的结果
     * - 成功
     * { code: 200, msg: 更新成功, data: 更新的数量 }
     * - 失败
     * { code: ExceptionCode, msg: 更新失败信息, data: null }
     */
    @Override
    public CommonResult<?> updateUserMedicalHistory(Map<String, Object> selectKey, Map<String, Object> updateMap) {
        if (CollectionUtils.isEmpty(selectKey) || CollectionUtils.isEmpty(updateMap)) {
            return new CommonResult<>(
                    ExceptionCode.USER_MEDICAL_HISTORY.getCode(),
                    CommonServiceMsg.UPDATE_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME, selectKey, updateMap));
        }

        try {
            int size = userMedicalHistoryMapper.update(selectKey, updateMap);
            if (size > 0) {
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(), CommonServiceMsg.UPDATE_SUCCESS.getMsg(CLASS_INFO_NAME), size);

            } else {
                return new CommonResult<>(
                        ExceptionCode.USER_MEDICAL_HISTORY.getCode(),
                        CommonServiceMsg.UPDATE_FAILED.getMsg(CLASS_INFO_NAME, selectKey, updateMap));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 插入病例数据
     *
     * @param medicals 病例数据列表
     * @return 插入的结果
     * - 成功
     * { code: 200, msg: 插入成功, data: { "count": 插入的数量, "result": 初始化后的数据列表 } }
     * - 失败
     * { code: ExceptionCode, msg: 插入失败信息, data: null }
     */
    @Override
    public CommonResult<?> insertUserMedicalHistory(List<TempUserMedicalHistory> medicals) {
        if (CollectionUtils.isEmpty(medicals)) {
            return new CommonResult<>(
                    ExceptionCode.USER_MEDICAL_HISTORY.getCode(),
                    CommonServiceMsg.INSERT_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME));
        }

        try {
            // 初始化所有的科室信息
            for (UserMedicalHistory medicalHistory : medicals) {
                medicalHistory.init();
            }
            int result = userMedicalHistoryMapper.insert(medicals);
            if (result == medicals.size()) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("count", result);
                map.put("result", medicals);
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(),
                        CommonServiceMsg.INSERT_SUCCESS.getMsg(CLASS_INFO_NAME),
                        map);
            } else {
                return new CommonResult<>(
                        ExceptionCode.USER_MEDICAL_HISTORY.getCode(),
                        CommonServiceMsg.INSERT_FAILED.getMsg(CLASS_INFO_NAME, medicals));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 删除病例数据
     *
     * @param deleteKey 需要删除的病例数据
     * @return 删除病例信息
     * - 成功
     * { code: 200, msg: 删除成功, data: 删除的数量 }
     * - 失败
     * { code: ExceptionCode, msg: 删除失败信息, data: null }
     */
    @Override
    public CommonResult<?> deleteUserMedicalHistory(Map<String, Object> deleteKey) {
        if (CollectionUtils.isEmpty(deleteKey)) {
            return new CommonResult<>(
                    ExceptionCode.USER_MEDICAL_HISTORY.getCode(),
                    CommonServiceMsg.DELETE_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME));
        }

        try {
            int result = userMedicalHistoryMapper.delete(deleteKey);
            if (result > 0) {
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(),
                        CommonServiceMsg.DELETE_SUCCESS.getMsg(CLASS_INFO_NAME),
                        result);
            } else {
                return new CommonResult<>(
                        ExceptionCode.USER_MEDICAL_HISTORY.getCode(),
                        CommonServiceMsg.DELETE_FAILED.getMsg(CLASS_INFO_NAME, deleteKey));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }
}
