package com.xi.service;

import com.xi.model.pojo.Ad;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface AdService{
    public List<Ad> getAllAd();
    public Ad getAdById(int id);
    public int updateAd(Ad ad);
}
