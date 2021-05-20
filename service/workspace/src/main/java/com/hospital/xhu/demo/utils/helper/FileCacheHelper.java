package com.hospital.xhu.demo.utils.helper;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/3
 */
@Slf4j
public class FileCacheHelper {

    private static final String MAP_TEMPLATE = "<div class=\"left\">%s</div>\n<div class=\"right\">%s</div>";

    /**
     * 获取文件sha256对应的Map
     *
     * @param sha256 文件的sha256
     * @return 对应Map
     */
    public static Map<String, Object> tempFileSha256Map(String sha256) {
        return Collections.singletonMap("fileSha256", sha256);
    }

    /**
     * 生成匹配某个模板的Pattern对象
     *
     * @param key 需要匹配的key
     * @return 正则对象
     */
    public static Pattern getStringPattern(String key) {
        String pattern = String.format("(\\$\\{%s\\})", key);
        log.debug("生成正则匹配项 > {}", pattern);
        return Pattern.compile(pattern);
    }

    /**
     * 自动生成循环部分的模板
     *
     * @param map 插入的值
     * @return 循环部分的字符串
     */
    public static String generateMap(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            stringBuilder.append(String.format(MAP_TEMPLATE, stringStringEntry.getKey(), stringStringEntry.getValue()));
        }
        return stringBuilder.toString();
    }

    /**
     * 通过正则匹配，来获取文件名
     *
     * @param url        文件对应的url
     * @return 文件名
     */
    public static String getTemplateNameByUrl(String url) {
        return url.substring(url.lastIndexOf("/"));
    }
}
