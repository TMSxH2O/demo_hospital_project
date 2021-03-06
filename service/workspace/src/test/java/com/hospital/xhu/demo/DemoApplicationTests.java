package com.hospital.xhu.demo;

import com.hospital.xhu.demo.dao.IUserInfoMapper;
import com.hospital.xhu.demo.dao.impl.DepartmentInfoMapperImpl;
import com.hospital.xhu.demo.dao.impl.UserMedicalHistoryMapperImpl;
import com.hospital.xhu.demo.exception.ProjectException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

    @Resource
    public IUserInfoMapper userInfoMapper;
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
    @Test
    void testUserInfoInsert() {
//        UserInfo userInfo = new UserInfo(null, "test", "123", null, null, null, null, LocalDateTime.now(), "");
//        System.out.println(userInfoMapper.insert(Collections.singletonList(userInfo)));
    }
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
    DepartmentInfoMapperImpl departmentInfoMapper;
    @Autowired
    UserMedicalHistoryMapperImpl userMedicalHistoryMapper;

    @Test
    void testMedicalHistory() {
        try {
            System.out.println(
                    userMedicalHistoryMapper.select(Collections.emptyMap(), null, null, null, null));
        } catch (ProjectException e) {
            e.printStackTrace();
        }
    }

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
