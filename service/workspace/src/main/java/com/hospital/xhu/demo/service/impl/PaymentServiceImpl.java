package com.hospital.xhu.demo.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.hospital.xhu.demo.dao.impl.UserReservationMapperImpl;
import com.hospital.xhu.demo.entity.UserReservation;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.properties.PaymentProperties;
import com.hospital.xhu.demo.service.IPaymentService;
import com.hospital.xhu.demo.service.IUserReservationService;
import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.enumerate.CommonCode;
import com.hospital.xhu.demo.utils.enumerate.CommonServiceMsg;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import com.hospital.xhu.demo.utils.helper.UserReservationHelper;
import com.hospital.xhu.demo.utils.payment.ReservationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/8
 */
@Service
@Slf4j
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentProperties properties;
    private final AlipayClient alipayClient;
    private final List<String> tradeList;
    private final List<String> refundList;
    @Value("${pay.subjectName}")
    private String subjectName;
    @Resource(name = "userReservationMapperImpl")
    private UserReservationMapperImpl userReservationMapper;
    @Resource(name = "userReservationServiceImpl")
    private IUserReservationService userReservationService;

    public PaymentServiceImpl(
            PaymentProperties properties, AlipayClient alipayClient) {
        this.properties = properties;
        this.alipayClient = alipayClient;
        tradeList = new ArrayList<>();
        refundList = new ArrayList<>();
    }

    /**
     * 响应支付界面表单
     *
     * @param reservationId 预约订单id
     * @param response      响应对象
     * @return 重定向结果
     */
    @Override
    public CommonResult<?> redirectPaymentPage(String reservationId, HttpServletResponse response) {
        if (StringUtils.isEmpty(reservationId)) {
            String msg = CommonServiceMsg.PAYMENT_MISSING_REQUIRED_INFO.getMsg("reservationId: " + reservationId);
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }

        if (tradeList.contains(reservationId)) {
            String msg = CommonServiceMsg.PAYMENT_FAILED.getMsg("请勿重复支付");
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }

        try {
            Map<String, Object> map = UserReservationHelper.tempUserReservationId(reservationId);
            UserReservation userReservation = userReservationMapper.selectPrimary(map);

            // 生成支付模型
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            model.setOutTradeNo(reservationId);
            model.setSubject(subjectName);
            model.setTotalAmount(String.valueOf(userReservation.getReservationPrice()));
            model.setBody(UserReservationHelper.getReservationBody(userReservation));
            model.setProductCode(properties.getProductCode());

            // 支付请求界面
            AlipayTradePagePayRequest pagePayRequest = new AlipayTradePagePayRequest();
            pagePayRequest.setReturnUrl(properties.getReturnUrl());
            pagePayRequest.setNotifyUrl(properties.getNotifyUrl());
            pagePayRequest.setBizModel(model);

            // 生成表单
            String form = alipayClient.pageExecute(pagePayRequest).getBody();
            response.setContentType("text/html;charset=" + properties.getCharset());
            // 将表单界面返回
            try (PrintWriter writer = response.getWriter()) {
                writer.write(form);
                writer.flush();

                // 将订单号入队，防止重复支付
                tradeList.add(reservationId);

                // 支付跳转
                String msg = CommonServiceMsg.PAYMENT_REDIRECT_SUCCESS.getMsg();
                log.debug(msg);
                return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, userReservation);
            } catch (IOException e) {
                String msg = "支付界面，返回表单界面失败 > " + e.getLocalizedMessage();
                log.warn(msg);
                log.error(e.getMessage());
                return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
            }
        } catch (ProjectException e) {
            return e.getResult();
        } catch (AlipayApiException e) {
            String msg = "支付界面，生成表单失败 > " + e.getErrMsg();
            log.warn(msg);
            log.error(e.getMessage());
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }
    }

    /**
     * 同步响应
     *
     *
     * @param request@return 支付的结果
     */
    @Override
    public CommonResult<?> syncNotification(HttpServletRequest request) {
        String tradeNo = request.getParameter("out_trade_no");
        String appId = request.getParameter("app_id");
        if (StringUtils.isEmpty(tradeNo) || StringUtils.isEmpty(appId)) {
            String msg = CommonServiceMsg.PAYMENT_MISSING_REQUIRED_INFO.getMsg(
                    String.format("tradeNo: %s appId: %s", tradeNo, appId));
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }

        if (!tradeList.contains(tradeNo) || !properties.getAppId().equals(appId)) {
            String msg = CommonServiceMsg.PAYMENT_FAILED.getMsg("订单号异常，请注意异常网络攻击");
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }

        // 尝试将对应的订单修改为已支付
        Map<String, Object> userReservationIdMap = UserReservationHelper.tempUserReservationId(tradeNo);
        UserReservationHelper.tempUserReservationStatusMap(userReservationIdMap, ReservationStatus.UNPAID.get());

        // 修改为已支付
        Map<String, Object> newReservationMap =
                UserReservationHelper.tempUserReservationStatusMap(null, ReservationStatus.PAID.get());

        CommonResult<?> result = userReservationService.updateUserReservation(userReservationIdMap, newReservationMap);

        if (CommonCode.SUCCESS.getCode() == result.getCode()) {
            // 修改成功的结果
            Map<String, String> map = new HashMap<>(2);
            map.put("out_trade_no", tradeNo);
            map.put("app_id", appId);
            result = new CommonResult<>(CommonCode.SUCCESS.getCode(), "支付订单更新成功", map);
        }

        // 不管支付成功或是失败，都将其移出正在支付列表
        tradeList.remove(tradeNo);
        return result;
    }

    @Override
    public CommonResult<?> asyncNotification(HttpServletRequest request) {
        return null;
    }

    /**
     * 退款接口
     *
     * @param tradeNo 订单号
     * @return 退款的结果
     */
    @Override
    public CommonResult<?> refund(String tradeNo) {
        if (StringUtils.isEmpty(tradeNo)) {
            String msg = CommonServiceMsg.PAYMENT_MISSING_REQUIRED_INFO.getMsg(
                    String.format("tradeNo: %s", tradeNo));
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }

        if (refundList.contains(tradeNo)) {
            String msg = CommonServiceMsg.PAYMENT_FAILED.getMsg("请勿重复退款");
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }

        // 将退款订单加入正在退款列表
        refundList.add(tradeNo);

        try {
            // 用于查询订单
            Map<String, Object> userReservationIdMap = UserReservationHelper.tempUserReservationId(tradeNo);
            UserReservation userReservation = userReservationMapper.selectPrimary(userReservationIdMap);

            if (ReservationStatus.PAID.get() != userReservation.getReservationStatus()) {
                String msg = "退款失败，当前订单状态异常 > " + userReservation;
                log.warn(msg);
                return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
            }

            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();

            // 设置退款的必要信息
            refundModel.setOutTradeNo(tradeNo);
            refundModel.setRefundAmount(String.valueOf(userReservation.getReservationPrice()));
            refundModel.setRefundReason("用户取消订单，退还费用");

            request.setBizModel(refundModel);

            log.debug("申请退款 > " + userReservation);
            try {
                // 调用alipay接口，尝试退款
                AlipayTradeRefundResponse refundResponse = alipayClient.execute(request);

                log.debug("alipay接口返回 > 申请退款成功");

                // 将订单的状态修改为已支付
                Map<String, Object> newReservationMap =
                        UserReservationHelper.tempUserReservationStatusMap(null, ReservationStatus.CANCEL.get());

                int result = userReservationMapper.update(userReservationIdMap, newReservationMap);

                // 修改失败，说明订单已经被更新
                if (result != 1) {
                    String msg = "退款失败，请检查参数是否正确 > tradeNo: " + tradeNo;
                    log.warn(msg);
                    return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
                } else {
                    String msg = "退款成功 > tradeNo: " + tradeNo;
                    log.debug(msg);
                    return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, tradeNo);
                }
            } catch (AlipayApiException e) {
                String msg = "申请退款异常 > " + userReservation;
                log.warn(msg);
                log.error("[{}] > {}", e.getLocalizedMessage(), e.getStackTrace());
                e.printStackTrace();
                return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
            }
        } catch (ProjectException e) {
            return e.getResult();
        }
    }
}
