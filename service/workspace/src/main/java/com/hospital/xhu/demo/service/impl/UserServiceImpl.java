package com.hospital.xhu.demo.service.impl;

import com.hospital.xhu.demo.dao.impl.UserInfoMapperImpl;
import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.service.IUserService;
import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.helper.UserInfoHelper;
import com.hospital.xhu.demo.utils.resultcode.CommonCode;
import com.hospital.xhu.demo.utils.resultcode.CommonServiceMsg;
import com.hospital.xhu.demo.utils.resultcode.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/18
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements IUserService {

    private static final String CLASS_INFO_NAME = "用户 user_info";
    private final UserInfoMapperImpl userInfoMapper;

    public UserServiceImpl(@Qualifier("userInfoMapperImpl") UserInfoMapperImpl userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    /**
     * 验证用户登录信息
     * 目前仅支持使用用户名和密码进行登录
     *
     * @param username 用户名
     * @param password 经过MD5加密后的密码
     * @return 登录结果
     * - 成功
     * { code: 200, msg: "登录成功", data: UserInfo }
     * - 失败
     * { code: ExceptionCode, msg: 登录失败提示信息, data: null }
     */
    @Override
    public CommonResult<Object> login(String username, String password) {
        // 传入的值都不能是空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return new CommonResult<>(
                    ExceptionCode.USER_INFO.getCode(), "登录的信息不能为空");
        }

        try {
            Map<String, Object> usernameMap = UserInfoHelper.tempUsernameMap(username);
            // 根据用户名查询用户信息
            UserInfo userInfo = userInfoMapper.selectPrimary(usernameMap);
            // 查询的结果为空
            if (null == userInfo) {
                return new CommonResult<>(
                        ExceptionCode.USER_INFO.getCode(), "没有查询到的对应的用户，请检查用户名是否正确");
            }
            // 获取用户密码加密的盐
            String pwdSalt = userInfo.getPasswordSalt();
            // 防止数据异常
            if (StringUtils.isEmpty(pwdSalt)) {
                log.error("没有正确获取到用户的密码盐信息，请检查数据库数据是否正确:" + pwdSalt);
                return new CommonResult<>(ExceptionCode.DATA_EXCEPTION.getCode(), "用户数据正常，请通知管理员处理");
            }
            // 合成用户输入密码加密后的结果
            String tempUserPassword = UserInfoHelper.getMd5UserPassword(password, pwdSalt);
            // 密码一致
            if (StringUtils.isEmpty(tempUserPassword) && tempUserPassword.equals(userInfo.getPassword())) {
                // 更新登录状态
                int result = updateLoginUserInfo(userInfo);
                if (result > 0) {
                    // 返回登录成功信息
                    return new CommonResult<>(CommonCode.SUCCESS.getCode(), "登录成功", userInfo);
                } else {
                    return new CommonResult<>(ExceptionCode.USER_INFO.getCode(), "用户信息更新失败");
                }
            } else {  // 密码不一致或者密码加密失败
                return new CommonResult<>(ExceptionCode.USER_INFO.getCode(), "用户名或密码错误，请重试");
            }
        } catch (ProjectException e) {  // 抛出异常
            return e.getResult();
        }
    }

    /**
     * 用户注销的方法
     *
     * @param username 用户名
     * @return 注销的结果
     * - 成功
     * { code: 200, msg: "注销成功", data: UserInfo }
     * - 失败
     * { code: ExceptionCode, msg: 注册失败信息, data: null }
     */
    @Override
    public CommonResult<Object> logout(String username) {
        if (StringUtils.isEmpty(username)) {
            return new CommonResult<>(ExceptionCode.USER_INFO.getCode(), "注销的用户信息不能为空");
        }

        try {
            // 获取用户信息
            UserInfo userInfo = userInfoMapper.selectPrimary(UserInfoHelper.tempUsernameMap(username));
            // 更新用户信息
            int result = updateLoginUserInfo(userInfo);
            if (result > 0) {
                return new CommonResult<>(CommonCode.SUCCESS.getCode(), "用户注销成功", userInfo);
            } else {
                return new CommonResult<>(ExceptionCode.USER_INFO.getCode(), "更新用户信息失败:" + userInfo);
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 用户注册方法
     *
     * @param userInfo 用户信息 其中的用户密码必须经过一次 MD5 加密
     * @return 注册的结果
     * - 成功
     * { code: 200, msg: "注册成功", data: userInfo }
     * - 失败
     * { code: ExceptionCode, msg: 注册失败信息, data: null }
     */
    @Override
    public CommonResult<Object> register(UserInfo userInfo) {
        // 检查必要的用户信息
        if (null == userInfo || StringUtils.isEmpty(userInfo.getUsername())
                || StringUtils.isEmpty(userInfo.getPassword())) {
            return new CommonResult<>(ExceptionCode.USER_INFO.getCode(), "用户注册失败，缺少必要的用户信息:" + userInfo);
        }

        // 对应用户信息进行初始化
        userInfo.init();

        try {
            // 获取密码的盐
            String md5PassSalt = UserInfoHelper.getMd5PassSalt();
            // 使用盐生成最终存储数据库的密码
            String md5UserPassword = UserInfoHelper.getMd5UserPassword(userInfo.getPassword(), md5PassSalt);
            userInfo.setPassword(md5UserPassword);
            // 尝试插入用户信息
            int result = userInfoMapper.insert(Collections.singletonList(userInfo));
            if (result > 0) {
                return new CommonResult<>(CommonCode.SUCCESS.getCode(), "注册成功", userInfo);
            } else {
                return new CommonResult<>(ExceptionCode.USER_INFO.getCode(), "已经存在相同的用户名，请重试");
            }
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询用户信息
     *
     * @param map        Optional 查询条件
     * @param orderedKey Optional 排序的字段
     * @param isDesc     Optional 是否反向
     * @param pageNum    Optional 页码
     * @param pageSize   Optional 页大小
     * @return 符合要求的用户信息列表
     * - 成功
     * { code: 200, msg: 查询成功, data: List<userInfo> }
     * - 失败
     * { code: ExceptionCode, msg: 查询失败信息, data: null }
     */
    @Override
    public CommonResult<Object> selectUserInfo(
            Map<String, Object> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc) {
        try {
            List<UserInfo> result =
                    userInfoMapper.select(map, orderedKey, isDesc, pageNum, pageSize);
            String msg =
                    CommonServiceMsg.SELECT_SUCCESS.getMsg(CLASS_INFO_NAME, map, orderedKey, isDesc, pageNum, pageSize);
            return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, result);
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 更新用户信息
     *
     * @param selectKey   查询用户需要更新的值
     * @param newValueMap 修改的值
     * @return 更新成功的数量
     * - 成功
     * { code: 200, msg: 更新成功, data: 更新成功的数量 }
     * - 失败
     * { code: ExceptionCode, msg: 更新失败信息, data: null }
     */
    @Override
    public CommonResult<Object> updateUserInfo(Map<String, Object> selectKey, Map<String, Object> newValueMap) {
        if (CollectionUtils.isEmpty(selectKey) || CollectionUtils.isEmpty(newValueMap)) {
            return new CommonResult<>(
                    ExceptionCode.USER_INFO.getCode(),
                    CommonServiceMsg.UPDATE_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME, selectKey, newValueMap));
        }

        try {
            int size = userInfoMapper.update(selectKey, newValueMap);
            if (size > 0) {
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(), CommonServiceMsg.UPDATE_SUCCESS.getMsg(CLASS_INFO_NAME), size);

            } else {
                return new CommonResult<>(
                        ExceptionCode.USER_INFO.getCode(),
                        CommonServiceMsg.UPDATE_FAILED.getMsg(CLASS_INFO_NAME, selectKey, newValueMap));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 插入新的用户列表
     *
     * @param userInfos 用户数据列表
     * @return 插入结果
     * - 成功
     * { code: 200, msg: 添加成功, data: 添加成功的数量 }
     * - 失败
     * { code: ExceptionCode, msg: 添加失败信息, data: null }
     */
    @Override
    public CommonResult<Object> insertUserInfo(List<UserInfo> userInfos) {
        if (CollectionUtils.isEmpty(userInfos)) {
            return new CommonResult<>(
                    ExceptionCode.USER_INFO.getCode(),
                    CommonServiceMsg.INSERT_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME));
        }

        try {
            // 初始化所有的用户信息
            for (UserInfo userInfo : userInfos) {
                userInfo.init();
            }
            int result = userInfoMapper.insert(userInfos);
            if (result == userInfos.size()) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("count", result);
                map.put("result", userInfos);
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(),
                        CommonServiceMsg.INSERT_SUCCESS.getMsg(CLASS_INFO_NAME),
                        map);
            } else {
                return new CommonResult<>(
                        ExceptionCode.USER_INFO.getCode(),
                        CommonServiceMsg.INSERT_FAILED.getMsg(CLASS_INFO_NAME, userInfos));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 删除用户的数据
     *
     * @param deleteKey 需要删除的用户数据
     * @return 删除的结果
     * - 成功
     * { code: 200, msg: 删除成功, data: 删除成功的数量 }
     * - 失败
     * { code: ExceptionCode, msg: 删除失败信息, data: null }
     */
    @Override
    public CommonResult<Object> deleteUserInfo(Map<String, Object> deleteKey) {
        if (CollectionUtils.isEmpty(deleteKey)) {
            return new CommonResult<>(
                    ExceptionCode.USER_INFO.getCode(),
                    CommonServiceMsg.DELETE_MISSING_REQUIRED_INFO.getMsg(CLASS_INFO_NAME));
        }

        try {
            int result = userInfoMapper.delete(deleteKey);
            if (result > 0) {
                return new CommonResult<>(
                        CommonCode.SUCCESS.getCode(),
                        CommonServiceMsg.DELETE_SUCCESS.getMsg(CLASS_INFO_NAME),
                        result);
            } else {
                return new CommonResult<>(
                        ExceptionCode.USER_INFO.getCode(),
                        CommonServiceMsg.DELETE_FAILED.getMsg(CLASS_INFO_NAME, deleteKey));
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 更新用户信息的方法
     * 在登录成功之后调用，更新用户的登录状态以及上一次登录的时间
     *
     * @param userInfo 登录的用户信息
     */
    private int updateLoginUserInfo(UserInfo userInfo) throws ProjectException {
        // 需要修改的Map
        Map<String, Object> tempUserInfo = new HashMap<>(2);
        // 如果已经登录变为没有登录，如果没有登录变为已经登录
        UserInfoHelper.lastLoginTimeNowMap(tempUserInfo);

        // 更新用户信息
        return userInfoMapper.update(UserInfoHelper.tempUsernameMap(userInfo.getUsername()), tempUserInfo);
    }
}

