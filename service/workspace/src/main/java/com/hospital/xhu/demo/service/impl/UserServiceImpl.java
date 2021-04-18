package com.hospital.xhu.demo.service.impl;

import com.hospital.xhu.demo.dao.impl.UserInfoMapper;
import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.service.IUserService;
import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.UserInfoUtils;
import com.hospital.xhu.demo.utils.resultcode.CommonCode;
import com.hospital.xhu.demo.utils.resultcode.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * TODO 之后改成以缓存为主，优先修改 IEntityCache 中的值
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/18
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    final UserInfoMapper userInfoMapper;

    public UserServiceImpl(UserInfoMapper userInfoMapper) {
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
                    ExceptionCode.USERINFO.getCode(), "登录的信息不能为空");
        }

        try {
            Map<String, String> usernameMap = UserInfoUtils.tempUsernameMap(username);
            // 根据用户名查询用户信息
            UserInfo userInfo = userInfoMapper.selectUserInfoPrimary(usernameMap);
            // 查询的结果为空
            if (null == userInfo) {
                return new CommonResult<>(
                        ExceptionCode.USERINFO.getCode(), "没有查询到的对应的用户，请检查用户名是否正确");
            }
            if (userInfo.getIsLogin()) {
                return new CommonResult<>(ExceptionCode.USERINFO.getCode(), "该用户正在登录中，请勿重复登录");
            }
            // 获取用户密码加密的盐
            String pwdSalt = userInfo.getPasswordSalt();
            // 防止数据异常
            if (StringUtils.isEmpty(pwdSalt)) {
                log.error("没有正确获取到用户的密码盐信息，请检查数据库数据是否正确:" + pwdSalt);
                return new CommonResult<>(ExceptionCode.DATAEXCEPTION.getCode(), "用户数据正常，请通知管理员处理");
            }
            // 合成用户输入密码加密后的结果
            String tempUserPassword = UserInfoUtils.getMd5UserPassword(password, pwdSalt);
            // 密码一致
            if (StringUtils.isEmpty(tempUserPassword) && tempUserPassword.equals(userInfo.getPassword())) {
                // 更新登录状态
                int result = updateLoginUserInfo(userInfo);
                if (result > 0) {
                    // 返回登录成功信息
                    return new CommonResult<>(CommonCode.SUCCESS.getCode(), "登录成功", userInfo);
                } else {
                    return new CommonResult<>(ExceptionCode.USERINFO.getCode(), "用户信息更新失败");
                }
            } else {  // 密码不一致或者密码加密失败
                return new CommonResult<>(ExceptionCode.USERINFO.getCode(), "用户名或密码错误，请重试");
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
            return new CommonResult<>(ExceptionCode.USERINFO.getCode(), "注销的用户信息不能为空");
        }

        try {
            // 获取用户信息
            UserInfo userInfo = userInfoMapper.selectUserInfoPrimary(UserInfoUtils.tempUsernameMap(username));
            // 校验是否在登录状态
            if (!userInfo.getIsLogin()) {
                return new CommonResult<>(ExceptionCode.USERINFO.getCode(), "用户已经注销，请勿重复操作");
            }
            // 更新用户信息
            int result = updateLoginUserInfo(userInfo);
            if (result > 0) {
                return new CommonResult<>(CommonCode.SUCCESS.getCode(), "用户注销成功", userInfo);
            } else {
                return new CommonResult<>(ExceptionCode.USERINFO.getCode(), "更新用户信息失败:" + userInfo);
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
            return new CommonResult<>(ExceptionCode.USERINFO.getCode(), "用户注册失败，缺少必要的用户信息:" + userInfo);
        }

        // 对应用户信息进行初始化
        userInfo.initUserInfo();

        try {
            // 尝试插入用户信息
            int result = userInfoMapper.insertUserInfo(Collections.singletonList(userInfo));
            if (result > 0) {
                return new CommonResult<>(CommonCode.SUCCESS.getCode(), "注册成功", userInfo);
            } else {
                return new CommonResult<>(ExceptionCode.USERINFO.getCode(), "已经存在相同的用户名，请重试");
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
     */
    @Override
    public CommonResult<Object> selectUserInfo(
            Map<String, String> map, Integer pageNum, Integer pageSize,
            String orderedKey, Boolean isDesc) {
        try {
            List<UserInfo> userInfos = userInfoMapper.selectUserInfo(map, orderedKey, isDesc, pageNum, pageSize);
            String msg =
                    "查询用户信息成功 map:" + map + " orderedKey:" + orderedKey + " isDesc:" + isDesc + " pageNum:"
                            + pageNum + " pageSize:" + pageSize;
            return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, userInfos);
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * TODO 更新用户信息
     *
     * @param selectKey   查询用户需要更新的值
     * @param newValueMap 修改的值
     * @return 更新成功的数量
     */
    @Override
    public CommonResult<Object> updateUserInfo(Map<String, String> selectKey, Map<String, String> newValueMap) {
        if (null == selectKey || null == newValueMap || selectKey.isEmpty() || newValueMap.isEmpty()) {
            return new CommonResult<>(
                    ExceptionCode.USERINFO.getCode(),
                    "更新用户信息的索引值和修改值都不能为空 select:" + selectKey + " new:" + newValueMap);
        }
        return null;
    }

    /**
     * 更新用户信息的方法
     * 在登录成功之后调用，更新用户的登录状态以及上一次登录的时间
     *
     * @param userInfo 登录的用户信息
     */
    private int updateLoginUserInfo(UserInfo userInfo) throws ProjectException {
        // 用户是否登录
        boolean isLogin = userInfo.getIsLogin();
        // 需要修改的Map
        Map<String, String> tempUserInfo = new HashMap<>(2);
        // 如果已经登录变为没有登录，如果没有登录变为已经登录
        UserInfoUtils.userIsLoginMap(tempUserInfo, isLogin);
        UserInfoUtils.lastLoginTimeNowMap(tempUserInfo);

        // 更新用户信息
        int result = userInfoMapper.updateUserInfo(UserInfoUtils.tempUsernameMap(userInfo.getUsername()), tempUserInfo);

        // 更新用户状态
        userInfo.setIsLogin(!isLogin);

        return result;
    }
}
