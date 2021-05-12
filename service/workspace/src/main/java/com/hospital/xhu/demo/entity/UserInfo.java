package com.hospital.xhu.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.helper.UserInfoHelper;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo implements Entity {
    /**
     * username         用户名，不可重复，登录时使用（担任id的功能）
     * password         用户密码，MD5加密，登录时使用
     * passwordSalt     用户密码加密过程中产生的盐，用于密码校验
     * email            用户的邮箱，可以用于找回密码（可能不做这个功能）
     * phone            用户的手机号，可以用于找回密码（可能不做这个功能）
     * lastLoginTime    用户最后登录时间（可能不需要）
     * userImageUri     用户头像图片路径（相对路径）
     * userLoginSign    用户登录的签名，用于单点登录
     */
    private Long id;
    private String username;
    private String password;
    private String passwordSalt;
    private String email;
    private Long phone;
    private Boolean isLogin;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;
    private String userImageUri;
    @JsonIgnore
    private String userLoginSign;

    /**
     * 对用户信息进行初始化
     * 只能在用户初始化之后进行操作（由客户端注册时传入的不完整的用户信息）
     */
    @Override
    public void init() throws ProjectException {
        if (StringUtils.isEmpty(username)) {
            throw new ProjectException(ExceptionCode.USER_INFO, "用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            throw new ProjectException(ExceptionCode.USER_INFO, "用户密码不能为空");
        }
        if (StringUtils.isEmpty(passwordSalt)) {
            // 生成密码盐
            passwordSalt = UserInfoHelper.getMd5PassSalt();
        }
        // 生成MD5加密后的密码
        password = UserInfoHelper.getMd5UserPassword(password, passwordSalt);
        // 邮箱为空
        if (null == email) {
            email = "";
        }
        // 用户电话为空
        if (null == phone) {
            throw new ProjectException(ExceptionCode.USER_INFO, "用户手机号不能为空");
        }
        // 用户登录
        if (null == isLogin) {
            isLogin = false;
        }
        // 用户最后登录的时间为空
        if (null == lastLoginTime) {
            // 默认为当前时间
            lastLoginTime = LocalDateTime.now();
        }
        // 用户头像地址为空
        if (null == userImageUri) {
            userImageUri = "";
        }
        // 分配用户登录签名
        if (null == userLoginSign) {
            userLoginSign = UserInfoHelper.getLoginSign();
        }
    }
}
