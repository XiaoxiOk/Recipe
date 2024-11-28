package com.xi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.Recipe;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xi.model.vo.RecipeShowVO;
import com.xi.model.vo.RecipeVO;
import org.apache.ibatis.annotations.Delete;
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
public interface RecipeMapper extends BaseMapper<Recipe> {

    @Select("select id from recipe")
    List<Integer> selectIds();
    @Select({"<script>",
            "select * from recipe where id in ",
            "<foreach collection=\"idList\" item=\"id\" separator=\",\" open=\"(\" close=\")\">\n" +
                    "#{id}",
            "</foreach>",
            "and state = 0",
            "</script>"})
    List<Recipe> selectByIdList(@Param("idList") List<Integer> idList);

    @Select({"<script>",
            "SELECT * FROM recipe r" ,
            "<where>",
            "<if test='state!=-1'>",
            "AND r.state = #{state}",
            "</if>",
            "</where>",
            "</script>"})
    IPage<Recipe> selectListByPage(Page<?> page, @Param("state") int state);

    @Select({"<script>",
            "SELECT * FROM recipe r",
            "<where>",
            "<if test='idList != null and idList.size() > 0'>",
            " id not in",
            "<foreach collection=\"idList\" item=\"id\" separator=\",\" open=\"(\" close=\")\">\n" +
                    "#{id}",
            "</foreach>",
            "</if>",
            "</where>",
            "</script>"})
    IPage<Recipe> selectRecipeFilterByPage(Page<?> page, @Param("state") int state, @Param("idList") List<Integer> idList);

    @Select({"<script>",
            "SELECT * FROM recipe",
            "<where>",
            "<if test='name != null and name != \"\"'>",
            "AND recipe_name LIKE \"%\"#{name}\"%\" ",
            "</if>",
            "<if test='state!=-1'>",
            "AND state = #{state}",
            "</if>",
            "</where>",
            "</script>"})
    IPage<Recipe> searchRecipeByNameOrState(Page<?> page, @Param("name") String name, @Param("state") int state);


    @Select({"<script>",
            "SELECT r.*, u.user_name userName, u.profile_photo profilePhoto, ss.type_name typeName\n" +
                    "FROM recipe r, `user` u, second_sort ss",
            "WHERE r.user_id = u.id AND r.sec_sort_id = ss.id",
            "<if test='state!=-1'>",
            "AND r.state = #{state}",
            "</if>",
            "</script>"})
    IPage<RecipeVO> selectRecipeVOByPage(Page<?> page, @Param("state") int state);


    @Select({"<script>",
            "SELECT r.id, u.user_name userName, u.profile_photo profilePhoto, r.recipe_name recipeName, r.show_image showImage, r.media_url mediaUrl, r.detail, r.difficulty, r.state, r.create_time createTime",
            "FROM recipe r",
            "LEFT JOIN `like` ulr ON r.id = ulr.type_id",
            "LEFT JOIN `user` u ON ulr.user_id = u.id",
            "WHERE ulr.type = 1 AND r.state != -1 AND u.id = #{fromUserId}",
            "ORDER BY ulr.create_time",
            "DESC",
            "</script>"})
    IPage<RecipeShowVO> getLikeRecipeByUserId(Page<?> page, @Param("fromUserId") int fromUserId);

    @Select({"<script>",
            "SELECT r.id, u.user_name userName, u.profile_photo profilePhoto, r.recipe_name recipeName, r.show_image showImage,r.media_url mediaUrl, r.detail, r.difficulty, r.state, r.create_time createTime",
            "FROM recipe r",
            "LEFT JOIN `user` u ON r.user_id = u.id",
            "WHERE u.id = #{fromUserId}",
            "<if test='state!=-1'>",
            "AND state = #{state}",
            "</if>",
            "ORDER BY r.create_time",
            "DESC",
            "</script>"})
    IPage<RecipeShowVO>  getRecipeListByUserId(Page<?> page, @Param("fromUserId") int fromUserId,int state);


