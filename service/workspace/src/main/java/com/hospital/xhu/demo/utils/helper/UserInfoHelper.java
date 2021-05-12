package com.hospital.xhu.demo.utils.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hospital.xhu.demo.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
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
        return Collections.singletonMap("id", userId);
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
     * 得到用户手机号的Map
     *
     * @param phone 手机号
     * @return 手机号的Map
     */
    public static Map<String, Object> tempUserPhoneMap(Long phone) {
        return Collections.singletonMap("phone", phone);
    }

    /**
     * 返回上次更新时间为现在的 Map
     *
     * @return 最新的登陆时间
     */
    public static LocalDateTime lastLoginTimeNowMap(Map<String, Object> map) {
        LocalDateTime result = LocalDateTime.now();
        map.put("lastLoginTime", result);
        return result;
    }

    /**
     * 返回登录状态的 Map
     *
     * @param isLogin 登录状态
     * @return 切换后的登陆状态
     */
    public static Boolean userIsLoginMap(Map<String, Object> map, boolean isLogin) {
        map.put("isLogin", isLogin);
        return isLogin;
    }

    /**
     * 创建用户登录的Sign
     *
     * @param map 登录的签名
     */
    public static String userLoginSignMap(Map<String, Object> map) {
        String result = getLoginSign();
        map.put("userLoginSign", result);
        return result;
    }

    /**
     * 将密码和盐组合，形成最终的密码
     *
     * @param password 经过一次MD5加密后的密码
     * @param pwdSalt  密码的盐
     * @return 最终的密码
     */
    public static String getMd5UserPassword(String password, String pwdSalt) {
        String temp = password + pwdSalt;
        String result = DigestUtils.md5DigestAsHex(temp.getBytes());
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
        String result = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
        log.debug("[用户密码盐生成] 生成密码盐 > {}", result);
        return result;
    }

    /**
     * 生成用户登录的签名
     *
     * @return 签名
     */
    public static String getLoginSign() {
        String result = UUID.randomUUID().toString();
        log.debug("[用户签名] 用户登录签名 > {}", result);
        return result;
    }

    /**
     * 生成用户登录的Token
     *
     * @param userInfo 用户信息
     * @return 生成的Token
     */
    public static String getToken(UserInfo userInfo, String sign) {
        // Token的有效时间范围
        ZoneId zoneId = ZoneId.systemDefault();
        // 使用的是最近的登陆时间进行计算
        ZonedDateTime zonedDateTime = userInfo.getLastLoginTime().atZone(zoneId);
        java.util.Date start = Date.from(zonedDateTime.toInstant());
        // 一个小时的有效时间
        long endTime = start.getTime() + 60 * 60 * 1000;
        // 过期时间
        Date end = new Date(endTime);

        log.debug("生成log > [{}, {}] sign: {}", start, end, userInfo.getUserLoginSign());
        // 生成Token
        return JWT.create().withAudience(String.valueOf(userInfo.getId()), userInfo.getUsername())
                .withIssuedAt(start).withExpiresAt(end).sign(Algorithm.HMAC256(sign));
    }
}
