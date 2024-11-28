package com.xi.service;

import com.xi.model.pojo.Logo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface LogoService{
    public Logo getCurrentLogo();
    public List<Logo> getAllLogo();
    public List<Logo> getHistoryLogo();
    public int updateCurrentLogo(int currentId, Logo logo);

}
