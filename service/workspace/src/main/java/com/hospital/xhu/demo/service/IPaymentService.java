package com.hospital.xhu.demo.service;

import com.hospital.xhu.demo.utils.CommonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/8
 */
public interface IPaymentService {
    /**
     * 根据预约订单信息，跳转到支付界面
     *
     * @param reservationId 预约订单id
     * @param response      响应对象
     * @return 响应信息
     */
    CommonResult<?> redirectPaymentPage(String reservationId, HttpServletResponse response);

    /**
     * 同步通知的界面
     *
     *
     * @param request@return 直接响应的处理结果
     */
    CommonResult<?> syncNotification(HttpServletRequest request);

    /**
     * 异步响应，支付接口自动调用，用于入库
     *
     * @param request 请求对象
     * @return 响应的结果（不重要）
     */
    CommonResult<?> asyncNotification(HttpServletRequest request);

    /**
     * 退款接口
     *
     * @param tradeNo 订单号
     * @return 响应的结果
     */
    CommonResult<?> refund(String tradeNo);
}
