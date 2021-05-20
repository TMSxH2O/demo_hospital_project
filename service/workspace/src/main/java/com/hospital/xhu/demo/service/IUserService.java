package com.hospital.xhu.demo.service;

import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.utils.CommonResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
     * @param phone    手机号
     * @param password 登录密码
     * @param response 响应
     * @return 登录的返回值
     */
    CommonResult<?> login(Long phone, String password, HttpServletResponse response);

    /**
     * 用户注销方法
     *
     * @param phone   手机号
     * @param request 请求
     * @return 注销的结果
     */
    CommonResult<?> logout(Long phone, HttpServletResponse request);

    /**
     * 用户注册
     *
     * @param userInfo 用户信息
     * @return 注册的结果
     */
    CommonResult<?> register(UserInfo userInfo);

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
    CommonResult<?> selectUserInfo(
            Map<String, Object> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc);

    /**
     * 查询用户信息数量
     *
     * @param map 查询条件
     * @return 符合条件的数据数量
     */
    CommonResult<?> selectCountUserInfo(
            Map<String, Object> map);

    /**
     * 更新用户的数据
     *
     * @param selectKey   查询用户需要更新的值
     * @param newValueMap 修改的值
     * @return 修改的结果
     */
    CommonResult<?> updateUserInfo(Map<String, Object> selectKey, Map<String, Object> newValueMap);

    /**
     * 插入新的用户数据
     *
     * @param userInfo 用户数据列表
     * @return 插入的结果
     */
    CommonResult<?> insertUserInfo(List<UserInfo> userInfo);

    /**
     * 删除用户的数据
     *
     * @param deleteKey 需要删除的用户数据
     * @return 删除的结果
     */
    CommonResult<?> deleteUserInfo(Map<String, Object> deleteKey);
}
