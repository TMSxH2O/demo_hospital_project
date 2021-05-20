package com.hospital.xhu.demo.utils.enumerate;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
public enum CommonServiceMsg {
    // 模板化各个返回msg输出
    SELECT_SUCCESS("查询[%s]成功 > {%s, %s, %b, %d, %d}"),
    SELECT_COUNT_SUCCESS("查询[%s]成功 > {%s}"),
    SELECT_FAILED("查询[%s]失败 > %s"),
    UPDATE_MISSING_REQUIRED_INFO("更新[%s]的索引值和修改值都不能为空"),
    UPDATE_SUCCESS("更新[%s]成功"),
    UPDATE_FAILED("更新[%s]失败 > {%s, %s}"),
    INSERT_MISSING_REQUIRED_INFO("插入[%s]的数据不能为空"),
    INSERT_SUCCESS("插入[%s]成功"),
    INSERT_FAILED("插入[%s]失败 > %s"),
    DELETE_MISSING_REQUIRED_INFO("删除[%s]的索引值不能为空"),
    DELETE_SUCCESS("删除[%s]成功"),
    DELETE_FAILED("删除[%s]失败 > %s"),
    FILE_PARSE_FAILED("文件解析失败"),
    FILE_UPDATE_SUCCESS("文件上传成功 > filename:%s sha256:%s"),
    FILE_PATH_ERROR("文件目录出现异常 > %s"),
    FILE_PARSE_ERROR("文件解析异常 > %s"),
    FILE_UPDATE_FAILED("文件接受出现异常 > %s"),
    TEMPLATE_FILE_PARAM_ERROR("模板参数异常 > %s"),
    TEMPLATE_FILE_NO_FOUND("没有找到对应的模板文件 > %s"),
    TEMPLATE_FAILED("模板输出异常 > %s"),
    TEMPLATE_UPDATE_SUCCESS("模板上传成功 > filename:%s"),
    TEMPLATE_PREVIEW_SUCCESS("模板预览成功 > filename:%s"),
    PAYMENT_MISSING_REQUIRED_INFO("支付接口缺少了必要的数据 > %s"),
    PAYMENT_REDIRECT_SUCCESS("支付重定向成功"),
    PAYMENT_SUCCESS("支付成功 > %s %s"),
    PAYMENT_FAILED("支付失败 > %s"),
    ;

    private final String msg;

    CommonServiceMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 拼接形成最终的字符串
     *
     * @param args 需要格式化的参数
     * @return 格式化后的字符串
     */
    public String getMsg(Object... args) {
        return String.format(msg, args);
    }
}
