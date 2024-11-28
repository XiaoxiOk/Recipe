package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xi.mapper.LogoMapper;
import com.xi.model.pojo.Logo;
import com.xi.service.LogoService;
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
public class LogoServiceImpl implements LogoService {

    @Resource
    private LogoMapper logoMapper;

    public Logo getCurrentLogo() {
        List<Logo> logoList = logoMapper.selectList(new QueryWrapper<Logo>().lambda().eq(Logo::getState, 1));
        if (logoList.size() == 1) {
            return logoList.get(0);
        } else {
            return logoList.get(logoList.size() - 1);
        }
    }

    public List<Logo> getAllLogo() {
        return logoMapper.selectList(null);
    }

    public List<Logo> getHistoryLogo() {
        return logoMapper.selectList(new QueryWrapper<Logo>().lambda().eq(Logo::getState, 0));
    }

    public int updateCurrentLogo(int currentId, Logo logo) {
        Logo currentLogo = new Logo();
        currentLogo.setState(0);
        int updateResult = logoMapper.update(currentLogo, new LambdaUpdateWrapper<Logo>().eq(Logo::getId, currentId));
        int insertResult = logoMapper.insert(logo);
        if (updateResult == 1 && insertResult == 1) {
            return getCurrentLogo().getId();
        } else {
            return -1; //失败时设置新插入的id为-1
        }
    }
}
