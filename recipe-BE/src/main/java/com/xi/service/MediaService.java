package com.xi.service;

import com.xi.model.pojo.Media;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface MediaService {
    public List<Media> getMediaListByShareId(int userShareId);
    public int insertOrUpdateMediaList(List<Media> imageList);
    public int delMediaByIds(List<Integer> ids);
    public int deleteByUserShareId(int userShareId);
}
