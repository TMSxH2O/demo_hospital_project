package com.hospital.xhu.demo.controller;

import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.service.IUserService;
import com.hospital.xhu.demo.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @RequestMapping("login")
    public CommonResult<Object> userLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        return userService.login(username, password);
    }

    @RequestMapping("logout")
    public CommonResult<Object> userLogout(
            @RequestParam("username") String username) {
        return userService.logout(username);
    }

    @RequestMapping("register")
    public CommonResult<Object> userRegister(
            @RequestParam("userInfo") UserInfo userInfo) {
        return userService.register(userInfo);
    }

    @RequestMapping("users")
    public CommonResult<Object> selectUserInfo(
            @RequestParam("p") Map<String, Object> map,
            @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize,
            @RequestParam("ordered") String orderedKey, @RequestParam("desc") Boolean isDesc) {
        return userService.selectUserInfo(map, pageNum, pageSize, orderedKey, isDesc);
    }

    @RequestMapping("users/update")
    public CommonResult<Object> updateUserInfo(
            @RequestParam("old") Map<String, Object> oldValue,
            @RequestParam("new") Map<String, Object> newValue) {
        return userService.updateUserInfo(oldValue, newValue);
    }

    @RequestMapping("users/insert")
    public CommonResult<Object> insertUserInfo(
            @RequestParam("p") List<UserInfo> userInfos) {
        return userService.insertUserInfo(userInfos);
    }

    @RequestMapping("users/delete")
    public CommonResult<Object> deleteUserInfo(
            @RequestParam("p") Map<String, Object> delete) {
        return userService.deleteUserInfo(delete);
    }
}
