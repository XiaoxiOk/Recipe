package com.xi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.bo.ReplyBO;
import com.xi.model.pojo.Reply;
import com.xi.model.vo.ReplyVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface ReplyService{
    Reply insertReply(Reply reply);
    Reply getReplyById(int id);
    int deleteReplyById(int replyId);
    int getRepliedCounts(int commentId);
    IPage<ReplyVO> getReplyListByPage(Page<?> page, int commentId);
}
