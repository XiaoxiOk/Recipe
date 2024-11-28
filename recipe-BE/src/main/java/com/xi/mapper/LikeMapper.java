package com.xi.mapper;

import com.xi.model.pojo.Like;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Repository
public interface LikeMapper extends BaseMapper<Like> {
    @Select("select type_id typeId from `like` where user_id = #{userId}  and type = #{type}")
    List<Integer> getRecipesByUserId(@Param("userId") int userId, @Param("type") int type);
}
