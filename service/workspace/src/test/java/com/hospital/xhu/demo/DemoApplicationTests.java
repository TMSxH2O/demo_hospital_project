package com.hospital.xhu.demo;

import com.hospital.xhu.demo.dao.IUserInfoMapper;
import com.hospital.xhu.demo.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

    @Resource
    public IUserInfoMapper userInfoMapper;

    @Test
    void testUserInfoSelect() {
        Map<String, String> test = new HashMap<>();
        test.put("user_pwd_salt", "456");
        // 时间可以直接利用 LocalDateTime 的 toString 来得到
        LocalDateTime testTime = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
        test.put("user_last_login_time", testTime.toString());
        System.out.println(userInfoMapper.selectUserInfoByMap(null, "username", null));
    }

    @Test
    void testUserInfoInsert() {
        UserInfo userInfo = new UserInfo("test2", "123", "123", "", 0L, false, LocalDateTime.now(), "");
        System.out.println(userInfoMapper.insertUserInfo(Collections.singletonList(userInfo)));
    }

    @Test
    void testUserInfoUpdate() {
        Map<String, String> before = new HashMap<>();
        Map<String, String> after = new HashMap<>();
        before.put("user_pwd", "123");
//        after.put("user_pwd", "123456");
        after.put("user_pwd_salt", "123456");
        System.out.println(userInfoMapper.updateUserInfo(before, after));
    }

    @Test
    void testUserInfoDelete() {
        Map<String, String> test = new HashMap<>();
        test.put("username", "test2");
        System.out.println(userInfoMapper.deleteUserInfo(test));
    }
}
