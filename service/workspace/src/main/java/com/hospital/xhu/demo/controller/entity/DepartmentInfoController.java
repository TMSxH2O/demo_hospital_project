package com.hospital.xhu.demo.controller.entity;

import com.hospital.xhu.demo.entity.DepartmentInfo;
import com.hospital.xhu.demo.service.IDepartmentInfoService;
import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.annotation.PassToken;
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
 * @date 2021/5/1
 */
@RestController
public class DepartmentInfoController {
    @Resource(name = "departmentInfoServiceImpl")
    private IDepartmentInfoService departmentInfoService;

    @PassToken
    @PostMapping("departments")
    public CommonResult<?> selectDepartmentInfo(
            @RequestBody(required = false) Map<String, Object> params) {
        if (null == params) {
            params = new HashMap<>(0);
        }
        Map<String, Object> p = (Map<String, Object>) params.getOrDefault("p", Collections.emptyMap());
        Integer pageNum = (Integer) params.getOrDefault("pageNum", null);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", null);
        String orderedKey = (String) params.getOrDefault("ordered", null);
        Boolean isDesc = (Boolean) params.getOrDefault("desc", null);
        return departmentInfoService.selectDepartmentInfo(p, pageNum, pageSize, orderedKey, isDesc);
    }

    @PassToken
    @PostMapping("departments/count")
    public CommonResult<?> selectCountDepartmentInfo(
            @RequestBody(required = false) Map<String, Object> params) {
        if (null == params) {
            params = Collections.emptyMap();
        }
        Map<String, Object> p = (Map<String, Object>) params.getOrDefault("p", Collections.emptyMap());
        return departmentInfoService.selectCountDepartmentInfo(p);
    }

    @PostMapping("departments/update")
    public CommonResult<?> updateDepartmentInfo(
            @RequestBody Map<String, Object> params) {
        Map<String, Object> beforeValue = (Map<String, Object>) params.getOrDefault("before", Collections.emptyMap());
        Map<String, Object> afterValue = (Map<String, Object>) params.getOrDefault("after", Collections.emptyMap());
        return departmentInfoService.updateDepartmentInfo(beforeValue, afterValue);
    }

    @PostMapping("departments/insert")
    public CommonResult<?> insertDepartmentInfo(
            @RequestBody List<DepartmentInfo> list) {
        return departmentInfoService.insertDepartmentInfo(list);
    }

    @PostMapping("departments/delete")
    public CommonResult<?> deleteDepartmentInfo(
            @RequestBody Map<String, Object> delete) {
        return departmentInfoService.deleteDepartmentInfo(delete);
    }
}
