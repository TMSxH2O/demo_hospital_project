package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.IDepartmentInfoMapper;
import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapperImpl;
import com.hospital.xhu.demo.entity.DepartmentInfo;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.resultcode.ExceptionCode;
import com.hospital.xhu.demo.utils.resultcode.SqlMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
@Repository("departmentInfoMapperImpl")
@Slf4j
public class DepartmentInfoMapperImpl extends GeneralMapperImpl<DepartmentInfo, IGeneralMapper<DepartmentInfo>> {

    private static final Map<String, String> DEPARTMENT_INFO_MAP = new HashMap<>();

    static {
        DEPARTMENT_INFO_MAP.put("id", "department_id");
        DEPARTMENT_INFO_MAP.put("hospitalId", "hospital_id");
        DEPARTMENT_INFO_MAP.put("departmentName", "department_name");
    }

    public DepartmentInfoMapperImpl(@Qualifier("departmentInfoMapper") IDepartmentInfoMapper departmentInfoMapper) {
        super(departmentInfoMapper);
    }

    /**
     * 将Map中的key从类属性转换为数据库字段名
     *
     * @param map 转换前的Map
     * @return 转换后的Map
     */
    @Override
    protected Map<String, Object> rebuildMap(Map<String, Object> map) throws ProjectException {
        Map<String, Object> result = new HashMap<>(map.size());
        for (String key : map.keySet()) {
            if (DEPARTMENT_INFO_MAP.containsKey(key)) {
                result.put(DEPARTMENT_INFO_MAP.get(key), map.get(key));
            }
            else {
                String msg = SqlMsg.REBUILD_KEY_ERROR.getMsg(getSqlName(), key);
                log.warn(msg);
                throw new ProjectException(ExceptionCode.DEPARTMENT_INFO, msg);
            }
        }
        log.debug(SqlMsg.REBUILD_SUCCESS.getMsg(getSqlName(), map, result));

        return result;
    }

    /**
     * 转换某个字符串到数据库字段名
     *
     * @param key 转换前的字符串
     * @return 转换后的字符串
     * @throws ProjectException 转换失败的报错
     */
    @Override
    protected String getMapString(String key) throws ProjectException {
        if (DEPARTMENT_INFO_MAP.containsKey(key)) {
            String result = DEPARTMENT_INFO_MAP.get(key);
            log.debug(SqlMsg.REBUILD_SUCCESS.getMsg(getSqlName(), key, result));
            return result;
        }
        else {
            String msg = SqlMsg.REBUILD_KEY_ERROR.getMsg(getSqlName(), key);
            log.warn(msg);
            throw new ProjectException(ExceptionCode.DEPARTMENT_INFO, msg);
        }
    }

    @Override
    protected String getSqlName() {
        return "department_info";
    }
}
