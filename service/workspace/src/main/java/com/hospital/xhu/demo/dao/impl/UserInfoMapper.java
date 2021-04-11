package com.hospital.xhu.demo.dao.impl;

import com.github.pagehelper.PageHelper;
import com.hospital.xhu.demo.dao.IUserInfoMapper;
import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.exception.ExceptionCode;
import com.hospital.xhu.demo.exception.ProjectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 对IUserInfoMapper进行封装，其中的所有的Map所需要的值对应的是数据库中的字段名，不利于使用，此处进行封装
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
@Component
@Slf4j
@Scope("singleton")
public class UserInfoMapper {
    @Resource
    private IUserInfoMapper userInfoMapper;

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
        USER_INFO_PARAM_MAP.put("isLogin", "user_is_login");
        USER_INFO_PARAM_MAP.put("lastLoginTime", "user_last_login_time");
        USER_INFO_PARAM_MAP.put("userImageUri", "user_image_uri");
    }

    /**
     * 将Map中的key从类属性转换为数据库字段名
     * @param map 转换前的Map
     * @return 转换后的Map
     */
    Map<String, String> rebuildMap(Map<String, String> map) {
        Map<String, String> result = new HashMap<>();
        for (String key: map.keySet()) {
            if (USER_INFO_PARAM_MAP.containsKey(key)) {
                result.put(USER_INFO_PARAM_MAP.get(key), map.get(key));
            }
        }
        log.debug("数据转换 >> before:" + map + " after:" + result);

        return result;
    }

    /**
     * 查找符合条件的用户信息列表
     * @param map 查找条件
     * @param page 页码
     * @param pageSize 页大小
     * @return 符合条件的用户列表
     */
    public List<UserInfo> selectUserInfo(Map<String, String> map, Integer page, Integer pageSize) throws ProjectException {
        // 存放转换后的Map
        Map<String, String> newMap = rebuildMap(map);

        // 默认从第一页开始
        if (null == page) {
            page = 0;
        }

        // 默认每页10条数据
        if (null == pageSize) {
            pageSize = 10;
        }

        try {
            // 分页查询
            List<UserInfo> result =
                    PageHelper.startPage(page, pageSize).doSelectPage(()->userInfoMapper.selectUserInfoByMap(newMap));
            log.debug("查询数据库【user_info】 {} [{},{}] >>> {}", map, page, pageSize, result);
            return result;
        } catch (Exception e) {
            // 查询失败抛出项目通用异常
            throw new ProjectException(ExceptionCode.SQLEXCEPTION, "数据库查询失败，请检查传入参数是否正确:" + map);
        }
    }

    /**
     * 插入新的用户的信息
     * @param userInfoList 需要插入的用户的列表
     * @return 插入成功的数量
     * @throws ProjectException 插入失败的异常
     */
    public int insertUserInfo(List<UserInfo> userInfoList) throws ProjectException {
        // TODO 应该检查一下已有的用户列表，防止用户名重复
        try {
            int result = userInfoMapper.insertUserInfo(userInfoList);
            log.debug("插入数据库【user_info】 {} >>> {}", userInfoList, result);
            return result;
        } catch (Exception e) {
            throw new ProjectException(ExceptionCode.SQLEXCEPTION, "数据库插入失败，请检查用户信息是否合法:" + userInfoList);
        }
    }

    /**
     * 更新用户数据
     * @param before 更新的用户条件
     * @param after 修改数据
     * @return 更新的用户数量
     * @throws ProjectException 修改的用户限定为空，或者更新操作报错
     */
    public int updateUserInfo(Map<String, String> before, Map<String, String> after) throws ProjectException {
        Map<String, String> tempBeforeMap = rebuildMap(before);
        Map<String, String> tempAfterMap = rebuildMap(after);

        // 防止一次性修改所有的用户，必须有一定的限制
        if (null == tempBeforeMap || tempBeforeMap.isEmpty()) {
            throw new ProjectException(
                    ExceptionCode.SQLEXCEPTION,
                    "数据库更新用户数据的判断条件不应该为空，请检查输入的数据是否正确:" + before);
        }

        try {
            int result = userInfoMapper.updateUserInfo(before, after);
            log.debug("更新数据库【user_info】 before:{} after:{} >>> {}", before, after, result);
            return result;
        } catch (Exception e) {
            throw new ProjectException(
                    ExceptionCode.SQLEXCEPTION,
                    "数据库更新用户数据失败，请检查用户数据是否合法: before:" + before + "after:" + after);
        }
    }

    /**
     * 删除用户信息
     * @param map 删除的条件
     * @return 删除的数量
     * @throws ProjectException 删除的条件为空，或者删除失败
     */
    public int deleteUserInfo(Map<String, String> map) throws ProjectException {
        Map<String, String> tempDeleteMap = rebuildMap(map);

        if (null == tempDeleteMap || tempDeleteMap.isEmpty()) {
            throw new ProjectException(
                    ExceptionCode.SQLEXCEPTION,
                    "数据库删除用户的判断条件不能为空，检查输入的数据是否正确:" + map);
        }

        try {
            int result = userInfoMapper.deleteUserInfo(map);
            log.debug("删除数据库【user_info】 {} >>> {}", map, result);
            return result;
        } catch (Exception e) {
            throw new ProjectException(
                    ExceptionCode.SQLEXCEPTION,
                    "数据库删除用户操作失败，请检查删除条件是否合法: " + map);
        }
    }
}
