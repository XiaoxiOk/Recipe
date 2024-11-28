package com.xi.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.FirstSort;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xi.model.vo.FirstSortVO;
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
public interface FirstSortMapper extends BaseMapper<FirstSort> {

    @Select("SELECT * FROM first_sort")
    IPage<FirstSort> selectListByPage(Page<?> page);

    @Select("SELECT id, type_name typeName FROM first_sort")
    List<FirstSortVO> getFirstSortNameList();
}
