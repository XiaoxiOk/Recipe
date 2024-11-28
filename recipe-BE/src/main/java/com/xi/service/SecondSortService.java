package com.xi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.SecondSort;
import com.xi.model.vo.SecSortBasicVO;
import com.xi.model.vo.SecSortVO;
import com.xi.model.vo.SecondSortVO;
import com.xi.model.vo.SortVO;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface SecondSortService{

    List<SortVO> getWholeSortName();
    IPage<SecondSortVO> getWholeSortByPage(Page<SecondSortVO> page);
    List<SecSortVO> getSecSortByFirstId(int firstSortId);
    String getSortNameBySecSortId(int secSortId);
    List<SecSortBasicVO> getSecSortBasicByFirstId(int firstSortId);
    public int insertSecondSort(SecondSort secondSort);
    public int updateSecondSort(SecondSort secondSort);
    public int deleteSecondSort(Integer sortId);
}
