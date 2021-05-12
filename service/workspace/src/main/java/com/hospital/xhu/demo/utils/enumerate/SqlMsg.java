package com.hospital.xhu.demo.utils.enumerate;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
public enum SqlMsg {
    // 测试
    REBUILD_KEY_ERROR("rebuild[%s]找不到对应的键 > %s"),
    REBUILD_SUCCESS("rebuild[%s] > before:%s after:%s"),
    SELECT_COUNT_MISSING_REQUIRED_INFO("查询数据库[%s]的条件不能为空"),
    SELECT_SUCCESS("查询数据库[%s]成功 %s [%d,%d] ordered:%s %b > %s"),
    SELECT_COUNT_SUCCESS("查询数据库[%s]成功 %s > %d"),
    SELECT_FAILED("数据库查询[%s]失败，请检查传入参数是否正确:%s"),
    SELECT_NOT_UNIQUE("数据库查询[%s]结果不唯一:%s"),
    INSERT_SUCCESS("插入数据库[%s]成功 %s > %d"),
    INSERT_FAILED("插入数据库[%s]失败，请检查数据是否合法:%s"),
    UPDATE_MISSING_REQUIRED_INFO("数据库更新[%s]的判断条件不应该为空，请检查输入的数据是否正确:%s"),
    UPDATE_SUCCESS("更新数据库[%s]成功 before:%s after:%s > %d"),
    UPDATE_FAILED("更新数据库[%s]失败，请检查数据是否合法 before:%s after:%s"),
    DELETE_MISSING_REQUIRED_INFO("数据库删除[%s]的判断条件不能为空"),
    DELETE_SUCCESS("删除数据库[%s]成功 %s >>> %d"),
    DELETE_FAILED("删除数据库[%s]失败，请检查删除条件是否合法:%s"),
    ;

    private final String msg;

    SqlMsg(String msg) {
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
