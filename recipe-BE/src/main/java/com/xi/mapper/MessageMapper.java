package com.xi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xi.model.vo.MessageVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Repository
public interface MessageMapper extends BaseMapper<Message> {

    @Select({"<script>",
            "SELECT msg.*, u.user_name fromUserName, u.profile_photo fromUserPhoto",
            "FROM (SELECT m.id msgId, m.from_uid fromUid, m.type_id typeId, m.is_read readMsg, m.create_time createTime FROM `message` m WHERE m.type = #{msgType} AND m.to_uid = #{toUid}) msg",
            "LEFT JOIN `user` u ON msg.fromUid = u.id",
            "ORDER BY msg.readMsg ASC, msg.createTime DESC",
            "</script>"})
    IPage<Map<String, Object>> getFollowMsgList(Page<?> page, @Param("msgType") int msgType, @Param("toUid") int toUid);

    @Select({"<script>",
            "SELECT m.id msgId, m.content content, m.from_uid fromUid, m.type type,m.type_id typeId, m.create_time createTime, m.is_read readMsg, u.user_name fromUserName, u.profile_photo fromUserPhoto",
            "FROM `message` m ",
            "LEFT JOIN `user` u ON m.from_uid = u.id",
            "WHERE",
            " m.type IN" ,
            "<foreach item='msgType'  collection='msgTypeList'  open='(' separator=',' close=')'>",
            "#{msgType}",
            "</foreach>",
            "<if test='toUid!=-1'>",
            "AND m.to_uid = #{toUid}",
            "</if>",
            "ORDER BY m.is_read ASC, m.create_time DESC",
            "</script>"})
    IPage<Map<String, Object>> getLikeOrCommentMsgList(Page<?> page, @Param("msgTypeList") List<Integer> msgTypeList, @Param("toUid") int toUid);



}
