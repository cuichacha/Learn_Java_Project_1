package com.tanhua.commons.service;

import com.tanhua.commons.vo.PictureUploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface PictureUploadService {
    public abstract String upload(String token, MultipartFile multipartFile);
}
