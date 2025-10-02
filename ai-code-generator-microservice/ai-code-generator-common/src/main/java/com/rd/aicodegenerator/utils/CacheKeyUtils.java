package com.rd.aicodegenerator.utils;

import cn.hutool.json.JSONUtil;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 缓存 key 工具类
 */
public class CacheKeyUtils {

    /**
     * 获取缓存 key
     *
     * @param object 对象
     * @return 缓存 key
     */
    public static String generateKey(Object object) {
        if (object == null) {
            return DigestUtils.md5Hex("null");
        }
        // 先转 JSON，再 MD5
        String jsonStr = JSONUtil.toJsonStr(object);
        return DigestUtils.md5Hex(jsonStr);
    }
}
