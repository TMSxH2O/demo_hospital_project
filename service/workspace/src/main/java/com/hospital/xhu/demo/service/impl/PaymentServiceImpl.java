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
        refundList = new ArrayList<>();
    }

    /**
     * ????????????????????????
     *
     * @param reservationId ????????????id
     * @param response      ????????????
     * @return ???????????????
     */
    @Override
    public CommonResult<?> redirectPaymentPage(String reservationId, HttpServletResponse response) {
        if (StringUtils.isEmpty(reservationId)) {
            String msg = CommonServiceMsg.PAYMENT_MISSING_REQUIRED_INFO.getMsg("reservationId: " + reservationId);
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }

        try {
            Map<String, Object> map = UserReservationHelper.tempUserReservationId(reservationId);
            UserReservation userReservation = userReservationMapper.selectPrimary(map);

            // ??????????????????
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            model.setOutTradeNo(reservationId);
            model.setSubject(subjectName);
            model.setTotalAmount(String.valueOf(userReservation.getReservationPrice()));
            model.setBody(UserReservationHelper.getReservationBody(userReservation));
            model.setProductCode(properties.getProductCode());

            // ??????????????????
            AlipayTradePagePayRequest pagePayRequest = new AlipayTradePagePayRequest();
            pagePayRequest.setReturnUrl(properties.getReturnUrl());
            pagePayRequest.setNotifyUrl(properties.getNotifyUrl());
            pagePayRequest.setBizModel(model);

            // ????????????
            String form = alipayClient.pageExecute(pagePayRequest).getBody();
            response.setContentType("text/html;charset=" + properties.getCharset());
            // ?????????????????????
            try (PrintWriter writer = response.getWriter()) {
                writer.write(form);
                writer.flush();

                // ????????????
                String msg = CommonServiceMsg.PAYMENT_REDIRECT_SUCCESS.getMsg();
                log.debug(msg);
                return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, userReservation);
            } catch (IOException e) {
                String msg = "??????????????????????????????????????? > " + e.getLocalizedMessage();
                log.warn(msg);
                log.error(e.getMessage());
                return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
            }
        } catch (ProjectException e) {
            return e.getResult();
        } catch (AlipayApiException e) {
            String msg = "????????????????????????????????? > " + e.getErrMsg();
            log.warn(msg);
            log.error(e.getMessage());
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }
    }

    /**
     * ????????????
     *
     * @param request@return ???????????????
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

        if (!properties.getAppId().equals(appId)) {
            String msg = CommonServiceMsg.PAYMENT_FAILED.getMsg("?????????????????????????????????????????????");
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }

        // ??????????????????????????????????????????
        Map<String, Object> userReservationIdMap = UserReservationHelper.tempUserReservationId(tradeNo);
        UserReservationHelper.tempUserReservationStatusMap(userReservationIdMap, ReservationStatus.UNPAID.get());

        // ??????????????????
        Map<String, Object> newReservationMap =
                UserReservationHelper.tempUserReservationStatusMap(null, ReservationStatus.PAID.get());

        CommonResult<?> result = userReservationService.updateUserReservation(userReservationIdMap, newReservationMap);

        if (CommonCode.SUCCESS.getCode() == result.getCode()) {
            // ?????????????????????
            Map<String, String> map = new HashMap<>(2);
            map.put("out_trade_no", tradeNo);
            map.put("app_id", appId);
            result = new CommonResult<>(CommonCode.SUCCESS.getCode(), "????????????????????????", map);
        }

        return result;
    }

    @Override
    public CommonResult<?> asyncNotification(HttpServletRequest request) {
        String tradeNo = request.getParameter("out_trade_no");
        String appId = request.getParameter("app_id");
        if (StringUtils.isEmpty(tradeNo) || StringUtils.isEmpty(appId)) {
            String msg = CommonServiceMsg.PAYMENT_MISSING_REQUIRED_INFO.getMsg(
                    String.format("tradeNo: %s appId: %s", tradeNo, appId));
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }

        if (!properties.getAppId().equals(appId)) {
            String msg = CommonServiceMsg.PAYMENT_FAILED.getMsg("?????????????????????????????????????????????");
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }

        // ??????????????????????????????????????????
        Map<String, Object> userReservationIdMap = UserReservationHelper.tempUserReservationId(tradeNo);
        UserReservationHelper.tempUserReservationStatusMap(userReservationIdMap, ReservationStatus.UNPAID.get());

        // ??????????????????
        Map<String, Object> newReservationMap =
                UserReservationHelper.tempUserReservationStatusMap(null, ReservationStatus.PAID.get());

        CommonResult<?> result = userReservationService.updateUserReservation(userReservationIdMap, newReservationMap);

        if (CommonCode.SUCCESS.getCode() == result.getCode()) {
            // ?????????????????????
            Map<String, String> map = new HashMap<>(2);
            map.put("out_trade_no", tradeNo);
            map.put("app_id", appId);
            result = new CommonResult<>(CommonCode.SUCCESS.getCode(), "????????????????????????", map);
        }

        return result;
    }

    /**
     * ????????????
     *
     * @param tradeNo ?????????
     * @return ???????????????
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
            String msg = CommonServiceMsg.PAYMENT_FAILED.getMsg("??????????????????");
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
        }

        // ???????????????????????????????????????
        refundList.add(tradeNo);

        try {
            // ??????????????????
            Map<String, Object> userReservationIdMap = UserReservationHelper.tempUserReservationId(tradeNo);
            UserReservation userReservation = userReservationMapper.selectPrimary(userReservationIdMap);

            if (ReservationStatus.PAID.get() != userReservation.getReservationStatus()) {
                String msg = "??????????????????????????????????????? > " + userReservation;
                log.warn(msg);
                return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
            }

            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();

            // ???????????????????????????
            refundModel.setOutTradeNo(tradeNo);
            refundModel.setRefundAmount(String.valueOf(userReservation.getReservationPrice()));
            refundModel.setRefundReason("?????????????????????????????????");

            request.setBizModel(refundModel);

            log.debug("???????????? > " + userReservation);
            try {
                // ??????alipay?????????????????????
                AlipayTradeRefundResponse refundResponse = alipayClient.execute(request);

                log.debug("alipay???????????? > ??????????????????");

                // ????????????????????????????????????
                Map<String, Object> newReservationMap =
                        UserReservationHelper.tempUserReservationStatusMap(null, ReservationStatus.CANCEL.get());

                int result = userReservationMapper.update(userReservationIdMap, newReservationMap);

                // ??????????????????????????????????????????
                if (result != 1) {
                    String msg = "?????????????????????????????????????????? > tradeNo: " + tradeNo;
                    log.warn(msg);
                    return new CommonResult<>(ExceptionCode.PAYMENT_ERROR.getCode(), msg);
                } else {
                    String msg = "???????????? > tradeNo: " + tradeNo;
                    log.debug(msg);
                    return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, tradeNo);
                }
            } catch (AlipayApiException e) {
                String msg = "?????????????????? > " + userReservation;
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
