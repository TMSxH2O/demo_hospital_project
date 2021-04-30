package com.hospital.xhu.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/28
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HospitalInfo implements Entity {
    private Long id;
    private String hospitalName;
    private String hospitalAddress;
    private Long phone;

    @Override
    public void init() {
        if (null == hospitalName) {
            hospitalName = "";
        }
        if (null == hospitalAddress) {
            hospitalAddress = "";
        }
        if (null == phone) {
            phone = 0L;
        }
    }
}
