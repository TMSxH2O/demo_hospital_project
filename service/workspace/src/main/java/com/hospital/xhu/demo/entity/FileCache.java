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
 * @date 2021/5/3
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileCache implements Entity {

    private Long id;
    private String filePath;
    private String fileSha256;

    @Override
    public void init() throws ProjectException {
        if (null == filePath) {
            throw new ProjectException(ExceptionCode.FILE_EXCEPTION, "文件对应的地址不能为空");
        }
        if (null == fileSha256) {
            throw new ProjectException(ExceptionCode.FILE_EXCEPTION, "文件对应的MD5不能为空");
        }
    }
}
