package com.hospital.xhu.demo.dao;

import com.hospital.xhu.demo.dao.general.IGeneralMapper;
import com.hospital.xhu.demo.entity.TempUserReservation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/10
 */
@Mapper
@Repository("tempUserReservationMapper")
public interface ITempUserReservationMapper extends IGeneralMapper<TempUserReservation> {
}
