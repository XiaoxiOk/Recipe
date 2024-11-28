package com.xi.service.impl;

import com.xi.mapper.AdMapper;
import com.xi.model.pojo.Ad;
import com.xi.service.AdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Service
public class AdServiceImpl implements AdService {
    @Resource
    private AdMapper adMapper;

    public List<Ad> getAllAd() {
        return adMapper.selectList(null);
    }

    public Ad getAdById(int id) {
        return adMapper.selectById(id);
    }

    public int updateAd(Ad ad) {
        return adMapper.updateById(ad);
    }
}
