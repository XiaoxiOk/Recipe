package com.xi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.awt.geom.IllegalPathStateException;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Repository
public interface UserMapper extends BaseMapper<User> {


    @Select({"<script>",
            "SELECT * FROM `user` ",
            "<where>",
            "<if test='frozen!=-1'>",
            "AND frozen = #{frozen}",
            "</if>",
            "</where>",
            "</script>"})
    IPage<User> selectUsersByIsFrozen(Page<?> page, @Param("frozen") int frozen);


    @Select({
            "<script>",
            "SELECT u.* FROM `user` u",
            "<where>",
            "<if test='userName != null and userName != \"\"'>",
            "AND user_name LIKE \"%\"#{userName}\"%\" ",
            "</if>",
            "</where>",
            "</script>"})
    IPage<User> selectUsersByName(Page<?> page, @Param("userName") String userName);
}