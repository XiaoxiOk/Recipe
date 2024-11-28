package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.mapper.FirstSortMapper;
import com.xi.mapper.SecondSortMapper;
import com.xi.model.pojo.SecondSort;
import com.xi.model.vo.SecSortBasicVO;
import com.xi.model.vo.SecSortVO;
import com.xi.model.vo.SecondSortVO;
import com.xi.model.vo.SortVO;
import com.xi.service.SecondSortService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Slf4j
@Service
public class SecondSortServiceImpl implements SecondSortService {

    @Resource
    private SecondSortMapper secondSortMapper;


    @Override
    public List<SortVO> getWholeSortName() {
        return secondSortMapper.getWholeSortName();
    }

    //分页获取二级类别信息--连接一级类别ID、名称
    @Override
    public IPage<SecondSortVO> getWholeSortByPage(Page<SecondSortVO> page){
        return secondSortMapper.selectWholeSortByPage(page);
    }


    @Override
    public List<SecSortVO> getSecSortByFirstId(int firstSortId) {
        QueryWrapper<SecondSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent", firstSortId);
        List<SecondSort> secondSorts = secondSortMapper.selectList(queryWrapper);
        List<SecSortVO> secSortVOS = new ArrayList<>();
        for (SecondSort secSort: secondSorts) {
            SecSortVO secSortVO = new SecSortVO();
            secSortVO.setSecId(secSort.getId());
            secSortVO.setSecName(secSort.getTypeName());
            secSortVO.setImage(secSort.getImage());
            secSortVOS.add(secSortVO);
        }
        return secSortVOS;
    }

    @Override
    public String getSortNameBySecSortId(int secSortId) {
        return secondSortMapper.getSortNameBySecSortId(secSortId);
    }

    //根据一级类别查找出其下所有二级类别信息-用于类别选择
    @Override
    public List<SecSortBasicVO> getSecSortBasicByFirstId(int firstSortId) {
        List<SecSortBasicVO> secSortVOS = secondSortMapper.getBasicByParent(firstSortId);
        return secSortVOS;
    }
    //插入二级类别信息
    public int insertSecondSort(SecondSort secondSort){
        //返回1表示插入成功；-1表示插入后的已存在，拒绝插入；0表示插入数据库操作失败
        SecondSort sortQuery = secondSortMapper.selectOne(new QueryWrapper<SecondSort>().lambda().eq(SecondSort::getTypeName,secondSort.getTypeName()).eq(SecondSort::getParent,secondSort.getParent()));
        if(sortQuery != null){
            log.info("插入失败！该二级类别已存在！");
            return -1;
        }else {
            return secondSortMapper.insert(secondSort);
        }
    }

    //更新一级类别信息
    public int updateSecondSort(SecondSort secondSort){
        //返回1表示修改成功；-1表示修改后的已存在，拒绝修改；0表示修改数据库操作失败
        SecondSort sortQuery = secondSortMapper.selectOne(new QueryWrapper<SecondSort>().lambda().eq(SecondSort::getTypeName,secondSort.getTypeName()).eq(SecondSort::getParent,secondSort.getParent()));
        if(sortQuery != null){
            log.info("更新失败！该二级类别已存在！");
            return -1;
        }else {
            return secondSortMapper.updateById(secondSort);
        }
    }

    //删除一级类别信息
    public int deleteSecondSort(Integer sortId){
        QueryWrapper<SecondSort> secondSortQueryWrapper = new QueryWrapper<SecondSort>();
        return secondSortMapper.deleteById(sortId);
    }
}
