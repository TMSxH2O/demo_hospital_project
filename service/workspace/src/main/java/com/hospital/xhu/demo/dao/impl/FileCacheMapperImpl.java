package com.hospital.xhu.demo.dao.impl;

import com.hospital.xhu.demo.dao.IFileCacheMapper;
import com.hospital.xhu.demo.dao.general.impl.GeneralMapperImpl;
import com.hospital.xhu.demo.entity.FileCache;
import com.hospital.xhu.demo.utils.enumerate.ExceptionCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/3
 */
@Repository("fileCacheMapperImpl")
public class FileCacheMapperImpl extends GeneralMapperImpl<FileCache> {

    private static final Map<String, String> FILE_CACHE_MAP = new HashMap<>();

    static {
        FILE_CACHE_MAP.put("id", "id");
        FILE_CACHE_MAP.put("filePath", "file_path");
        FILE_CACHE_MAP.put("fileSha256", "file_sha256");
    }

    public FileCacheMapperImpl(
            @Qualifier("fileCacheMapper") IFileCacheMapper fileCacheMapper) {
        super(fileCacheMapper);
    }

    @Override
    protected String getSqlName() {
        return "file_cache";
    }

    @Override
    protected Map<String, String> getMap() {
        return FILE_CACHE_MAP;
    }

    @Override
    protected ExceptionCode getExceptionCode() {
        return ExceptionCode.FILE_EXCEPTION;
    }
}
