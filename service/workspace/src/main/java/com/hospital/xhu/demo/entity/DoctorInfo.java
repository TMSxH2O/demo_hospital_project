package com.hospital.xhu.demo.entity;

import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorInfo implements Entity {
    private Long id;
    private Long departmentId;
    private String doctorName;
    private Float reservationPrice;
    private Integer remainingCapacity;
    private String doctorImageUri;

    @Override
    public void init() throws ProjectException {
        if (null == departmentId) {
            throw new ProjectException(ExceptionCode.DOCTOR_INFO, "医生对应的科室id不能为空");
        }
        if (null == doctorName) {
            doctorName = "";
        }
        if (null == reservationPrice) {
            reservationPrice = 0.0f;
        }
        if (null == remainingCapacity) {
            remainingCapacity = 1;
        }
        if (null == doctorImageUri) {
            doctorImageUri = "";
        }
    }
}
