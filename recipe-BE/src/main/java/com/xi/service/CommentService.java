package com.xi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.bo.CommentBO;
import com.xi.model.pojo.Comment;
import com.xi.model.vo.CommentVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface CommentService{
   Comment insertComment(Comment comment);

   int deleteCommentById(int commentId);
   Comment getCommentById(int id);
   Integer getCommentCounts(int type, int typeId);
   IPage<CommentVO> getCommentListByPage(Page<?> page, int type, int typeId);

}
