package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.enums.CommentTypeEnum;
import com.xi.common.utils.BaseInfoProperties;
import com.xi.mapper.ReplyMapper;
import com.xi.model.bo.ReplyBO;
import com.xi.model.pojo.Comment;
import com.xi.model.pojo.Reply;
import com.xi.model.vo.ReplyVO;
import com.xi.service.ReplyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Service
public class ReplyServiceImpl extends BaseInfoProperties implements ReplyService {

    @Resource
    private ReplyMapper replyMapper;


    @Override
    public IPage<ReplyVO> getReplyListByPage(Page<?> page, int commentId) {
        return replyMapper.getReplyListByPage(page, commentId);
    }
    /**
     * 新增回复信息
     * @param reply 回复信息
     * @return 新增对象（id已自动填充）
     */
    @Override
    public Reply insertReply(Reply reply) {
        int count = replyMapper.insert(reply);
        if(count == 1){
            return reply;
        }
        return null;
    }

    /**
     * 删除回复信息
     * @param replyId 回复信息
     * @return 影响条数
     */
    @Override
    public int deleteReplyById(int replyId) {
        return replyMapper.deleteById(replyId);
    }

    public Reply getReplyById(int id){
        return replyMapper.selectById(id);
    }

    /**
     * 获取指定评论的回复数
     * @param commentId 评论ID
     * @return 回复总数
     */
    public int getRepliedCounts(int commentId){
        String countsStr = redis.get(REDIS_COMMENT_BE_REPLIED_COUNTS + ":" + commentId);;

        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }

        return Integer.valueOf(countsStr);
    }

}
