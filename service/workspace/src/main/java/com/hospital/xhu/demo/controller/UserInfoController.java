package com.hospital.xhu.demo.controller;

import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.service.IUserService;
import com.hospital.xhu.demo.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
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

    @PostMapping("login")
    public CommonResult<Object> userLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        return userService.login(username, password);
    }

    @PostMapping("logout")
    public CommonResult<Object> userLogout(
            @RequestParam("username") String username) {
        return userService.logout(username);
    }

    @PostMapping("register")
    public CommonResult<Object> userRegister(
            @RequestBody UserInfo userInfo) {
        return userService.register(userInfo);
    }

    @GetMapping("users")
    public CommonResult<Object> selectUserInfo(
            @RequestBody Map<String, Object> params) {
        Map<String, Object> p = (Map<String, Object>) params.getOrDefault("p", Collections.emptyMap());
        Integer pageNum = (Integer) params.getOrDefault("pageNum", null);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", null);
        String orderedKey = (String) params.getOrDefault("ordered", null);
        Boolean isDesc = (Boolean) params.getOrDefault("desc", null);
        return userService.selectUserInfo(p, pageNum, pageSize, orderedKey, isDesc);
    }

    @PostMapping("users/update")
    public CommonResult<Object> updateUserInfo(
            @RequestBody Map<String, Object> params) {
        Map<String, Object> beforeValue = (Map<String, Object>) params.getOrDefault("before", Collections.emptyMap());
        Map<String, Object> afterValue = (Map<String, Object>) params.getOrDefault("after", Collections.emptyMap());
        return userService.updateUserInfo(beforeValue, afterValue);
    }

    @PostMapping("users/insert")
    public CommonResult<Object> insertUserInfo(
            @RequestBody List<UserInfo> userInfos) {
        return userService.insertUserInfo(userInfos);
    }

    @PostMapping("users/delete")
    public CommonResult<Object> deleteUserInfo(
            @RequestBody Map<String, Object> delete) {
        return userService.deleteUserInfo(delete);
    }
}
