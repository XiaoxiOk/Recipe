package com.xi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xi.model.vo.FollowUserVO;
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
public interface FollowMapper extends BaseMapper<Follow> {

    @Select("SELECT A.to_uid userId, A.mutual mutual , B.user_name userName, B.profile_photo profilePhoto \n" +
            "FROM `follow` A LEFT JOIN `user` B ON A.to_uid = B.id\n" +
            "where A.from_uid = #{fromUid}")
    IPage<FollowUserVO> getMyFollowsByPage(Page<?> page, @Param("fromUid") int fromUid);


    @Select("SELECT A.from_uid userId, IF(A.mutual = 1 ,TRUE,FALSE) AS mutual , B.user_name userName, B.profile_photo profilePhoto \n" +
            "FROM `follow` A LEFT JOIN `user` B ON A.from_uid = B.id\n" +
            "where A.to_uid = #{toUid}")
    IPage<FollowUserVO> getMyFansByPage(Page<?> page, @Param("toUid") int toUid);

    @Select("SELECT COUNT(id) FROM `follow` WHERE from_uid = #{myId}")
    int getMyFollowsCounts(@Param("myId") int myId);
    @Select("SELECT COUNT(id) FROM `follow` WHERE to_uid = #{myId}")
    int getMyFansCounts(@Param("myId") int myId);
}
