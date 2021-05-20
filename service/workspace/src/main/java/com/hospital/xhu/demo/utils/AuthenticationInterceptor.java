package com.hospital.xhu.demo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hospital.xhu.demo.dao.impl.UserInfoMapperImpl;
import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.exception.AuthenticationException;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.annotation.PassToken;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import com.hospital.xhu.demo.utils.helper.UserInfoHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/4
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Resource(name = "userInfoMapperImpl")
    private UserInfoMapperImpl userInfoMapper;

    private static final int ADMIN_TOKEN_SIZE = 3;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Cookie[] cookies = request.getCookies();
//        String token = null;
//        if (null != cookies) {
//            for (Cookie cookie: cookies) {
//                if ("token".equals(cookie.getName())) {
//                    Cookie c = new Cookie("token", cookie.getValue());
//                    c.setMaxAge(10 * 60 * 1000);
//                    response.addCookie(c);
//                    token = cookie.getValue();
//                    break;
//                }
//            }
//        }
//        log.debug("解析Token > {}", token);
//        // 如果不是映射到方法直接通过
//        if (!(handler instanceof HandlerMethod)) {
//            return true;
//        }
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        Method method = handlerMethod.getMethod();
//        log.debug("权限校验 > {}", method.getName());
//        // 方法上是否有 PassToken
//        if (method.isAnnotationPresent(PassToken.class)) {
//            PassToken pass = method.getAnnotation(PassToken.class);
//            if (pass.required()) {
//                return true;
//            }
//        }
//        if (token == null) {
//            throw new ProjectException(ExceptionCode.AUTHENTICATION_ERROR, "缺少对应的Token，请重新登录");
//        }
//        try {
//            List<String> audience = JWT.decode(token).getAudience();
//            if (ADMIN_TOKEN_SIZE == audience.size()) {
//                return true;
//            }
//            // 用户id
//            String sUserId = audience.get(0);
//            // 用户名
//            String username = audience.get(1);
//            long userId = Long.parseLong(sUserId);
//            UserInfo userInfo = userInfoMapper.selectPrimary(UserInfoHelper.tempUserIdMap(userId));
//            if (!username.equals(userInfo.getUsername())) {
//                log.warn("Token中解析到的用户名不正确 {} > {}", username, userInfo);
//                throw new ProjectException(ExceptionCode.AUTHENTICATION_ERROR, "Token数据错误，请重新登录");
//            }
//            // 验证sign
//            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(userInfo.getUserLoginSign())).build();
//            jwtVerifier.verify(token);
//            return true;
//        } catch (JWTDecodeException e) {
//            throw new AuthenticationException("Token解析错误，请重新登录");
//        } catch (NumberFormatException e) {
//            log.warn("用户id转换异常 > {}", JWT.decode(token).getAudience());
//            throw new AuthenticationException("Token解析数据异常，请重新登录");
//        } catch (JWTVerificationException e) {
//            log.debug("登录失效记录 > {}", token);
//            throw new AuthenticationException("登录状态已失效，请重新登录");
//        }
        return true;
    }
}
