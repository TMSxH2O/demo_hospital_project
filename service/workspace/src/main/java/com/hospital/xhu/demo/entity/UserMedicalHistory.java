package com.hospital.xhu.demo.entity;

import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/2
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMedicalHistory implements Entity {
    protected Long id;
    protected Long userId;
    protected Long doctorId;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    protected LocalDate medicalDate;
    protected String medicalHistoryUri;

    @Override
    public void init() throws ProjectException {
        if (null == userId) {
            throw new ProjectException(ExceptionCode.USER_MEDICAL_HISTORY, "用户病例对应的用户不能为空");
        }
        if (null == doctorId) {
            throw new ProjectException(ExceptionCode.USER_MEDICAL_HISTORY, "用户病例对应的医生不能为空");
        }
        if (null == medicalDate) {
            medicalDate = LocalDate.now();
        }
        if (null == medicalHistoryUri) {
            medicalHistoryUri = "";
        }
    }
}
