package com.hospital.xhu.demo.utils.mgr;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 通过使用 LRU 管理实体的列表，在超出上限之后删除其中不常用的实体
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/18
 */
public interface IEntityCache<T> {
    /**
     * 查询实体的通用模板
     *
     * @param map           查询条件
     * @param pageNum       Optional 页码
     * @param pageSize      Optional 页大小
     * @param orderedKey    Optional 排序的键
     * @param isDesc        Optional 是否反向
     * @return 查询的对象列表
     */
    List<T> select(Map<String, String> map, Integer pageNum, Integer pageSize, String orderedKey, Boolean isDesc);

    /**
     * 更新实体的通用模板
     *
     * @param selectMap 寻找需要查找的条件
     * @param updateMap 更新的值
     * @return 更新成功的数量
     */
    int update(Map<String, String> selectMap, Map<String, String> updateMap);

    /**
     * 添加始替的通用模板
     *
     * @param entityList 添加的实体列表
     * @return 添加成功的列的数量
     */
    int insert(List<T> entityList);

    /**
     * 删除实体的通用模板
     *
     * @param map 需要删除的实体的条件
     * @return 删除成功的数量
     */
    int delete(Map<String, String> map);
}
