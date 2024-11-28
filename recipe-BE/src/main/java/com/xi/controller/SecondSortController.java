package com.xi.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.grace.DuplicateMethod;
import com.xi.common.grace.GraceJSONResult;
import com.xi.model.pojo.FirstSort;
import com.xi.model.pojo.SecondSort;
import com.xi.model.vo.SecSortBasicVO;
import com.xi.model.vo.SecSortVO;
import com.xi.model.vo.SecondSortVO;
import com.xi.model.vo.SortVO;
import com.xi.service.SecondSortService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Slf4j
@RestController
@Api(tags = "SecondSortController")
@RequestMapping("/second-sort")
public class SecondSortController {
    @Resource
    private SecondSortService secondSortService;


    @ApiOperation(value = "获取所有类别名信息")
    @PostMapping("/getWholeSortByPage")
    public GraceJSONResult getWholeSortByPage(@RequestBody Page<SecondSortVO> page){
        IPage<SecondSortVO> contentIPage = secondSortService.getWholeSortByPage(page);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }

    @ApiOperation(value = "根据一级类别id获取所有二级类别信息")
    @GetMapping("/getSecSortByFirstId")
    public GraceJSONResult getSecSortByFirstId(@RequestParam("firstSortId") int firstSortId){
        List<SecSortVO> sortVOS = secondSortService.getSecSortByFirstId(firstSortId);
        if(sortVOS.size() != 0){
            return GraceJSONResult.ok(sortVOS);
        }else{
            return GraceJSONResult.errorMsg("类别获取为空！");
        }
    }

    @ApiOperation(value = "根据二级类别id获取[一级类别名称/二级类别名称]")
    @GetMapping("/getSortNameBySecSortId")
    public GraceJSONResult getSortNameBySecSortId(@RequestParam("secSortId") int secSortId){
        String sortConcatName = secondSortService.getSortNameBySecSortId(secSortId);
        Map<String, String> resultData = new HashMap<>();
        resultData.put("sortConcatName", sortConcatName);
        return GraceJSONResult.ok(resultData);
    }

    @ApiOperation(value = "菜谱添加选择-根据一级id获取所有二级类别")
    @GetMapping("/getSecSortBasicByFirstId")
    public GraceJSONResult getSecSortBasicByFirstId(@RequestParam("firstSortId") int firstSortId){
        List<SecSortBasicVO> sortVOS = secondSortService.getSecSortBasicByFirstId(firstSortId);
        if(sortVOS.size() != 0){
            return GraceJSONResult.ok(sortVOS);
        }else{
            return GraceJSONResult.errorMsg("类别获取为空！");
        }
    }

    @ApiOperation(value = "新增二级类别信息")
    @PostMapping("/addSecondSort")
    public GraceJSONResult addSecondSort(@RequestBody SecondSort secondSort){
        //返回1表示插入成功；-1表示插入后的已存在，拒绝插入；0表示插入数据库操作失败
        int count = secondSortService.insertSecondSort(secondSort);
        return DuplicateMethod.notifyMsgByCount(count);
    }

    @ApiOperation(value = "更新二级类别信息")
    @PostMapping("/updateSecondSort")
    public GraceJSONResult updateSecondSort(@RequestBody SecondSort secondSort){
        log.info("secondSort:id"+secondSort.getId()+";typename:"+secondSort.getTypeName());
        int count = secondSortService.updateSecondSort(secondSort);
        //返回1表示修改成功；-1表示修改后的已存在，拒绝修改；0表示修改数据库操作失败
        return DuplicateMethod.notifyMsgByCount(count);

    }

    @ApiOperation(value = "根据id删除二级类别")
    @PostMapping("/deleteSecondSort")
    public GraceJSONResult deleteSecondSortById(@RequestParam("id") int id){
        int count = secondSortService.deleteSecondSort(id);
        if(count==0){
            return GraceJSONResult.errorMsg("操作失败！");
        }else {
            return GraceJSONResult.ok("删除成功！");
        }
    }
}

