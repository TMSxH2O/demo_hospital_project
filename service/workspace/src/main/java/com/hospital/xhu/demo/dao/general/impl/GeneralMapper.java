package com.hospital.xhu.demo.dao.general.impl;

import com.github.pagehelper.PageHelper;
import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.entity.Entity;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.resultcode.ExceptionCode;
import com.hospital.xhu.demo.utils.resultcode.SqlMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
@Slf4j
abstract public class GeneralMapper<C extends Entity, T extends IGeneralMapper<C>> {
    private static final String SQL_NAME = "";

    protected final T mapper;

    public GeneralMapper(T mapper) {
        this.mapper = mapper;
    }

    /**
     * 将Map中的key从类属性转换为数据库字段名
     *
     * @param map 转换前的Map
     * @return 转换后的Map
     * @throws ProjectException 转换失败的信息
     */
    protected abstract Map<String, Object> rebuildMap(Map<String, Object> map) throws ProjectException;

    /**
     * 将key从类属性转换为数据库字段名
     *
     * @param key 转换前的字符串
     * @return 转换后的字符串
     * @throws ProjectException 转化失败
     */
    protected abstract String getMapString(String key) throws ProjectException;

    /**
     * 通用查询数据的方法
     *
     * @param map        Optional 查找条件
     * @param orderedKey Optional 排序字段
     * @param isDesc     Optional 是否反向（默认升序）
     * @param page       Optional 页码
     * @param pageSize   Optional 页大小
     * @return 符合条件的医院科室信息列表
     * @throws ProjectException 查询失败信息
     */
    public List<C> select(
            Map<String, Object> map, String orderedKey, Boolean isDesc,
            Integer page, Integer pageSize)
            throws ProjectException {
        // 存放转换后的Map
        System.out.println(">>>" + map);
        Map<String, Object> newMap = rebuildMap(map);
        String tempOrderedKey = null;
        if (!StringUtils.isEmpty(orderedKey)) {
            // 尝试获取数据库中排序的字段
            tempOrderedKey = getMapString(orderedKey);
        }
        // 默认从第一页开始
        if (null == page) {
            page = 0;
        }

        // 默认每页10条数据
        if (null == pageSize) {
            pageSize = 10;
        }

        try {
            // 分页查询
            PageHelper.startPage(page, pageSize);
            List<C> result = mapper.select(newMap, tempOrderedKey, isDesc);
            log.debug(SqlMsg.SELECT_SUCCESS.getMsg(SQL_NAME, map, page, pageSize, orderedKey, isDesc, result));
            return result;
        } catch (Exception e) {
            // 查询失败抛出项目通用异常
            String msg = SqlMsg.SELECT_FAILED.getMsg(SQL_NAME, map);
            log.warn(msg);
            log.error(e.getMessage());
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }
    }

    /**
     * 重载接口，返回符合条件的唯一值，如果结果不唯一，则报错
     *
     * @param map 查询条件
     * @return 唯一结果
     */
    public C selectPrimary(Map<String, Object> map) throws ProjectException {
        List<C> result = select(map, null, null, null, 1);

        if (result.size() > 1) {
            String msg = SqlMsg.SELECT_NOT_UNIQUE.getMsg(SQL_NAME, map);
            log.warn(msg);
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }

        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * 通用插入数据的方法
     *
     * @param list 需要插入数据的列表
     * @return 插入成功的数量
     * @throws ProjectException 插入失败的异常
     */
    public int insert(List<C> list) throws ProjectException {
        try {
            int result = mapper.insert(list);
            log.debug(SqlMsg.INSERT_SUCCESS.getMsg(SQL_NAME, list, result));
            return result;
        } catch (Exception e) {
            String msg = SqlMsg.INSERT_FAILED.getMsg(SQL_NAME, list);
            log.warn(msg);
            log.error(e.getMessage());
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }
    }

    /**
     * 通用更新数据的方法
     *
     * @param before 更新条件
     * @param after  修改数据
     * @return 更新成功的数量
     * @throws ProjectException 修改的医院科室限定为空，或者更新操作报错
     */
    public int update(Map<String, Object> before, Map<String, Object> after) throws ProjectException {
        Map<String, Object> tempBeforeMap = rebuildMap(before);
        Map<String, Object> tempAfterMap = rebuildMap(after);

        // 防止一次性修改所有的医院科室信息，必须有一定的限制
        if (CollectionUtils.isEmpty(tempBeforeMap)) {
            String msg = SqlMsg.UPDATE_MISSING_REQUIRED_INFO.getMsg(SQL_NAME, before);
            log.warn(msg);
            throw new ProjectException(
                    ExceptionCode.SQL_EXCEPTION,
                    msg);
        }

        try {
            int result = mapper.update(tempBeforeMap, tempAfterMap);
            log.debug(SqlMsg.UPDATE_SUCCESS.getMsg(SQL_NAME, before, after, result));
            return result;
        } catch (Exception e) {
            String msg = SqlMsg.UPDATE_FAILED.getMsg(SQL_NAME, before, after);
            log.warn(msg);
            log.error(e.getMessage());
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }
    }

    /**
     * 通用删除数据的方法
     *
     * @param map 删除的条件
     * @return 删除的数量
     * @throws ProjectException 删除的条件为空，或者删除失败
     */
    public int delete(Map<String, Object> map) throws ProjectException {
        Map<String, Object> tempDeleteMap = rebuildMap(map);

        if (CollectionUtils.isEmpty(tempDeleteMap)) {
            String msg = SqlMsg.DELETE_MISSING_REQUIRED_INFO.getMsg(SQL_NAME, map);
            log.warn(msg);
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }

        try {
            int result = mapper.delete(tempDeleteMap);
            log.debug(SqlMsg.DELETE_SUCCESS.getMsg(SQL_NAME, map, result));
            return result;
        } catch (Exception e) {
            String msg = SqlMsg.DELETE_FAILED.getMsg(SQL_NAME, map);
            log.warn(msg);
            log.error(e.getMessage());
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }
    }
}
