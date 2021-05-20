package com.hospital.xhu.demo.service.impl;

import com.hospital.xhu.demo.dao.impl.*;
import com.hospital.xhu.demo.entity.*;
import com.hospital.xhu.demo.exception.ProjectException;
import com.hospital.xhu.demo.properties.FileProperties;
import com.hospital.xhu.demo.service.IFileService;
import com.hospital.xhu.demo.utils.CommonResult;
import com.hospital.xhu.demo.utils.enumerate.CommonCode;
import com.hospital.xhu.demo.utils.enumerate.CommonServiceMsg;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import com.hospital.xhu.demo.utils.helper.DoctorInfoHelper;
import com.hospital.xhu.demo.utils.helper.FileCacheHelper;
import com.hospital.xhu.demo.utils.helper.UserInfoHelper;
import com.hospital.xhu.demo.utils.helper.UserReservationHelper;
import com.hospital.xhu.demo.utils.payment.ReservationStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/3
 */
@Slf4j
public class FileServiceImpl implements IFileService {

    private static final String MEDICAL_SURNAME = ".htm";

    @Autowired
    private FileCacheMapperImpl fileCacheMapper;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private UserInfoMapperImpl userInfoMapper;
    @Autowired
    private DoctorInfoMapperImpl doctorInfoMapper;
    @Autowired
    private UserMedicalHistoryMapperImpl userMedicalHistoryMapper;
    @Autowired
    private UserReservationMapperImpl userReservationMapper;

