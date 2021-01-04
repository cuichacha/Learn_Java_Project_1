package com.tanhua.commons.service.sso;

import org.springframework.web.multipart.MultipartFile;

public interface PictureUploadService {
    public abstract String upload(String token, MultipartFile multipartFile);
}
