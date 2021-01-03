package com.tanhua.sso.controller;

import com.tanhua.commons.service.PictureUploadService;
import com.tanhua.commons.vo.ErrorResult;
import com.tanhua.commons.vo.PictureUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> upload(MultipartFile multipartFile) {
        Boolean result = pictureUploadService.upload(multipartFile);
        if (result) {
            return ResponseEntity.ok(null);
        }
        ErrorResult errorResult = new ErrorResult();
        errorResult.setErrCode("003");
        errorResult.setErrMessage("图片上传失败");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }
}
