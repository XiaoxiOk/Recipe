package com.xi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.FirstSort;
import com.xi.model.vo.FirstSortVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface FirstSortService{
    public IPage<FirstSort> getFirstSortList(Page<FirstSort> page);
    public List<FirstSortVO> getFirstSortNameList();
    public int insertFirstSort(FirstSort firstSort);
    public int updateFirstSort(FirstSort firstSort);
    public int deleteFirstSort(Integer sortId);
}
