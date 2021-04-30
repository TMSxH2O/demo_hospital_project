package com.hospital.xhu.demo.utils.resultcode;

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
    UPDATE_MISSING_REQUIRED_INFO("更新[%s]的索引值和修改值都不能为空"),
    UPDATE_SUCCESS("更新[%s]成功"),
    UPDATE_FAILED("更新[%s]失败 > {%s, %s}"),
    INSERT_MISSING_REQUIRED_INFO("插入[%s]的数据不能为空"),
    INSERT_SUCCESS("插入[%s]成功"),
    INSERT_FAILED("插入[%s]失败 > %s"),
    DELETE_MISSING_REQUIRED_INFO("删除[%s]的索引值不能为空"),
    DELETE_SUCCESS("删除[%s]成功"),
    DELETE_FAILED("删除[%s]失败 > %s"),
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
