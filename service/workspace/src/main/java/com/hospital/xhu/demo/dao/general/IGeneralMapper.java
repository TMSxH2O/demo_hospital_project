package com.hospital.xhu.demo.dao.general;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
public interface IGeneralMapper<T> {
    /**
     * 根据所传入的Map查询用户信息
     * 如果传入的 Map 为空，会返回所有的用户信息（必须避免）；
     * 如果没有查到符合条件的结果，返回的结果为空。
     *
     * @param map        key为所查询的字段名，value为对应的值（必须可以进行转换）
     * @param orderedKey 排序的键
     * @param isDesc     是否反向
     * @return 符合条件的所有用户的信息
     */
    List<T> select(
            @Param("searchMap") Map<String, Object> map,
            @Param("orderedKey") String orderedKey,
            @Param("isDesc") Boolean isDesc);

    /**
     * 添加新的用户的方法
     *
     * @param userInfos 需要添加的用户的列表
     * @return 返回插入成功的数量
     */
    int insert(@Param("info") List<T> userInfos);

    /**
     * 根据传入的before查找符合的用户，再使用after中的数据进行更新
     *
     * @param before 查找条件（如果为空，将会修改所有的用户）
     * @param after  新的数据
     * @return 更新成功的数量
     */
    int update(@Param("before") Map<String, Object> before, @Param("after") Map<String, Object> after);

    /**
     * 删除符合条件的数据
     *
     * @param map 删除的条件
     * @return 删除成功的数量
     */
    int delete(@Param("deleteMap") Map<String, Object> map);
}
