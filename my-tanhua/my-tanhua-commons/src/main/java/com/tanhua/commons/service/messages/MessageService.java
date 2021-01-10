package com.tanhua.commons.service.messages;

import com.tanhua.commons.vo.messages.MessageResult;

public interface MessageService {
    public abstract Boolean addContact(String token, Long userId);

    public abstract MessageResult queryContactsLists(String token, Integer startPage, Integer pageSize, String keyword);

    public abstract MessageResult queryComments(String token, Integer startPage, Integer pageSize, Integer commentType);

    public abstract MessageResult queryAnnouncement(String token, Integer startPage, Integer pageSize);
}
