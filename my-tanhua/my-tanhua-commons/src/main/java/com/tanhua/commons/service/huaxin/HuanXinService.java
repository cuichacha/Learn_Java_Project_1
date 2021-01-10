package com.tanhua.commons.service.huaxin;

public interface HuanXinService {
    public abstract Boolean registerUser(Long userId);

    public abstract Boolean addFriend(Long loginUserId, Long friendUserId);
}
