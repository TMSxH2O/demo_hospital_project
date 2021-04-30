package com.hospital.xhu.demo;

import com.hospital.xhu.demo.dao.impl.DepartmentInfoMapper;
import com.hospital.xhu.demo.exception.ProjectException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

//    @Resource
//    public IUserInfoMapper userInfoMapper;
//
//    @Test
//    void testUserInfoSelect() {
//        Map<String, String> test = new HashMap<>();
//        test.put("user_pwd_salt", "456");
//        // 时间可以直接利用 LocalDateTime 的 toString 来得到
//        LocalDateTime testTime = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
//        test.put("user_last_login_time", testTime.toString());
//        System.out.println(userInfoMapper.select(null, "username", null));
//    }
//
//    @Test
//    void testUserInfoInsert() {
//        UserInfo userInfo = new UserInfo("test2", "123", "123", "", 0L, LocalDateTime.now(), "");
//        System.out.println(userInfoMapper.insert(Collections.singletonList(userInfo)));
//    }
//
//    @Test
//    void testUserInfoUpdate() {
//        Map<String, Object> before = new HashMap<>();
//        Map<String, Object> after = new HashMap<>();
//        before.put("user_pwd", "123");
////        after.put("user_pwd", "123456");
//        after.put("user_pwd_salt", "123456");
//        System.out.println(userInfoMapper.update(before, after));
//    }
//
//    @Test
//    void testUserInfoDelete() {
//        Map<String, Object> test = new HashMap<>();
//        test.put("username", "test2");
//        System.out.println(userInfoMapper.delete(test));
//    }
//
    @Autowired
    DepartmentInfoMapper departmentInfoMapper;

    @Test
    void testDepartmentMapper() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", 1);
            System.out.println(departmentInfoMapper.select(map, null, null, null, null));
        } catch (ProjectException e) {
            e.printStackTrace();
        }
    }
}
