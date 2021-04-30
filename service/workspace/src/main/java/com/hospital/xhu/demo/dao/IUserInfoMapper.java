package com.hospital.xhu.demo.dao;

import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 用户信息的相关数据库操作，对应UserInfoMapper.xml文件
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/11
 */
@Mapper
@Repository("userInfoMapper")
public interface IUserInfoMapper extends IGeneralMapper<UserInfo> {
    /**
     * 查询所有的用户信息【测试用】
     * 除测试之外的地方不应该使用没有任何限制的查询
     * @return 所有用户的信息
     * @deprecated 只在测试中使用
     */
    List<UserInfo> testSelectAll();

}
