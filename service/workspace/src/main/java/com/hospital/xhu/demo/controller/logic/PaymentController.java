package com.hospital.xhu.demo.controller.logic;

import com.hospital.xhu.demo.service.IPaymentService;
import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.annotation.PassToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/8
 */
@RestController
@RequestMapping("/alipay")
public class PaymentController {
    private final IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/page", produces = "text/html;charset=UTF-8")
    public void redirectAlipay(
            @RequestParam("reservationId") String reservationId, HttpServletResponse response) {
        paymentService.redirectPaymentPage(reservationId, response);
    }

    @PassToken
    @GetMapping("/callback")
    public CommonResult<?> syncNotification(
            HttpServletRequest request) {
        return paymentService.syncNotification(request);
    }

    @PostMapping("/notify")
    public CommonResult<?> asyncNotification(HttpServletRequest request) {
        return null;
    }

    @PostMapping("/refund")
    public CommonResult<?> refund(
            @RequestParam("reservationId") String tradeNo) {
        return paymentService.refund(tradeNo);
    }
}
