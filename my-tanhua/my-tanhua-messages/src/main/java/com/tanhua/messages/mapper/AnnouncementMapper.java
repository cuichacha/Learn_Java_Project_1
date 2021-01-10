package com.tanhua.messages.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.commons.pojo.messages.MessageAnnouncement;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementMapper extends BaseMapper<MessageAnnouncement> {
}
