package com.hospital.xhu.demo.dao;

import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.entity.UserReservation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
@Mapper
@Repository("userReservationMapper")
public interface IUserReservationMapper extends IGeneralMapper<UserReservation> {

}
