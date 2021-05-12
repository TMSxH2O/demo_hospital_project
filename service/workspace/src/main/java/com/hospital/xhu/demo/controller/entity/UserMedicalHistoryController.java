package com.hospital.xhu.demo.controller.entity;

import com.hospital.xhu.demo.entity.UserMedicalHistory;
import com.hospital.xhu.demo.service.IUserMedicalHistoryService;
import com.hospital.xhu.demo.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2021/5/3
 */
@RestController
@Slf4j
public class UserMedicalHistoryController {
    @Resource(name = "userMedicalHistoryServiceImpl")
    private IUserMedicalHistoryService userMedicalHistoryService;

    @PostMapping("medicals")
    public CommonResult<?> selectUserMedicalHistory(
            @RequestBody(required = false) Map<String, Object> params) {
        if (null == params) {
            params = new HashMap<>(0);
        }
        Map<String, Object> p = (Map<String, Object>) params.getOrDefault("p", Collections.emptyMap());
        Integer pageNum = (Integer) params.getOrDefault("pageNum", null);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", null);
        String orderedKey = (String) params.getOrDefault("ordered", null);
        Boolean isDesc = (Boolean) params.getOrDefault("desc", null);
        return userMedicalHistoryService.selectUserMedicalHistory(p, pageNum, pageSize, orderedKey, isDesc);
    }

    @PostMapping("medicals/update")
    public CommonResult<?> updateUserMedicalHistory(
            @RequestBody Map<String, Object> params) {
        Map<String, Object> beforeValue = (Map<String, Object>) params.getOrDefault("before", Collections.emptyMap());
        Map<String, Object> afterValue = (Map<String, Object>) params.getOrDefault("after", Collections.emptyMap());
        return userMedicalHistoryService.updateUserMedicalHistory(beforeValue, afterValue);
    }

    @PostMapping("medicals/insert")
    public CommonResult<?> insertUserMedicalHistory(
            @RequestBody List<UserMedicalHistory> list) {
        return userMedicalHistoryService.insertUserMedicalHistory(list);
    }

    @PostMapping("medicals/delete")
    public CommonResult<?> deleteUserMedicalHistory(
            @RequestBody Map<String, Object> delete) {
        return userMedicalHistoryService.deleteUserMedicalHistory(delete);
    }
}
