package com.xi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.SecondSort;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xi.model.vo.SecSortBasicVO;
import com.xi.model.vo.SecSortVO;
import com.xi.model.vo.SecondSortVO;
import com.xi.model.vo.SortVO;
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
public interface SecondSortMapper extends BaseMapper<SecondSort> {

    @Select("SELECT * FROM second_sort")
    IPage<SecondSort> selectListByPage(Page<?> page);


    @Select("SELECT a.id secId, a.type_name secName,a.image image, b.id firstId, b.type_name firstName, a.create_time createTime, a.update_time updateTime \n" +
            "FROM `second_sort` AS a \n" +
            "LEFT JOIN `first_sort` AS b \n" +
            "ON a.parent = b.id")
    public IPage<SecondSortVO> selectWholeSortByPage(Page<?> page);

    @Select("SELECT a.id secId, a.type_name secName, b.id firstId, b.type_name firstName \n" +
            "FROM `second_sort` AS a \n" +
            "LEFT JOIN `first_sort` AS b \n" +
            "ON a.parent = b.id")
    List<SortVO> getWholeSortName();

    @Select("SELECT id, type_name typeName FROM second_sort WHERE parent = #{parent}")
    List<SecSortBasicVO> getBasicByParent(@Param("parent") int parent);


    @Select("SELECT CONCAT(f.type_name,'/', s.type_name) sortConcatName\n" +
            "FROM second_sort s LEFT JOIN first_sort f ON s.parent = f.id\n" +
            "WHERE s.id = #{secSortId}")
    String getSortNameBySecSortId(@Param("secSortId") int secSortId);
}