    @Select({"<script>",
            "SELECT r.*, u.user_name userName, u.profile_photo profilePhoto, ss.type_name typeName",
            "FROM recipe r, `user` u, second_sort ss",
            "WHERE r.id = #{recipeId} AND r.user_id = u.id AND r.sec_sort_id = ss.id",
            "</script>"})
    RecipeVO queryRecipeUserById(@Param("recipeId") int recipeId);


    @Select({"<script>",
            "SELECT r.id, u.user_name userName, u.profile_photo profilePhoto, r.recipe_name recipeName, r.show_image showImage, r.media_url mediaUrl, r.detail, r.difficulty, r.state, r.create_time createTime",
            "FROM recipe r LEFT JOIN `user` u ON r.user_id = u.id",
            "<where>",
            "<if test='name != null and name != \"\"'>",
            "AND recipe_name LIKE CONCAT('%',#{name},'%')",
            "</if>",
            "<if test='state!=-1'>",
            "AND state = #{state}",
            "</if>",
            "</where>",
            "</script>"})
    IPage<RecipeShowVO> showRecipeByNameOrState(Page<?> page, @Param("name") String name, @Param("state") int state);

    @Select({"<script>",
            "SELECT r.id, u.user_name userName, u.profile_photo profilePhoto, r.recipe_name recipeName, r.show_image showImage, r.media_url mediaUrl, r.detail, r.difficulty, r.state, r.create_time createTime",
            "FROM recipe r LEFT JOIN `user` u ON r.user_id = u.id",
            "<where>",
            "<if test='name != null and name != \"\"'>",
            "AND recipe_name LIKE CONCAT('%',#{name},'%')",
            "</if>",
            "<if test='secId!=-1'>",
            "AND sec_sort_id = #{secId}",
            "</if>",
            "<if test='state!=-1'>",
            "AND state = #{state}",
            "</if>",
            "</where>",
            "</script>"})
    IPage<RecipeShowVO> showRecipeByNameOrSecId(Page<?> page, @Param("name") String name, @Param("secId") int secId, @Param("state") int state);

    @Select({"<script>",
            "SELECT r.id, u.user_name userName, u.profile_photo profilePhoto, r.recipe_name recipeName, r.show_image showImage, r.media_url mediaUrl, r.detail, r.difficulty, r.state, r.create_time createTime",
            "FROM recipe r LEFT JOIN `user` u ON r.user_id = u.id",
            "WHERE state = 0",
            "ORDER BY createTime DESC",
            "</script>"})
    IPage<RecipeShowVO> getLatestRecipeList(Page<?> page);

    @Select({"<script>",
            "SELECT r.id, rt.scoreSum, u.user_name userName, u.profile_photo profilePhoto, r.recipe_name recipeName, r.show_image showImage, r.media_url mediaUrl, r.detail, r.difficulty, r.state, r.create_time createTime",
            "FROM recipe r LEFT JOIN `user` u ON r.user_id = u.id LEFT JOIN (SELECT recipe_id, IFNULL(SUM(score),0) scoreSum FROM `rate`GROUP BY rate.recipe_id) rt ON r.id = rt.recipe_id",
            "WHERE state = 0 AND 7 >= TO_DAYS(NOW())-TO_DAYS(r.update_time)",
            "ORDER BY rt.scoreSum DESC",
            "</script>"})
    IPage<RecipeShowVO> getWeeklyHotRecipeList(Page<?> page);

    @Select({"<script>",
            "SELECT r.id, u.user_name userName, u.profile_photo profilePhoto, r.recipe_name recipeName, r.show_image showImage, r.media_url mediaUrl, r.detail, r.difficulty, r.state, r.create_time createTime",
            "FROM recipe r LEFT JOIN `user` u ON r.user_id = u.id",
            "WHERE state = 0",
            "ORDER BY r.difficulty DESC",
            "</script>"})
    IPage<RecipeShowVO> getEasyRecipeList(Page<?> page);
}
