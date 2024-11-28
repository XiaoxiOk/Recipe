package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.mapper.FirstSortMapper;
import com.xi.mapper.SecondSortMapper;
import com.xi.model.pojo.FirstSort;
import com.xi.model.pojo.SecondSort;
import com.xi.model.vo.FirstSortVO;
import com.xi.service.FirstSortService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
public class FirstSortServiceImpl implements FirstSortService {


    @Resource
    private FirstSortMapper firstSortMapper;

    @Resource
    private SecondSortMapper secondSortMapper;

    //获取所有一级类别信息
    public IPage<FirstSort> getFirstSortList(Page<FirstSort> page){
        return firstSortMapper.selectListByPage(page);
    }

    //获取一级类别id&名称列表
    public List<FirstSortVO> getFirstSortNameList() {
//        QueryWrapper<FirstSort> queryWrapper = new QueryWrapper<FirstSort>();
//        queryWrapper.select("id","type_name");
//        List<FirstSort> firstSortList = firstSortMapper.selectList(queryWrapper);
        return firstSortMapper.getFirstSortNameList();
    }

    //插入一级类别信息
    public int insertFirstSort(FirstSort firstSort){
        //返回1表示插入成功；-1表示插入后的已存在，拒绝插入；0表示插入数据库操作失败
        FirstSort sortQuery = firstSortMapper.selectOne(new QueryWrapper<FirstSort>().lambda().eq(FirstSort::getTypeName,firstSort.getTypeName()));
        if(sortQuery != null){
            log.info("插入失败！该一级类别已存在！");
            return -1;
        }else {
            return firstSortMapper.insert(firstSort);
        }
    }

    //更新一级类别信息
    public int updateFirstSort(FirstSort firstSort){
        //返回1表示修改成功；-1表示修改后的已存在，拒绝修改；0表示修改数据库操作失败
        FirstSort sortQuery = firstSortMapper.selectOne(new QueryWrapper<FirstSort>().lambda().eq(FirstSort::getTypeName,firstSort.getTypeName()));
        if(sortQuery != null){
            log.info("更新失败！该一级类别已存在！");
            return -1;
        }else {
            return firstSortMapper.updateById(firstSort);
        }
    }

    //删除一级类别信息
    public int deleteFirstSort(Integer sortId){
        QueryWrapper<SecondSort> secondSortQueryWrapper = new QueryWrapper<SecondSort>();
        secondSortQueryWrapper.eq("parent", sortId);
        //获得一级类别从属的二级类别总数
        Long count = secondSortMapper.selectCount(secondSortQueryWrapper);
        if(count==0) {  //所属二级类别为空时可删除该一级类别
            return firstSortMapper.deleteById(sortId);
        }else{//删除失败
            log.info("该一级类别中还有二级类别从属,总数为："+ count);
            return -1;
        }

    }
}
