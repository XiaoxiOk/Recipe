package com.xi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.UserShare;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xi.model.vo.UserShareVO;
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
public interface UserShareMapper extends BaseMapper<UserShare> {

    @Select({"<script>",
            "SELECT us.*, u.user_name userName, u.profile_photo profilePhoto",
            "FROM user_share us",
            "LEFT JOIN `user` u ON us.uid = u.id",
            "<where>",
            "<if test='userShareId!=-1'>",
            "AND us.id = #{userShareId}",
            "</if>",
            "</where>",
            "</script>"})
    UserShareVO queryUserShareVOById(@Param("userShareId") int userShareId);

    @Select({"<script>",
            "SELECT us.*, u.user_name userName, u.profile_photo profilePhoto",
            "FROM ( SELECT * FROM user_share where uid = #{myId}) us LEFT JOIN `user` u ON us.uid = u.id",
            "ORDER BY us.create_time",
            "DESC",
            "</script>"})
    IPage<UserShareVO> getMySharesByPage(Page<?> page, @Param("myId") int myId);

    @Select({"<script>",
            "SELECT us.*, u.user_name userName, u.profile_photo profilePhoto",
            "FROM user_share us,`user` u",
            "<where>",
            "us.uid = u.id",
            "<if test='fanId!=-1'>",
            "AND u.id IN (SELECT f.to_uid  FROM follow f WHERE f.from_uid = #{fanId})",
            "</if>",
            "</where>",
            "ORDER BY us.create_time",
            "DESC",
            "</script>"})
    IPage<UserShareVO> queryFollowSharesByPage(Page<?> page, @Param("fanId") int fanId);

}
