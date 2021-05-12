package com.hospital.xhu.demo.controller.entity;

import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.service.IUserService;
import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.annotation.PassToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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

    @PassToken
    @PostMapping("login")
    public CommonResult<?> userLogin(
            @RequestParam("phone") Long phone,
            @RequestParam("password") String password,
            HttpServletResponse response) {
        return userService.login(phone, password, response);
    }

    @PostMapping("logout")
    public CommonResult<?> userLogout(
            @RequestParam("phone") Long phone,
            HttpServletResponse response) {
        return userService.logout(phone, response);
    }

    @PassToken
    @PostMapping("register")
    public CommonResult<?> userRegister(
            @RequestBody UserInfo userInfo) {
        return userService.register(userInfo);
    }

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
