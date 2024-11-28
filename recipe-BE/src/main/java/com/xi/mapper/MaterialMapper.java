package com.xi.mapper;

import com.xi.model.pojo.Material;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xi.model.pojo.Material;
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
public interface MaterialMapper extends BaseMapper<Material> {
    @Select("SELECT * FROM material WHERE recipe_id = #{recipeId}")
    public List<Material> getMaterialsByRecipeId(int recipeId);

    @Insert({"<script>",
            "INSERT INTO material(id, recipe_id, food_name, consumption)",
            "VALUES",
            "<foreach collection =\"materialList\" item=\"material\" separator =\",\">",
            "(#{material.id}, #{material.recipeId}, #{material.foodName}, #{material.consumption})",
            "</foreach>",
            "on duplicate key update",
            "id = VALUES(id), recipe_id = VALUES(recipe_id),food_name = VALUES(food_name), consumption = VALUES(consumption)",
            "</script>"})
    public int insertOrUpdateMaterialList(@Param("materialList") List<Material> materialList);

    @Delete({"<script>",
            "delete from material where id in ",
            "<foreach collection=\"ids\" item=\"id\" separator=\",\" open=\"(\" close=\")\">\n" +
                    "#{id}",
            "</foreach>",
            "</script>"})
    public int delMaterialByIds(@Param("ids") List<Integer> ids);

}
