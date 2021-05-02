package com.hospital.xhu.demo.utils.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * 用户信息相关的工具类
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/18
 */
@Slf4j
public class UserInfoHelper {

    /**
     * 得到用户id的Map
     *
     * @param userId 用户id
     * @return 用户id的Map
     */
    public static Map<String, Object> tempUserIdMap(long userId) {
        return Collections.singletonMap("userId", userId);
    }

    /**
     * 得到用户名的Map
     *
     * @param username 用户名
     * @return 用户名的Map
     */
    public static Map<String, Object> tempUsernameMap(String username) {
        return Collections.singletonMap("username", username);
    }

    /**
     * 返回上次更新时间为现在的 Map
     *
     * @return { "lastLoginTime", now() }
     */
    public static Map<String, Object> lastLoginTimeNowMap(Map<String, Object> map) {
        if (null == map) {
            map = new HashMap<>(1);
        }
        map.put("lastLoginTime", LocalDateTime.now().toString());
        return map;
    }

    /**
     * 返回登录状态的 Map
     *
     * @param isLogin 登录状态
     * @return { "isLogin", "0"/"1" }
     */
    public static Map<String, Object> userIsLoginMap(Map<String, Object> map, boolean isLogin) {
        if (null == map) {
            map = new HashMap<>(1);
        }
        map.put("isLogin", isLogin ? "0" : "1");
        return map;
    }

    /**
     * 将密码和盐组合，形成最终的密码
     *
     * @param password 经过一次MD5加密后的密码
     * @param pwdSalt  密码的盐
     * @return 最终的密码
     */
    public static String getMd5UserPassword(String password, String pwdSalt) {
        String newWord = password + pwdSalt;
        String result = MD5Encoder.encode(newWord.getBytes());
        log.debug("[用户密码解析] md5加密 password:{} pwdSalt:{} >> result:{}", password, pwdSalt, result);
        return result;
    }

    /**
     * 生成随机的盐
     * 随机生成的数字进行MD5加密之后的结果
     *
     * @return 密码盐
     */
    public static String getMd5PassSalt() {
        // 使用uuid生成随机值，再进行MD5加密
        String result = MD5Encoder.encode(UUID.randomUUID().toString().getBytes());
        log.debug("[用户密码盐生成] 生成密码盐 > {}", result);
        return result;
    }
}
