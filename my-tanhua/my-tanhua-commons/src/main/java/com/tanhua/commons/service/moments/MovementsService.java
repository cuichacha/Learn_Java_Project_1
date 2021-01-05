package com.tanhua.commons.service.moments;

import com.tanhua.commons.vo.moments.MovementsResult;
import org.springframework.web.multipart.MultipartFile;

public interface MovementsService {
    public abstract MovementsResult queryFriendsMovements(String token, Integer startPage, Integer pageSize);

    public abstract MovementsResult queryRecommendedMovements(String token, Integer startPage, Integer pageSize);

    public abstract Boolean publishMoment(String token, String textContent,
                                          String location, String latitude,
                                          String longitude, MultipartFile[] multipartFile);
}
