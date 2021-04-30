package com.hospital.xhu.demo.entity;

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
     */
    private String username;
    private String password;
    private String passwordSalt;
    private String email;
    private Long phone;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;
    private String userImageUri;

    /**
     * 对用户信息进行初始化
     * 只能在用户初始化之后进行操作（由客户端注册时传入的不完整的用户信息）
     */
    @Override
    public void init() {
        // 邮箱为空
        if (StringUtils.isEmpty(email)) {
            email = "";
        }
        // 用户电话为空
        if (null == phone) {
            phone = 11111111111L;
        }
        // 用户最后登录的时间为空
        if (null == lastLoginTime) {
            // 默认为当前时间
            lastLoginTime = LocalDateTime.now();
        }
        // 用户头像地址为空
        if (StringUtils.isEmpty(userImageUri)) {
            userImageUri = "";
        }
    }
}
