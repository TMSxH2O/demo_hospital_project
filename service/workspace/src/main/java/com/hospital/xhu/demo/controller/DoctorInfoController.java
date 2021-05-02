package com.hospital.xhu.demo.controller;

import com.hospital.xhu.demo.entity.DoctorInfo;
import com.hospital.xhu.demo.service.IDoctorInfoService;
import com.hospital.xhu.demo.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/1
 */
@RestController
@Slf4j
public class DoctorInfoController {
    @Resource(name = "doctorInfoServiceImpl")
    private IDoctorInfoService doctorInfoService;

    @GetMapping("doctors")
    public CommonResult<?> selectDoctorInfo(
            @RequestBody Map<String, Object> params) {
        Map<String, Object> p = (Map<String, Object>) params.getOrDefault("p", Collections.emptyMap());
        Integer pageNum = (Integer) params.getOrDefault("pageNum", null);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", null);
        String orderedKey = (String) params.getOrDefault("ordered", null);
        Boolean isDesc = (Boolean) params.getOrDefault("desc", null);
        return doctorInfoService.selectDoctorInfo(p, pageNum, pageSize, orderedKey, isDesc);
    }

    @GetMapping("department/doctors")
    public CommonResult<?> selectDoctorInDepartment(
            @RequestBody Map<String, Object> params) {
        Long departmentId = (Long) params.getOrDefault("p", null);
        Integer pageNum = (Integer) params.getOrDefault("pageNum", null);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", null);
        String orderedKey = (String) params.getOrDefault("ordered", null);
        Boolean isDesc = (Boolean) params.getOrDefault("desc", null);
        return doctorInfoService.selectDepartmentDoctors(departmentId, pageNum, pageSize, orderedKey, isDesc);
    }

    @PostMapping("doctors/update")
    public CommonResult<?> updateDoctorInfo(
            @RequestBody Map<String, Object> params) {
        Map<String, Object> beforeValue = (Map<String, Object>) params.getOrDefault("before", Collections.emptyMap());
        Map<String, Object> afterValue = (Map<String, Object>) params.getOrDefault("after", Collections.emptyMap());
        return doctorInfoService.updateDoctorInfo(beforeValue, afterValue);
    }

    @PostMapping("doctors/insert")
    public CommonResult<?> insertDoctorInfo(
            @RequestBody List<DoctorInfo> list) {
        return doctorInfoService.insertDoctorInfo(list);
    }

    @PostMapping("doctors/delete")
    public CommonResult<?> deleteDoctorInfo(
            @RequestBody Map<String, Object> delete) {
        return doctorInfoService.deleteDoctorInfos(delete);
    }
}
