package com.hospital.xhu.demo.service;

import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.utils.CommonResult;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
@Service
public interface IUserService {
    /**
     * 用户登录方法
     * @param username 用户名
     * @param password 经过MD5加密后的密码
     * @return 登录的返回值
     */
    public CommonResult<UserInfo> login(String username, String password);
}
