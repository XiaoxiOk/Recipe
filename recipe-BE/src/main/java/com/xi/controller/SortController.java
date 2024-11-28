package com.xi.controller;

import com.xi.common.grace.GraceJSONResult;
import com.xi.model.vo.FirstSortVO;
import com.xi.model.vo.SecSortBasicVO;
import com.xi.model.vo.SortVO;
import com.xi.service.FirstSortService;
import com.xi.service.SecondSortService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "SortController")
@RequestMapping("/sort")
public class SortController {
    @Resource
    private FirstSortService firstSortService;
    @Resource
    private SecondSortService secondSortService;

    @ApiOperation(value = "获取所有类别名信息")
    @GetMapping("/getWholeSortName")
    public GraceJSONResult getWholeSortName(){
        List<FirstSortVO> firstSortList = firstSortService.getFirstSortNameList();
        List<SortVO> sortVOS = new ArrayList<>();
        for (FirstSortVO firstSortVO: firstSortList) {
            SortVO sortVO = new SortVO();
            sortVO.setId(firstSortVO.getId());
            sortVO.setTypeName(firstSortVO.getTypeName());
            sortVO.setChildren(secondSortService.getSecSortBasicByFirstId(firstSortVO.getId()));
            sortVOS.add(sortVO);
        }
        return GraceJSONResult.ok(sortVOS);
    }
}
