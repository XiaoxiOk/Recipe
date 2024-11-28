package com.xi.common.utils;

import org.springframework.beans.factory.annotation.Autowired;


public class BaseInfoProperties {

    @Autowired
    public RedisOperator redis;

    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_INFO = "redis_user_info";


    // 我的关注总数
    public static final String REDIS_MY_FOLLOWS_COUNTS = "redis_my_follows_counts";
    // 我的粉丝总数
    public static final String REDIS_MY_FANS_COUNTS = "redis_my_fans_counts";
    // 我和其他发布者之间的关联关系，依赖redis，不要存储数据库，避免db的性能瓶颈，用于判断他们是否互粉
    public static final String REDIS_USER_RELATIONSHIP = "redis_user_relationship";

    // 菜谱获赞数
    public static final String REDIS_RECIPE_BE_LIKED_COUNTS = "redis_recipe_be_liked_counts";
    // 发布者发布动态获赞数
    public static final String REDIS_USER_SHARE_BE_LIKED_COUNTS = "redis_user_share_be_liked_counts";

    // 菜谱的评论总数
    public static final String REDIS_RECIPE_COMMENT_COUNTS = "redis_recipe_comment_counts";
    // 用户动态的评论总数
    public static final String REDIS_USER_SHARE_COMMENT_COUNTS = "redis_userShare_comment_counts";

    // 评论获赞数
    public static final String REDIS_COMMENT_BE_LIKED_COUNTS = "redis_comment_be_liked_counts";
    // 回复获赞数
    public static final String REDIS_REPLY_BE_LIKED_COUNTS = "redis_reply_be_liked_counts";

    // 评论获得回复总数
    public static final String REDIS_COMMENT_BE_REPLIED_COUNTS = "redis_comment_be_replied_counts";

    // 用户是否喜欢/点赞菜谱，取代数据库的关联关系，1：喜欢，0：不喜欢（默认） redis_user_like_recipe:{userId}:{recipeId}
    public static final String REDIS_USER_LIKE_RECIPE = "redis_user_like_recipe";
    // 用户点赞动态
    public static final String REDIS_USER_LIKE_USER_SHARE = "redis_user_like_userShare";
    // 用户点赞评论
    public static final String REDIS_USER_LIKE_COMMENT = "redis_user_like_comment";
    // 用户点赞回复
    public static final String REDIS_USER_LIKE_REPLY = "redis_user_like_reply";

}
