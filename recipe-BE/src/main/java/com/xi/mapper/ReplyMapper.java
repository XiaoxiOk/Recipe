package com.xi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.Reply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xi.model.vo.ReplyVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Repository
public interface ReplyMapper extends BaseMapper<Reply> {

    @Select({"<script>",
            "SELECT r.*, u1.user_name fromUserName, u1.profile_photo profilePhoto, u2.user_name toUserName",
            "FROM reply r",
            "LEFT JOIN `user` u1 ON r.from_uid = u1.id",
            "LEFT JOIN `user` u2 ON r.to_uid = u2.id",
            "<where>",
            "<if test='commentId != -1'>",
            "AND r.comment_id = #{commentId}",
            "</if>",
            "</where>",
            "ORDER BY r.create_time ASC",
            "</script>"})
    IPage<ReplyVO> getReplyListByPage(Page<?> page, @Param("commentId") int commentId);
}
