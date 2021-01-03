package com.tanhua.sso.controller;

import com.tanhua.commons.service.PictureUploadService;
import com.tanhua.commons.vo.PictureUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class PictureUploadController {
    
    @Autowired
    private PictureUploadService pictureUploadService;

    @PostMapping("/loginReginfo/head")
    public PictureUploadResult upload(MultipartFile multipartFile) {
        Boolean result = pictureUploadService.upload(multipartFile);

        return null;
    }
}
