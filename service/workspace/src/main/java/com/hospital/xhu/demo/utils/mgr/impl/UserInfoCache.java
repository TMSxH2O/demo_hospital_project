package com.hospital.xhu.demo.utils.mgr.impl;

import com.hospital.xhu.demo.dao.impl.UserInfoMapperImpl;
import com.hospital.xhu.demo.entity.UserInfo;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.EntityCache;
import com.hospital.xhu.demo.utils.mgr.IEntityCache;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * TODO 暂时搁置，最后尝试提升效率时再做
 * TODO 用户信息缓冲
 * 需要注意不要存放相同对象，需要检查 hashCode
 * listSize 限制缓冲的最大容量，达到最大容量之后开始清理
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/18
 */
//@Component
//@Scope("singleton")
public class UserInfoCache implements IEntityCache<UserInfo> {

    static final private List<EntityCache<UserInfo>> CACHE = new LinkedList<>();
    @Value("${cache.cache_size}")
    private static int listSize;
    @Value("${cache.limit_time")
    private static int lruLimitTime;
//    @Resource(name = "UserInfoMapper")
    private UserInfoMapperImpl userInfoMapper;

    static final private List<String> USERINFO_KEYS =
            Arrays.asList(
                    "username", "password", "passwordSalt", "email",
                    "phone", "isLogin", "lastLoginTime", "userImageUri"
            );

    /**
     * 查询方法，如果缓冲中存在对应的对象，直接返回结果列表
     *
     * @param map           查询条件
     * @param pageNum       Optional 页码
     * @param pageSize      Optional 页大小
     * @param orderedKey    Optional 排序的键
     * @param isDesc        Optional 是否反向
     * @return 符合结果的列表
     */
    @Override
    public List<UserInfo> select(
            Map<String, Object> map, Integer pageNum, Integer pageSize, String orderedKey, Boolean isDesc)
            throws ProjectException {
        // 获取符合条件的结果
        List<UserInfo> result = searchObjectInCache(map);
        if (CollectionUtils.isEmpty(result)) {
            // 没有查询到数据，就需要查询数据库的接口
            result = userInfoMapper.select(map, orderedKey, isDesc, pageNum, pageSize);
        }
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

    /**
     * 在缓冲中进行搜索，返回符合条件的列表
     *
     * @param map 搜索条件
     * @return 符合条件的对象列表
     */
    private List<UserInfo> searchObjectInCache(Map<String, Object> map) {
        List<UserInfo> result = new LinkedList<>();
        for (String key : USERINFO_KEYS) {
            if (map.containsKey(key)) {
                result = updateTempSearchObject(result, key, map.get(key));
            }
        }
        return result;
    }

    /**
     * 用于更新临时搜索到的数据列表
     *
     * @param list 搜索的列表，如果列表为空，会直接在列表上添加；反之，在现有列表中进行删减
     * @param key 搜索的键
     * @param value 搜索的值
     * @return 最终的列表地址
     */
    private List<UserInfo> updateTempSearchObject(List<UserInfo> list, String key, Object value) {
        if (CollectionUtils.isEmpty(list)) {
            for (EntityCache<UserInfo> userInfo: CACHE) {
                UserInfo temp = userInfo.getData();
                if (temp.equals(value)) {
                    list.add(temp);
                }
            }
        } else {
            list = list.parallelStream()
                    .filter(o -> o.getPassword().equals(value))
                    .collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 添加数据
     * @param userInfo 用户数据对象
     */
    private void addCache(UserInfo userInfo) {
        EntityCache<UserInfo> temp = new EntityCache<>(userInfo);
        // 如果缓冲列表达到上限，清理缓冲
        if (listSize <= CACHE.size() + 1) {
            // 获取当前时间
            long limitTime = Time.now() - lruLimitTime;
            for (int index = CACHE.size() - 1; CACHE.get(index).getTime() < limitTime; --index) {
                // 删除节点
                CACHE.remove(index);
            }
        }
        // 添加节点，因为是最新的节点，排在最前
        CACHE.add(0, temp);
    }
}
