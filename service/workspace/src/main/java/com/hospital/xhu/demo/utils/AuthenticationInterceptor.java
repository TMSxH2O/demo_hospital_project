package com.hospital.xhu.demo.utils;

import com.hospital.xhu.demo.dao.impl.UserInfoMapperImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String token = request.getHeader("token");
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
//            throw new ProjectException(ExceptionCode.AUTHENTICATION_ERROR, "Token解析错误，请重新登录");
//        } catch (NumberFormatException e) {
//            log.warn("用户id转换异常 > {}", JWT.decode(token).getAudience());
//            throw new ProjectException(ExceptionCode.AUTHENTICATION_ERROR, "Token解析数据异常，请重新登录");
//        } catch (JWTVerificationException e) {
//            log.debug("登录失效记录 > {}", token);
//            throw new ProjectException(ExceptionCode.AUTHENTICATION_ERROR, "登录状态已失效，请重新登录");
//        }
        return true;
    }
}
