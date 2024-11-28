package com.xi.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.grace.DuplicateMethod;
import com.xi.common.grace.GraceJSONResult;
import com.xi.model.pojo.FirstSort;
import com.xi.model.pojo.User;
import com.xi.service.FirstSortService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
@Api(tags = "FirstSortController")
@RequestMapping("/first-sort")
public class FirstSortController {
    @Resource
    private FirstSortService firstSortService;

    @ApiOperation(value = "分页获取一级类别列表")
    @PostMapping("/getFirstSortList") //使用@RequestBody JSON传数据只能是POST方式
    public GraceJSONResult getFirstSortList(@RequestBody Page<FirstSort> page){

        IPage<FirstSort> contentIPage = firstSortService.getFirstSortList(page);
        return DuplicateMethod.getDataListByPage(contentIPage,page);

    }

    @ApiOperation(value = "获取一级类别id&名称列表")
    @GetMapping("/getFirstSortNameList")
    public GraceJSONResult getFirstSortNameList()
    {//返回一级类别名称列表
        return GraceJSONResult.ok(firstSortService.getFirstSortNameList());
    }

    @ApiOperation(value = "新增一级类别信息")
    @PostMapping("/addFirstSort")
    public GraceJSONResult addFirstSort(@RequestBody FirstSort firstSort){
        int count = firstSortService.insertFirstSort(firstSort);
        //返回1表示插入成功；-1表示插入后的已存在，拒绝插入；0表示插入数据库操作失败
        return DuplicateMethod.notifyMsgByCount(count);
    }

    @ApiOperation(value = "更新一级类别信息")
    @PostMapping("/updateFirstSort")
    public GraceJSONResult updateFirstSort(@RequestBody FirstSort firstSort){
       log.info("firstSort:id"+firstSort.getId()+";typename:"+firstSort.getTypeName());
        int count = firstSortService.updateFirstSort(firstSort);
        //返回1表示修改成功；-1表示修改后的已存在，拒绝修改；0表示修改数据库操作失败
        return DuplicateMethod.notifyMsgByCount(count);
    }

    @ApiOperation(value = "根据id删除一级类别")
    @PostMapping("/deleteFirstSortById")
    public GraceJSONResult deleteFirstSortById(@RequestParam("id") int id){
        int count = firstSortService.deleteFirstSort(id);
        if(count == 1){
            return GraceJSONResult.ok("删除成功！");
        }else if(count == -1){
            return GraceJSONResult.notifyMsg("该一级类别中还有二级类别从属!");
        }
        else{
            return GraceJSONResult.errorMsg("操作失败！");
        }
    }
}

