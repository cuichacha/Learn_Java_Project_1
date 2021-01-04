package com.tanhua.sso.service.impl;

import com.aliyun.oss.OSSClient;
import com.tanhua.commons.config.AliyunConfig;
import com.tanhua.commons.service.sso.PictureUploadService;

//import com.tanhua.sso.config.AliyunConfig;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class PictureUploadServiceImpl implements PictureUploadService {

    @Autowired
    private AliyunConfig aliyunConfig;

    @Autowired
    private OSSClient ossClient;

    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg",
            ".jpeg", ".gif", ".png"};

    @Override
    public String upload(String token, MultipartFile multipartFile) {

        boolean isLegal = false;

        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(multipartFile.getOriginalFilename(),
                    type)) {
                isLegal = true;
                break;
            }
        }

        if (!isLegal) {
            return null;
        }

        String fileName = multipartFile.getOriginalFilename();
        String filePath = getFilePath(fileName);

        try {
            ossClient.putObject(aliyunConfig.getBucketName(), filePath, new ByteArrayInputStream(multipartFile.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return aliyunConfig.getPrefixUrl() + filePath;
    }

    private String getFilePath(String fileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." +
                StringUtils.substringAfterLast(fileName, ".");
    }
}
