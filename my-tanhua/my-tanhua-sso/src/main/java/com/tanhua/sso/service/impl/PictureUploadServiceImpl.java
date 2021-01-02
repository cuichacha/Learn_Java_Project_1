package com.tanhua.sso.service.impl;

import com.aliyun.oss.OSSClient;

import com.tanhua.commons.vo.PictureUploadResult;
import com.tanhua.sso.config.AliyunConfig;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tanhua.commons.service.PictureUploadService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class PictureUploadServiceImpl implements PictureUploadService {

    @Autowired
    private AliyunConfig aliyunConfig;

    @Autowired
    private OSSClient ossClient;

    @Override
    public PictureUploadResult upload(MultipartFile multipartFile) {

        PictureUploadResult pictureUploadResult = new PictureUploadResult();

        String filename = multipartFile.getOriginalFilename();
        String filePath = getFilePath(filename);

        try {
            ossClient.putObject(aliyunConfig.getBucketName(), filePath, new ByteArrayInputStream(multipartFile.getBytes()));

        } catch (IOException e) {
            e.printStackTrace();
            pictureUploadResult.setStatus("error");
            return pictureUploadResult;
        }

        pictureUploadResult.setName(aliyunConfig.getUrlPrefix() + filename);
        pictureUploadResult.setStatus("done");
        pictureUploadResult.setUid(String.valueOf(System.currentTimeMillis()));
        return pictureUploadResult;
    }

    private String getFilePath(String fileName) {
        DateTime dateTime = new DateTime();
        //images/yyyy/MM/dd/xxxxxxx.jpg
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." +
                StringUtils.substringAfterLast(fileName, ".");
    }
}
