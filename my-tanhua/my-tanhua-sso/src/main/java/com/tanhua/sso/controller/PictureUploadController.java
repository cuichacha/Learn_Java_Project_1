package com.tanhua.sso.controller;

import com.tanhua.sso.service.impl.PictureUploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class PictureUploadController {
    @Autowired
    private PictureUploadServiceImpl pictureUploadService;

    @PostMapping("/loginReginfo/head")
    public void saveAvatar(MultipartFile headPhoto) {
        pictureUploadService.upload(headPhoto);
    }
}
