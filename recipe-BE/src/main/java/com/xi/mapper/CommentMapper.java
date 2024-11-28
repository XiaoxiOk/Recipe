package com.xi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xi.model.vo.CommentVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    @Select({"<script>",
            "SELECT c.*, user_name userName, profile_photo profilePhoto",
            "FROM `comment` c LEFT JOIN `user` u ON(c.from_uid = u.id)",
            "<where>",
            "<if test='type!=-1'>",
            "AND topic_type = #{type}",
            "</if>",
            "<if test='typeId!=-1'>",
            "AND topic_id = #{typeId}",
            "</if>",
            "</where>",
            "</script>"})
    IPage<CommentVO> getCommentListByPage(Page<?> page, @Param("type") int type, @Param("typeId") int typeId);

}
