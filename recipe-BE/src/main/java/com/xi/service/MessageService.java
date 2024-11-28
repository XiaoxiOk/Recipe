package com.xi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.Message;
import com.xi.model.vo.MessageVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface MessageService {
    Message insertMessage(Message message);
    IPage<Map<String, Object>> getFollowMessage(Page<?> page, int userId);
    IPage<Map<String, Object>> getLikeMessage(Page<?> page, int userId);
    IPage<Map<String, Object>> getCommentMessage(Page<?> page, int userId);
    int deleteMsgById(int msgId);
}
