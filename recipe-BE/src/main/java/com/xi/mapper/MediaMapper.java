package com.xi.mapper;

import com.xi.model.pojo.Media;
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
public interface MediaMapper extends BaseMapper<Media> {
    @Select("SELECT * FROM media WHERE user_share_id = #{userShareId}")
    public List<Media> getImagesByShareId(int userShareId);
    @Insert({"<script>",
            "INSERT INTO media(id, cover_url, file_url, user_share_id)",
            "VALUES",
            "<foreach collection =\"mediaList\" item=\"media\" separator =\",\">",
            "(#{media.id}, #{media.coverUrl}, #{media.fileUrl}, #{media.userShareId})",
            "</foreach>",
            "on duplicate key update",
            "id = VALUES(id), cover_url = VALUES(cover_url),  file_url = VALUES(file_url),user_share_id = VALUES(user_share_id)",
            "</script>"})
    public int insertOrUpdateImageList(@Param("mediaList") List<Media> mediaList);

    @Delete({"<script>",
            "delete from media where id in ",
            "<foreach collection=\"ids\" item=\"id\" separator=\",\" open=\"(\" close=\")\">\n" +
                    "#{id}",
            "</foreach>",
            "</script>"})
    public int deleteImageByIds(@Param("ids") List<Integer> ids);
    
}
