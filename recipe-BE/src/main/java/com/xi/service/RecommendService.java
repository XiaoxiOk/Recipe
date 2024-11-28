package com.xi.service;

import com.xi.model.dto.RelateDTO;

import java.util.List;

public interface RecommendService {
    List<Integer>  userCfRecommend(int userId, int maxSize);
}
