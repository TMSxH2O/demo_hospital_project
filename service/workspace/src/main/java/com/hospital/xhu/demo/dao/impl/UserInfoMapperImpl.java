package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.IUserInfoMapper;
import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapperImpl;
import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 对IUserInfoMapper进行封装，其中的所有的Map所需要的值对应的是数据库中的字段名，不利于使用，此处进行封装
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
@Repository("userInfoMapperImpl")
@Slf4j
public class UserInfoMapperImpl extends GeneralMapperImpl<UserInfo> {
    /**
     * 存放类属性到数据库字段的映射
     */
    private static final Map<String, String> USER_INFO_MAP = new HashMap<>();

    static {
        USER_INFO_MAP.put("id", "user_id");
        USER_INFO_MAP.put("username", "username");
        USER_INFO_MAP.put("password", "user_pwd");
        USER_INFO_MAP.put("passwordSalt", "user_pwd_salt");
        USER_INFO_MAP.put("email", "user_email");
        USER_INFO_MAP.put("phone", "user_phone");
        USER_INFO_MAP.put("isLogin", "user_is_login");
        USER_INFO_MAP.put("lastLoginTime", "user_last_login_time");
        USER_INFO_MAP.put("userImageUri", "user_image_uri");
        USER_INFO_MAP.put("userLoginSign", "user_login_sign");
    }

    public UserInfoMapperImpl(@Qualifier("userInfoMapper") IUserInfoMapper userInfoMapper) {
        super(userInfoMapper);
    }

    @Override
    protected String getSqlName() {
        return "user_info";
    }

    @Override
    protected Map<String, String> getMap() {
        return USER_INFO_MAP;
    }

    @Override
    protected ExceptionCode getExceptionCode() {
        return ExceptionCode.USER_INFO;
    }
}
