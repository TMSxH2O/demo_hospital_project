package com.hospital.xhu.demo.dao;

import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.entity.DoctorInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
@Mapper
@Repository("doctorInfoMapper")
public interface IDoctorInfoMapper extends IGeneralMapper<DoctorInfo> {

}
