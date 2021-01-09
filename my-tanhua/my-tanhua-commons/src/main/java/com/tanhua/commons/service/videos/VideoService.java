package com.tanhua.commons.service.videos;

import com.tanhua.commons.vo.videos.VideoResult;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    public abstract VideoResult queryVideoList(String token, Integer startPage, Integer pageSize);

    public abstract Boolean publishVideo(String token, MultipartFile thumbNail, MultipartFile videoFile);

    public abstract Boolean subscribe(String token, Long userId);

    public abstract Boolean unsubscribe(String token, Long userId);
}
