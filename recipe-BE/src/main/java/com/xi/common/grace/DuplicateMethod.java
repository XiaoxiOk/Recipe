package com.xi.common.grace;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 用于将逻辑类似的抽象成统一类方法
 */
public class DuplicateMethod {
    /**
     * 根据操作结果影响条数来返回通知类型和信息
     * @param count 操作结果影响条数：
     *              1表示添加/修改成功；
     *              -1表示添加/修改后的已存在，拒绝修改；
     *              0表示添加/修改数据库操作失败
     * @return
     */
    public static GraceJSONResult notifyMsgByCount(int count) {

        if(count == 1){
            return GraceJSONResult.ok("操作成功！");
        }else if(count == -1){
            return new GraceJSONResult(ResponseStatusEnum.FAILED_NOTIFY, "拒绝操作:已存在！");
        }else if(count == 0){
            return GraceJSONResult.errorMsg("数据库操作失败！");
        }else {
            return GraceJSONResult.errorMsg("操作失败！");
        }
    }

    /**
     * 分页数据返回
     */
    public static GraceJSONResult getDataListByPage(IPage<?> contentIPage, Page<?> page){
        List<?> list = contentIPage.getRecords();
        long total = contentIPage.getTotal();
        if(page.getSize() <= 0){// page.size<0 一般取-1返回全部数据;
            page.setTotal(list.size());// 但不分页时total不会更新，故手动更新一下总数
            return GraceJSONResult.ok(contentIPage);
        }else if(total !=0 && list.size()!=0){
            return GraceJSONResult.ok(contentIPage);
        }else{
            return GraceJSONResult.ok("暂无数据");
        }
    }

}
