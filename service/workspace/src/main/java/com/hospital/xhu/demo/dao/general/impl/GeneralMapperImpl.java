package com.hospital.xhu.demo.dao.general.impl;

import com.github.pagehelper.PageHelper;
import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.entity.Entity;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import com.hospital.xhu.demo.utils.enumerate.SqlMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
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
abstract public class GeneralMapperImpl<T extends Entity> {

    private final IGeneralMapper<T> mapper;

    public GeneralMapperImpl(IGeneralMapper<T> mapper) {
        this.mapper = mapper;
    }

    /**
     * 返回不同数据库表名
     *
     * @return getSqlName()
     */
    protected abstract String getSqlName();

    /**
     * 获取字段转换的的Map
     *
     * @return 获取字段转换的Map
     */
    protected abstract Map<String, String> getMap();

    /**
     * 获取当前Mapper对应的错误码
     *
     * @return 错误码
     */
    protected abstract ExceptionCode getExceptionCode();

    /**
     * 将Map中的key从类属性转换为数据库字段名
     *
     * @param map 转换前的Map
     * @return 转换后的Map
     * @throws ProjectException 转换失败的信息
     */
    protected Map<String, Object> rebuildMap(Map<String, Object> map) throws ProjectException {
        Map<String, Object> result = new HashMap<>(map.size());
        Map<String, String> tempRebuildMap = getMap();
        for (String key : map.keySet()) {
            if (tempRebuildMap.containsKey(key)) {
                // 需要同时保证值不为空
                Object value = map.get(key);
                if (null != value && !"".equals(value)) {
                    result.put(tempRebuildMap.get(key), value);
                }
            }
            else {
                String msg = SqlMsg.REBUILD_KEY_ERROR.getMsg(getSqlName(), key);
                log.warn(msg);
                throw new ProjectException(getExceptionCode(), msg);
            }
        }
        log.debug(SqlMsg.REBUILD_SUCCESS.getMsg(getSqlName(), map, result));

        return result;
    }

    /**
     * 将key从类属性转换为数据库字段名
     *
     * @param key 转换前的字符串
     * @return 转换后的字符串
     * @throws ProjectException 转化失败
     */
    protected String getMapString(String key) throws ProjectException {
        Map<String, String> tempRebuildMap = getMap();
        if (tempRebuildMap.containsKey(key)) {
            String result = tempRebuildMap.get(key);
            log.debug(SqlMsg.REBUILD_SUCCESS.getMsg(getSqlName(), key, result));
            return result;
        }
        else {
            String msg = SqlMsg.REBUILD_KEY_ERROR.getMsg(getSqlName(), key);
            log.warn(msg);
            throw new ProjectException(getExceptionCode(), msg);
        }
    }

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
    public List<T> select(
            Map<String, Object> map, String orderedKey, Boolean isDesc,
            Integer page, Integer pageSize)
            throws ProjectException {
        // 存放转换后的Map
        Map<String, Object> newMap = rebuildMap(map);
        String tempOrderedKey = null;
        if (!StringUtils.isEmpty(orderedKey)) {
            // 尝试获取数据库中排序的字段
            tempOrderedKey = getMapString(orderedKey);
        }
        // 默认从第一页开始
        if (null == page || page < 1) {
            page = 1;
        }

        // 默认每页10条数据
        if (null == pageSize) {
            pageSize = 10;
        }

        try {
            // 分页查询
            PageHelper.startPage(page, pageSize);
            List<T> result = mapper.select(newMap, tempOrderedKey, isDesc);
            log.debug(SqlMsg.SELECT_SUCCESS.getMsg(getSqlName(), map, page, pageSize, orderedKey, isDesc, result));
            return result;
        } catch (Exception e) {
            // 查询失败抛出项目通用异常
            String msg = SqlMsg.SELECT_FAILED.getMsg(getSqlName(), map);
            log.warn(msg);
            log.error(e.getMessage());
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }
    }

    /**
     * 查询符合条件的数据
     *
     * @param map 查询条件
     * @return 符合条件的数量
     * @throws ProjectException 查询失败的异常
     */
    public int selectCount(Map<String, Object> map) throws ProjectException {
        Map<String, Object> tempMap = rebuildMap(map);

        if (CollectionUtils.isEmpty(tempMap)) {
            String msg = SqlMsg.SELECT_COUNT_MISSING_REQUIRED_INFO.getMsg(getSqlName(), map);
            log.warn(msg);
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }

        try {
            int result = mapper.selectCount(tempMap);
            log.debug(SqlMsg.SELECT_COUNT_SUCCESS.getMsg(getSqlName(), map, result));
            return result;
        } catch (Exception e) {
            String msg = SqlMsg.SELECT_FAILED.getMsg(getSqlName(), map);
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
    public T selectPrimary(Map<String, Object> map) throws ProjectException {
        List<T> result = select(map, null, null, null, 1);

        if (CollectionUtils.isEmpty(result)) {
            String msg = SqlMsg.SELECT_FAILED.getMsg(getSqlName(), map);
            log.warn("查询结果[{}] > {}", result, msg);
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }
        else if (result.size() > 1) {
            String msg = SqlMsg.SELECT_NOT_UNIQUE.getMsg(getSqlName(), map);
            log.warn(msg);
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }

        return result.get(0);
    }

    /**
     * 通用插入数据的方法
     *
     * @param list 需要插入数据的列表
     * @return 插入成功的数量
     * @throws ProjectException 插入失败的异常
     */
    public int insert(List<T> list) throws ProjectException {
        try {
            int result = mapper.insert(list);
            log.debug(SqlMsg.INSERT_SUCCESS.getMsg(getSqlName(), list, result));
            return result;
        } catch (Exception e) {
            String msg = SqlMsg.INSERT_FAILED.getMsg(getSqlName(), list);
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
            String msg = SqlMsg.UPDATE_MISSING_REQUIRED_INFO.getMsg(getSqlName(), before);
            log.warn(msg);
            throw new ProjectException(
                    ExceptionCode.SQL_EXCEPTION,
                    msg);
        }

        try {
            int result = mapper.update(tempBeforeMap, tempAfterMap);
            log.debug(SqlMsg.UPDATE_SUCCESS.getMsg(getSqlName(), before, after, result));
            return result;
        } catch (Exception e) {
            String msg = SqlMsg.UPDATE_FAILED.getMsg(getSqlName(), before, after);
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
            String msg = SqlMsg.DELETE_MISSING_REQUIRED_INFO.getMsg(getSqlName(), map);
            log.warn(msg);
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }

        try {
            int result = mapper.delete(tempDeleteMap);
            log.debug(SqlMsg.DELETE_SUCCESS.getMsg(getSqlName(), map, result));
            return result;
        } catch (Exception e) {
            String msg = SqlMsg.DELETE_FAILED.getMsg(getSqlName(), map);
            log.warn(msg);
            log.error(e.getMessage());
            throw new ProjectException(ExceptionCode.SQL_EXCEPTION, msg);
        }
    }
}
