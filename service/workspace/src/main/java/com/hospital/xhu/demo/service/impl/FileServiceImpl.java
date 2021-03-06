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
     * ????????????
     *
     * @param file ???????????????
     * @return ???????????????
     * - ??????
     * { code: 200, msg: ????????????, data: ????????????????????? }
     * - ??????
     * { code: ExceptionCode, msg: ????????????, data: null }
     */
    @Override
    public CommonResult<?> updateImgFile(MultipartFile file) {
        try {
            // ???????????????sha256???
            String fileSha256 = DigestUtils.sha256Hex(file.getInputStream());
            String tempFileName = file.getOriginalFilename();
            if (null == tempFileName) {
                String msg = CommonServiceMsg.FILE_PARSE_ERROR.getMsg("???????????????");
                log.warn(msg);
                return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
            }
            // ??????????????????
            String subFileType = tempFileName.toLowerCase().substring(tempFileName.lastIndexOf("."));
            String newFileName = fileSha256 + subFileType;
            // ??????????????????
            String newFilePath = fileProperties.getSaveImgPath() + File.separator + newFileName;
            String tempFileUrl = fileProperties.getSaveImgUrl() + newFileName;

            // ?????????????????????
            FileCache fileCache = new FileCache(null, tempFileUrl, fileSha256);
            // ???????????????????????????????????????sha256??????????????????????????????????????????????????????
            if (!tryInsertFileCache(fileCache)) {
                Map<String, Object> tempSha256Map = FileCacheHelper.tempFileSha256Map(fileSha256);
                // ??????sha256???????????????????????????????????????
                FileCache result = fileCacheMapper.selectPrimary(tempSha256Map);
                String msg = CommonServiceMsg.FILE_UPDATE_SUCCESS.getMsg(result.getFilePath(), fileSha256);
                log.debug(msg);
                return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, result);
            }

            // ????????????????????????
            File saveFilePath = new File(fileProperties.getSaveImgPath());
            if (!saveFilePath.isDirectory() && !saveFilePath.mkdirs()) {
                String msg = CommonServiceMsg.FILE_PATH_ERROR.getMsg(fileProperties.getSaveImgPath());
                log.warn(msg);
                // ??????????????????
                deleteFileCacheAsync(fileSha256);
                return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
            }

            File newFile = new File(newFilePath);
            try {
                // ???????????????
                file.transferTo(newFile);
                String msg = CommonServiceMsg.FILE_UPDATE_SUCCESS.getMsg(tempFileUrl, fileSha256);
                log.debug(msg);
                return new CommonResult<>(CommonCode.SUCCESS.getCode(), msg, fileCache);
            } catch (IOException | IllegalStateException e) {
                String msg = CommonServiceMsg.FILE_UPDATE_FAILED.getMsg(newFilePath);
                log.warn(msg);
                log.error(e.getMessage());
                // ??????????????????????????????
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
     * ????????????????????????????????????htm??????
     *
     * @param map ?????????????????????
     * @return ??????????????????
     */
    @Override
    public CommonResult<?> generalTemplateFile(Map<String, Object> map) {
        if (null == map || !isValidTemplateParams(map)) {
            String msg = CommonServiceMsg.TEMPLATE_FILE_PARAM_ERROR.getMsg(map);
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.DATA_EXCEPTION.getCode(), msg);
        }

        // ?????????????????????????????????
        String reservationCode = String.valueOf(map.get("reservationCode"));
        // ????????????????????????
        String newFileName = reservationCode + MEDICAL_SURNAME;
        // ?????????????????????
        String tempFile = reservationCode + ".htm";
        // ???????????????????????????
        String newFilePath = fileProperties.getSaveMedicalPath() + File.separator + newFileName;
        // ?????????????????????
        String tempFilePath = fileProperties.getSaveTempMedicalPath() + File.separator + tempFile;
        // ???????????????????????????
        String newFileUrl = fileProperties.getSaveMedicalUrl() + newFileName;

        // ?????????????????????
        File filePath = new File(fileProperties.getSaveMedicalPath());
        if (!filePath.isDirectory() && !filePath.mkdirs()) {
            String msg = CommonServiceMsg.FILE_PATH_ERROR.getMsg(fileProperties.getSaveMedicalPath());
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
        }

        // ????????????????????????????????????
        if (StringUtils.isEmpty(reservationCode)) {
            String msg = CommonServiceMsg.TEMPLATE_FILE_PARAM_ERROR.getMsg(map);
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
        }

        log.debug("???????????? > {}", newFilePath);
        try {
            // ?????????????????????????????????????????????????????????????????????
            Map<String, Object> userReservationMap = UserReservationHelper.tempUserReservationId(reservationCode);
            UserReservationHelper.tempUserReservationStatusMap(userReservationMap, ReservationStatus.PAID.get());

            Map<String, Object> newUserReservationStatusMap = UserReservationHelper.tempUserReservationStatusMap(
                    null, ReservationStatus.PROCESSED.get());

            // ?????????????????????
            int update = userReservationMapper.update(userReservationMap, newUserReservationStatusMap);

            if (0 == update) {
                String msg = CommonServiceMsg.UPDATE_FAILED.getMsg(
                        "??????", userReservationMap, newUserReservationStatusMap);
                log.warn(msg);
                return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
            }

            // ??????????????????
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
            log.debug("???????????? > {}", buffer);

            File newFile = new File(newFilePath);
            FileUtils.write(newFile, buffer, StandardCharsets.UTF_8);

            TempUserMedicalHistory history = new TempUserMedicalHistory();
            // ????????????
            UserInfo userInfo = userInfoMapper.selectPrimary(
                    UserInfoHelper.tempUsernameMap(String.valueOf(map.get("name"))));
            history.setUserId(userInfo.getId());

            // ????????????
            DoctorInfo doctorInfo = doctorInfoMapper.selectPrimary(
                    DoctorInfoHelper.tempDoctorNameMap(String.valueOf(map.get("doctorName"))));
            history.setDoctorId(doctorInfo.getId());

            // ??????
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
                // ????????????
                msg = CommonServiceMsg.TEMPLATE_UPDATE_SUCCESS.getMsg(newFileName);
                log.debug(msg);
            } else {
                msg = "??????????????? > " + history;
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
     * ???????????????????????????
     *
     * @param url ???????????????
     * @param response        ????????????
     * @return ????????????????????????????????????
     */
    @Override
    public CommonResult<?> previewTemplateFile(String url, HttpServletResponse response) {
        if (StringUtils.isEmpty(url)) {
            String msg = CommonServiceMsg.TEMPLATE_FILE_PARAM_ERROR.getMsg(
                    "?????????????????? reservationCode" + url);
            log.warn(msg);
            return new CommonResult<>(ExceptionCode.FILE_EXCEPTION.getCode(), msg);
        }

        // ?????????????????????
        String templateName = FileCacheHelper.getTemplateNameByUrl(url);
        String tempFileName = fileProperties.getSaveMedicalPath() + File.separator + templateName;

        // ???????????????html
        response.setContentType("text/html;charset=utf-8");

        try (final ServletOutputStream outputStream = response.getOutputStream()) {
            // ??????????????????????????????
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
     * ?????????????????? FileCache ?????????????????????????????????????????????????????????????????????
     *
     * @param fileCache ?????????FileCache??????
     * @return ??????????????????
     */
    private boolean tryInsertFileCache(FileCache fileCache) throws ProjectException {
        int result = fileCacheMapper.insert(Collections.singletonList(fileCache));
        return result == 1;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param sha256 ?????????????????????
     */
    @Async
    void deleteFileCacheAsync(String sha256) {
        try {
            Map<String, Object> tempMap = FileCacheHelper.tempFileSha256Map(sha256);
            int delete = fileCacheMapper.delete(tempMap);
            if (delete > 0) {
                log.debug("????????????????????????[file_cache]?????? {} > {}", tempMap, delete);
            } else {
                log.error("????????????????????????[file_cache]?????? > {}", tempMap);
            }
        } catch (ProjectException e) {
            log.error(e.getMsg());
        }
    }

    /**
     * ????????????????????????????????????????????????????????????????????????
     *
     * @param map ???????????????
     * @return ????????????
     */
    private boolean isValidTemplateParams(Map<String, Object> map) {
        if (!map.containsKey("medicalDate")) {
            // ??????????????????
            map.put("medicalDate", LocalDate.now());
        }
        return map.containsKey("p") && map.containsKey("reservationCode");
    }
}
