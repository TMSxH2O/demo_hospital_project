package com.hospital.xhu.demo.controller.logic;

import com.hospital.xhu.demo.service.IFileService;
import com.hospital.xhu.demo.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/3
 */
@RestController
@Slf4j
public class FileController {
    @Resource(name = "fileServiceImpl")
    private IFileService fileService;

    @PostMapping("file/update")
    public CommonResult<?> updateImgFile(
            @RequestParam("file") MultipartFile file) {
        return fileService.updateImgFile(file);
    }

    @PostMapping("medical/general")
    public CommonResult<?> generalTemplateFile(
            @RequestBody Map<String, Object> map) {
        return fileService.generalTemplateFile(map);
    }
}
