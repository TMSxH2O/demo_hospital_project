package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.IUserInfoMapper;
import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapperImpl;
import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.resultcode.ExceptionCode;
import com.hospital.xhu.demo.utils.resultcode.SqlMsg;
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
public class UserInfoMapperImpl extends GeneralMapperImpl<UserInfo, IGeneralMapper<UserInfo>> {
    /**
     * 存放类属性到数据库字段的映射
     */
    private static final Map<String, String> USER_INFO_PARAM_MAP = new HashMap<>();

    static {
        USER_INFO_PARAM_MAP.put("username", "username");
        USER_INFO_PARAM_MAP.put("password", "user_pwd");
        USER_INFO_PARAM_MAP.put("passwordSalt", "user_pwd_salt");
        USER_INFO_PARAM_MAP.put("email", "user_email");
        USER_INFO_PARAM_MAP.put("phone", "user_phone");
        USER_INFO_PARAM_MAP.put("lastLoginTime", "user_last_login_time");
        USER_INFO_PARAM_MAP.put("userImageUri", "user_image_uri");
    }

    public UserInfoMapperImpl(@Qualifier("userInfoMapper") IUserInfoMapper userInfoMapper) {
        super(userInfoMapper);
    }

    /**
     * 将Map中的key从类属性转换为数据库字段名
     *
     * @param map 转换前的Map
     * @return 转换后的Map
     */
    @Override
    protected Map<String, Object> rebuildMap(Map<String, Object> map) throws ProjectException {
        Map<String, Object> result = new HashMap<>(map.size());
        for (String key : map.keySet()) {
            if (USER_INFO_PARAM_MAP.containsKey(key)) {
                result.put(USER_INFO_PARAM_MAP.get(key), map.get(key));
            }
            else {
                String msg = SqlMsg.REBUILD_KEY_ERROR.getMsg(getSqlName(), key);
                log.warn(msg);
                throw new ProjectException(ExceptionCode.USER_INFO, msg);
            }
        }
        log.debug(SqlMsg.REBUILD_SUCCESS.getMsg(getSqlName(), map, result));

        return result;
    }

    /**
     * 转换某个字符串到数据库字段名
     *
     * @param key 转换前的字符串
     * @return 转换后的字符串
     * @throws ProjectException 转换失败的报错
     */
    @Override
    protected String getMapString(String key) throws ProjectException {
        if (USER_INFO_PARAM_MAP.containsKey(key)) {
            String result = USER_INFO_PARAM_MAP.get(key);
            log.debug(SqlMsg.REBUILD_SUCCESS.getMsg(getSqlName(), key, result));
            return result;
        }
        else {
            String msg = SqlMsg.REBUILD_KEY_ERROR.getMsg(getSqlName(), key);
            log.warn(msg);
            throw new ProjectException(ExceptionCode.USER_INFO, msg);
        }
    }

    @Override
    protected String getSqlName() {
        return "user_info";
    }
}