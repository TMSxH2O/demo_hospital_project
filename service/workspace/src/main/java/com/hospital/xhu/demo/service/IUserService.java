package com.hospital.xhu.demo.service;

import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.utils.CommonResult;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
public interface IUserService {
    /**
     * 用户登录方法
     *
     * @param username 用户名
     * @param password 经过MD5加密后的密码
     * @return 登录的返回值
     */
    CommonResult<Object> login(String username, String password);

    /**
     * 用户注销方法
     *
     * @param username 用户名
     * @return 注销的结果
     */
    CommonResult<Object> logout(String username);

    /**
     * 用户注册
     *
     * @param userInfo 用户信息
     * @return 注册的结果
     */
    CommonResult<Object> register(UserInfo userInfo);

    /**
     * 查询用户信息
     *
     * @param map        Optional 查询条件
     * @param orderedKey Optional 排序的字段
     * @param isDesc     Optional 是否反向
     * @param pageNum    Optional 页码
     * @param pageSize   Optional 页大小
     * @return 符合条件的用户信息列表
     */
    CommonResult<Object> selectUserInfo(
            Map<String, String> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc);

    /**
     * 更新用户的数据
     *
     * @param selectKey   查询用户需要更新的值
     * @param newValueMap 修改的值
     * @return 修改的结果
     */
    CommonResult<Object> updateUserInfo(Map<String, String> selectKey, Map<String, String> newValueMap);
}
