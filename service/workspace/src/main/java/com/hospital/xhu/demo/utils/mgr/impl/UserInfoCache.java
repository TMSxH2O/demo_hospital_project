package com.hospital.xhu.demo.utils.mgr.impl;

import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.utils.EntityCache;
import com.hospital.xhu.demo.utils.mgr.IEntityCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * TODO 用户信息缓冲
 * 需要注意不要存放相同对象，需要检查 hashCode
 * listSize 限制缓冲的最大容量，达到最大容量之后开始清理
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/18
 */
@Component
@Scope("singleton")
public class UserInfoCache implements IEntityCache<UserInfo> {

    @Value("${cache.user_info}")
    private static int listSize;

    static final List<EntityCache<UserInfo>> CACHE = new LinkedList<>();

    @Override
    public List<UserInfo> select(Map<String, String> map, Integer pageNum, Integer pageSize, String orderedKey, Boolean isDesc) {
        return null;
    }

    @Override
    public int update(Map<String, String> selectMap, Map<String, String> updateMap) {
        return 0;
    }

    @Override
    public int insert(List<UserInfo> entityList) {
        return 0;
    }

    @Override
    public int delete(Map<String, String> map) {
        return 0;
    }
}
