package com.tanhua.sso.controller;

import com.tanhua.commons.service.LoginService;
import com.tanhua.commons.service.PictureUploadService;
import com.tanhua.commons.vo.ErrorResult;
import com.tanhua.commons.vo.PictureUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@RestController
@RequestMapping("/user")
public class PictureUploadController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private PictureUploadService pictureUploadService;

    @PostMapping("/loginReginfo/head")
    public ResponseEntity<Object> saveAvatar(@RequestParam("headPhoto") MultipartFile multipartFile,
                                             @RequestHeader("Authorization") String token) {

        String avatarUrl = pictureUploadService.upload(token, multipartFile);
        if (avatarUrl == null) {
            ErrorResult errorResult = new ErrorResult();
            errorResult.setErrCode("004");
            errorResult.setErrMessage("图片上传失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }
        return loginService.addUserAvatar(token, avatarUrl);
    }
}
