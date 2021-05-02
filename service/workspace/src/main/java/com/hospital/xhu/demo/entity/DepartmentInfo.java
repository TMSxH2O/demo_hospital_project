package com.hospital.xhu.demo.entity;

import com.hospital.xhu.demo.exception.ProjectException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * 医院科室信息
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/4/30
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepartmentInfo implements Entity {
    private Long id;
    private Long hospitalId;
    private String departmentName;

    @Override
    public void init() throws ProjectException {
        if (null == hospitalId) {
            // throw new ProjectException(ExceptionCode.DEPARTMENT_INFO, "科室对应的医院id不能为空");
            // 目前只针对一个医院，因此医院编号默认为0
            hospitalId = 0L;
        }
        if (null == departmentName) {
            departmentName = "";
        }
    }
}
