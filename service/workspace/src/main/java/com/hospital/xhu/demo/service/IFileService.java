package com.hospital.xhu.demo.service;

import com.hospital.xhu.demo.utils.CommonResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/3
 */
public interface IFileService {
    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return 上传的结果
     */
    CommonResult<?> updateImgFile(MultipartFile file);

    /**
     * 生成模板文件
     *
     * @param map 需要插入的数据
     * @return 生成模板文件
     */
    CommonResult<?> generalTemplateFile(Map<String, Object> map);

    /**
     * 预览病例文件
     *
     * @param reservationCode 预约订单号
     * @param response        响应对象
     * @return 预览的结果
     */
    CommonResult<?> previewTemplateFile(String reservationCode, HttpServletResponse response);
}
