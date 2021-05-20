package com.hospital.xhu.demo.controller.entity;

import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.service.IUserService;
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
 * @date 2021/4/11
 */
@RestController
@Slf4j
public class UserInfoController {
    @Resource(name="userService")
    private IUserService userService;

    @PostMapping("users")
    public CommonResult<?> selectUserInfo(
            @RequestBody(required = false) Map<String, Object> params) {
        if (null == params) {
            params = new HashMap<>(0);
        }
        Map<String, Object> p = (Map<String, Object>) params.getOrDefault("p", Collections.emptyMap());
        Integer pageNum = (Integer) params.getOrDefault("pageNum", null);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", null);
        String orderedKey = (String) params.getOrDefault("ordered", null);
        Boolean isDesc = (Boolean) params.getOrDefault("desc", null);
        return userService.selectUserInfo(p, pageNum, pageSize, orderedKey, isDesc);
    }

    @PostMapping("users/count")
    public CommonResult<?> selectCountUserInfo(
            @RequestBody(required = false) Map<String, Object> params) {
        if (null == params) {
            params = Collections.emptyMap();
        }
        Map<String, Object> p = (Map<String, Object>) params.getOrDefault("p", Collections.emptyMap());
        return userService.selectCountUserInfo(p);
    }

    @PostMapping("users/update")
    public CommonResult<?> updateUserInfo(
            @RequestBody Map<String, Object> params) {
        Map<String, Object> beforeValue = (Map<String, Object>) params.getOrDefault("before", Collections.emptyMap());
        Map<String, Object> afterValue = (Map<String, Object>) params.getOrDefault("after", Collections.emptyMap());
        return userService.updateUserInfo(beforeValue, afterValue);
    }

    @PostMapping("users/insert")
    public CommonResult<?> insertUserInfo(
            @RequestBody List<UserInfo> userInfos) {
        return userService.insertUserInfo(userInfos);
    }

    @PostMapping("users/delete")
    public CommonResult<?> deleteUserInfo(
            @RequestBody Map<String, Object> delete) {
        return userService.deleteUserInfo(delete);
    }
}
