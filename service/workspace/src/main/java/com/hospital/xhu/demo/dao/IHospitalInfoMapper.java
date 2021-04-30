package com.hospital.xhu.demo.dao;

import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.entity.HospitalInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/28
 */
@Mapper
@Repository("hospitalInfoMapper")
public interface IHospitalInfoMapper extends IGeneralMapper<HospitalInfo> {
}
