package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xi.mapper.MediaMapper;
import com.xi.model.pojo.Media;
import com.xi.service.MediaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Service
public class MediaServiceImpl implements MediaService {

    @Resource
    private MediaMapper mediaMapper;
    //根据用户动态ID获取其下所有图片信息
    public List<Media> getMediaListByShareId(int userShareId){
        return mediaMapper.getImagesByShareId(userShareId);
    }

    //批量修改某个用户动态的图片信息，有则修改，无则插入/新增
    public int insertOrUpdateMediaList(List<Media> imageList){
        return mediaMapper.insertOrUpdateImageList(imageList);
    }

    //批量删除动态的图片信息
    public int delMediaByIds(List<Integer> ids){
        return mediaMapper.deleteImageByIds(ids);
    }

    //根据用户动态id删除所有图片信息
    public int deleteByUserShareId(int userShareId){
        QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_share_id", userShareId);
        return mediaMapper.delete(queryWrapper);
    }
}
