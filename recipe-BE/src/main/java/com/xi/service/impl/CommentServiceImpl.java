package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.enums.CommentTypeEnum;
import com.xi.common.utils.BaseInfoProperties;
import com.xi.mapper.CommentMapper;
import com.xi.model.bo.CommentBO;
import com.xi.model.pojo.Comment;
import com.xi.model.vo.CommentVO;
import com.xi.service.CommentService;
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
public class CommentServiceImpl extends BaseInfoProperties implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    /**
     * 分页获取评论对象
     * @param page 页面对象
     * @param type 评论对象类型
     * @param typeId 评论对象ID
     * @return 评论列表
     */
    @Override
    public IPage<CommentVO> getCommentListByPage(Page<?> page, int type, int typeId) {

        return commentMapper.getCommentListByPage(page, type, typeId);
    }


    /**
     * 新增评论信息
     * @param comment 评论信息
     * @return 评论对象
     */
    @Override
    public Comment insertComment(Comment comment) {
        int count = commentMapper.insert(comment);
        if(count == 1){
            return comment;
        }
        return null;
    }


    /**
     * 删除评论信息
     * @param commentId 评论信息ID
     * @return 影响条数
     */
    @Override
    public int deleteCommentById(int commentId) {
        return commentMapper.deleteById(commentId);
    }

    public Comment getCommentById(int id){
        return commentMapper.selectById(id);
    }

    /**
     * 获取总的评论数
     * @param type 被评论对象类型
     * @param typeId 被评论对象ID
     * @return 数量0+
     */
    @Override
    public Integer getCommentCounts(int type, int typeId) {
        String countsStr = null;
        CommentTypeEnum checkTypeName = CommentTypeEnum.getByCode(type);
        switch (Objects.requireNonNull(checkTypeName)) {
            case RECIPE:
                countsStr = redis.get(REDIS_RECIPE_COMMENT_COUNTS + ":" + typeId);
                break;
            case USER_SHARE:
                countsStr = redis.get(REDIS_USER_SHARE_COMMENT_COUNTS + ":" + typeId);
                break;
            default: break;
        }

        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }

        return Integer.valueOf(countsStr);
    }


}
