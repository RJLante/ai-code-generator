package com.rd.aicodegenerator.service;

import org.springframework.stereotype.Service;

/**
 * 截图服务
 */
@Service
public interface ScreenShotService {

    /**
     * 通用的截图服务，可以得到访问地址
     * @param url
     * @return
     */
    String generateAndUploadScreenshot(String url);

}
