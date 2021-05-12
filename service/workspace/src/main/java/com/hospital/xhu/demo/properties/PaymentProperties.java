package com.hospital.xhu.demo.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/8
 */
@ConfigurationProperties(prefix = "pay.alipay")
@Slf4j
@Data
public class PaymentProperties {
        /** 支付宝gatewayUrl */
        private String gatewayUrl;
        /** 商户应用id */
        private String appId;
        /** RSA私钥，用于对商户请求报文加签 */
        private String appPrivateKey;
        /** 支付宝RSA公钥，用于验签支付宝应答 */
        private String alipayPublicKey;
        /** 签名类型 */
        private String signType = "RSA2";
        /** 格式 */
        private String format = "json";
        /** 编码 */
        private String charset = "UTF-8";
        /** 同步地址 */
        private String returnUrl;
        /** 异步地址 */
        private String notifyUrl;
        /** 支付模型 */
        private String productCode;
        /** 最大查询次数 */
        private static int maxQueryRetry = 5;
        /** 查询间隔（毫秒） */
        private static long queryDuration = 5000;
        /** 最大撤销次数 */
        private static int maxCancelRetry = 3;
        /** 撤销间隔（毫秒） */
        private static long cancelDuration = 3000;
}
