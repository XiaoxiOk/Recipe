package com.xi.mapper;

import com.xi.model.pojo.Step;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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
public interface StepMapper extends BaseMapper<Step> {

    @Select("SELECT * FROM step WHERE recipe_id = #{recipeId}")
    public List<Step> getStepsByRecipeId(int recipeId);

    @Insert({"<script>",
            "INSERT INTO step(id, recipe_id, seq_number, description, img_url)",
            "VALUES",
            "<foreach collection =\"stepList\" item=\"step\" separator =\",\">",
            "(#{step.id}, #{step.recipeId}, #{step.seqNumber}, #{step.description}, #{step.imgUrl})",
            "</foreach>",
            "on duplicate key update",
            "id = VALUES(id), recipe_id = VALUES(recipe_id), seq_number = VALUES(seq_number), description = VALUES(description), img_url = VALUES(img_url)",
            "</script>"})
    public int insertOrUpdateStepList(@Param("stepList") List<Step> stepList);

    @Delete({"<script>",
            "delete from step where id in ",
            "<foreach collection=\"ids\" item=\"id\" separator=\",\" open=\"(\" close=\")\">\n" +
            "#{id}",
            "</foreach>",
            "</script>"})
    public int deleteStepByIds(@Param("ids") List<Integer> ids);
}