    @Autowired
    private FileProperties fileProperties;

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return 上传的结果
     * - 成功
     * { code: 200, msg: 上传成功, data: 上传的文件缓存 }
     * - 失败
     * { code: ExceptionCode, msg: 上传失败, data: null }
     */
    @Override
    public CommonResult<?> updateImgFile(MultipartFile file) {
        try {
            // 获取文件的sha256值
            String fileSha256 = DigestUtils.sha256Hex(file.getInputStream());
            String tempFileName = file.getOriginalFilename();
            if (null == tempFileName) {
                String msg = CommonServiceMsg.FILE_PARSE_ERROR.getMsg("文件名为空");
                log.warn(msg);
                return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
            }
            // 获取文件后缀
            String subFileType = tempFileName.toLowerCase().substring(tempFileName.lastIndexOf("."));
            String newFileName = fileSha256 + subFileType;
            // 拼接文件地址
            String newFilePath = fileProperties.getSaveImgPath() + File.separator + newFileName;
            String tempFileUrl = fileProperties.getSaveImgUrl() + newFileName;

            // 新建的文件对象
            FileCache fileCache = new FileCache(null, tempFileUrl, fileSha256);
            // 插入失败，说明已经存在对应sha256的文件，直接获取对应文件的路径并返回
            if (!tryInsertFileCache(fileCache)) {
                Map<String, Object> tempSha256Map = FileCacheHelper.tempFileSha256Map(fileSha256);
                // 获取sha256对应的文件对象，并返回地址
                FileCache result = fileCacheMapper.selectPrimary(tempSha256Map);
                String msg = CommonServiceMsg.FILE_UPDATE_SUCCESS.getMsg(result.getFilePath(), fileSha256);
                log.debug(msg);
                return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, result);
            }

            // 检查路径是否存在
            File saveFilePath = new File(fileProperties.getSaveImgPath());
            if (!saveFilePath.isDirectory() && !saveFilePath.mkdirs()) {
                String msg = CommonServiceMsg.FILE_PATH_ERROR.getMsg(fileProperties.getSaveImgPath());
                log.warn(msg);
                // 尝试删除缓存
                deleteFileCacheAsync(fileSha256);
                return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
            }

            File newFile = new File(newFilePath);
            try {
                // 保存到文件
                file.transferTo(newFile);
                String msg = CommonServiceMsg.FILE_UPDATE_SUCCESS.getMsg(tempFileUrl, fileSha256);
                log.debug(msg);
                return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, fileCache);
            } catch (IOException | IllegalStateException e) {
                String msg = CommonServiceMsg.FILE_UPDATE_FAILED.getMsg(newFilePath);
                log.warn(msg);
                log.error(e.getMessage());
                // 异步清理数据库的数据
                deleteFileCacheAsync(fileSha256);
                return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
            }
        } catch (ProjectException e) {
            return e.getResult();
        } catch (IOException e) {
            String msg = CommonServiceMsg.FILE_PARSE_FAILED.getMsg();
            log.warn(msg);
            log.error(e.getMessage());
            return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
        }
    }

    /**
     * 根据模板文件，生成最终的htm文件
     *
     * @param map 需要插入的数据
     * @return 生成模板文件
     */
    @Override
    public CommonResult<?> generalTemplateFile(Map<String, Object> map) {
        if (null == map || !isValidTemplateParams(map)) {
            String msg = CommonServiceMsg.TEMPLATE_FILE_PARAM_ERROR.getMsg(map);
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.DATA_EXCEPTION.getCode(), msg);
        }

        // 使用订单号来作为文件名
        String reservationCode = String.valueOf(map.get("reservationCode"));
        // 拼接保存的文件名
        String newFileName = reservationCode + MEDICAL_SURNAME;
        // 拼接临时文件名
        String tempFile = reservationCode + ".htm";
        // 最终保存的文件路径
        String newFilePath = fileProperties.getSaveMedicalPath() + File.separator + newFileName;
        // 临时转换的文件
        String tempFilePath = fileProperties.getSaveTempMedicalPath() + File.separator + tempFile;
        // 最终对应的文件地址
        String newFileUrl = fileProperties.getSaveMedicalUrl() + newFileName;

        // 保存病例的文件
        File filePath = new File(fileProperties.getSaveMedicalPath());
        if (!filePath.isDirectory() && !filePath.mkdirs()) {
            String msg = CommonServiceMsg.FILE_PATH_ERROR.getMsg(fileProperties.getSaveMedicalPath());
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
        }

        // 用订单号来作为病例文件名
        if (StringUtils.isEmpty(reservationCode)) {
            String msg = CommonServiceMsg.TEMPLATE_FILE_PARAM_ERROR.getMsg(map);
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
        }

        log.debug("病例输出 > {}", newFilePath);
        try {
            // 尝试更新订单状态，从已支付状态转换到已处理状态
            Map<String, Object> userReservationMap = UserReservationHelper.tempUserReservationId(reservationCode);
            UserReservationHelper.tempUserReservationStatusMap(userReservationMap, ReservationStatus.PAID.get());

            Map<String, Object> newUserReservationStatusMap = UserReservationHelper.tempUserReservationStatusMap(
                    null, ReservationStatus.PROCESSED.get());

            // 判断更新的结果
            int update = userReservationMapper.update(userReservationMap, newUserReservationStatusMap);

            if (0 == update) {
                String msg = CommonServiceMsg.UPDATE_FAILED.getMsg(
                        "病例", userReservationMap, newUserReservationStatusMap);
                log.warn(msg);
                return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
            }

            // 读取整个模板
            String buffer = IOUtils.toString(
                    resourceLoader.getResource(fileProperties.getMedicalTemplate()).getInputStream(),
                    StandardCharsets.UTF_8);

            for (String key : map.keySet()) {
                Pattern pattern = FileCacheHelper.getStringPattern(key);
                Matcher matcher = pattern.matcher(buffer);
                Object value = map.get(key);
                if (value instanceof String || value instanceof LocalDate) {
                    buffer = matcher.replaceAll(String.valueOf(value));
                }else if (value instanceof Map) {
                    try {
                        Map<String, String> temp = (Map<String, String>) value;
                        buffer = matcher.replaceAll(FileCacheHelper.generateMap(temp));
                    } catch (Exception e) {
                        String msg = CommonServiceMsg.TEMPLATE_FILE_PARAM_ERROR.getMsg(map);
                        log.warn(msg);
                        log.error(e.getMessage());
                        return new CommonResult<>(ExceptionCode.DATA_EXCEPTION.getCode(), msg);
                    }
                }
            }
            log.debug("测试输出 > {}", buffer);

            File newFile = new File(newFilePath);
            FileUtils.write(newFile, buffer, StandardCharsets.UTF_8);

            TempUserMedicalHistory history = new TempUserMedicalHistory();
            // 查询用户
            UserInfo userInfo = userInfoMapper.selectPrimary(
                    UserInfoHelper.tempUsernameMap(String.valueOf(map.get("name"))));
            history.setUserId(userInfo.getId());

            // 查询医生
            DoctorInfo doctorInfo = doctorInfoMapper.selectPrimary(
                    DoctorInfoHelper.tempDoctorNameMap(String.valueOf(map.get("doctorName"))));
            history.setDoctorId(doctorInfo.getId());

            // 时间
            Object medicalDate = map.get("medicalDate");
            LocalDate date;
            if (medicalDate instanceof String) {
                date = LocalDate.parse(String.valueOf(medicalDate));
            } else {
                date = (LocalDate) medicalDate;
            }
            history.setMedicalDate(date);
            history.setMedicalHistoryUri(newFileUrl);

            int result = userMedicalHistoryMapper.insert(Collections.singletonList(history));

            String msg;
            if (1 == result) {
                // 返回结果
                msg = CommonServiceMsg.TEMPLATE_UPDATE_SUCCESS.getMsg(newFileName);
                log.debug(msg);
            } else {
                msg = "病例已存在 > " + history;
                log.warn(msg);
            }
            return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, history);
        } catch (IOException e) {
            String msg = CommonServiceMsg.TEMPLATE_FILE_NO_FOUND.getMsg(fileProperties.getMedicalTemplate());
            log.warn(msg);
            log.error(e.getMessage());
            return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
        } catch (ProjectException e) {
            return e.getResult();
        }
    }

    /**
     * 预览订单文件的方法
     *
     * @param url 预约订单号
     * @param response        响应对象
     * @return 将病例文件输出到响应体中
     */
    @Override
    public CommonResult<?> previewTemplateFile(String url, HttpServletResponse response) {
        if (StringUtils.isEmpty(url)) {
            String msg = CommonServiceMsg.TEMPLATE_FILE_PARAM_ERROR.getMsg(
                    "缺少必要参数 reservationCode" + url);
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
        }

        // 获取对应文件名
        String templateName = FileCacheHelper.getTemplateNameByUrl(url);
        String tempFileName = fileProperties.getSaveMedicalPath() + File.separator + templateName;

        // 设置响应为html
        response.setContentType("text/html;charset=utf-8");

        try (final ServletOutputStream outputStream = response.getOutputStream()) {
            // 读取文件内容到输出流
            FileUtils.copyFile(new File(tempFileName), outputStream);

            String msg = CommonServiceMsg.TEMPLATE_PREVIEW_SUCCESS.getMsg(url);
            log.debug(msg);
            return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, tempFileName);
        } catch (IOException e) {
            String msg = CommonServiceMsg.TEMPLATE_FILE_NO_FOUND.getMsg(tempFileName);
            log.warn(msg);
            log.error(e.getMessage());
            return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
        }
    }

    /**
     * 尝试插入新的 FileCache 对象，根据结果，可以判断是否已经存在对应的文件
     *
     * @param fileCache 插入的FileCache对象
     * @return 插入是否成功
     */
    private boolean tryInsertFileCache(FileCache fileCache) throws ProjectException {
        int result = fileCacheMapper.insert(Collections.singletonList(fileCache));
        return result == 1;
    }

    /**
     * 如果创建文件失败，之后异步删除文件，尽可能不去影响返回数据
     *
     * @param sha256 需要删除的字段
     */
    @Async
    void deleteFileCacheAsync(String sha256) {
        try {
            Map<String, Object> tempMap = FileCacheHelper.tempFileSha256Map(sha256);
            int delete = fileCacheMapper.delete(tempMap);
            if (delete > 0) {
                log.debug("异步清理文件缓冲[file_cache]成功 {} > {}", tempMap, delete);
            } else {
                log.error("异步清理文件缓冲[file_cache]失败 > {}", tempMap);
            }
        } catch (ProjectException e) {
            log.error(e.getMsg());
        }
    }

    /**
     * 检查模板文件的相关参数是否正确，是否缺少某个属性
     *
     * @param map 模板的参数
     * @return 是否合法
     */
    private boolean isValidTemplateParams(Map<String, Object> map) {
        if (!map.containsKey("medicalDate")) {
            // 添加当前时间
            map.put("medicalDate", LocalDate.now());
        }
        return map.containsKey("p") && map.containsKey("reservationCode");
    }
}
