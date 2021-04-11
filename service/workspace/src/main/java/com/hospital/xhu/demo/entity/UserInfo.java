package com.hospital.xhu.demo.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class UserInfo {
    /**
     * username         用户名，不可重复，登录时使用（担任id的功能）
     * password         用户密码，MD5加密，登录时使用
     * passwordSalt     用户密码加密过程中产生的盐，用于密码校验
     * email            用户的邮箱，可以用于找回密码（可能不做这个功能）
     * phone            用户的手机号，可以用于找回密码（可能不做这个功能）
     * isLogin          用户是否登录，0表示未登录，1表示已登录
     * lastLoginTime    用户最后登录时间（可能不需要）
     * userImageUri     用户头像图片路径（相对路径）
     */
    private String username;
    private String password;
    private String passwordSalt;
    private String email;
    private Long phone;
    private Boolean isLogin;
    private LocalDateTime lastLoginTime;
    private String userImageUri;
}
