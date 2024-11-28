package com.xi.common.enums;

/**
 * @Desc: 消息类型
 */
public enum MessageTypeEnum {
    FOLLOW_YOU(1, "关注", "follow"),
    LIKE_RECIPE(2, "点赞菜谱", "likeRecipe"),
    LIKE_USER_SHARE(3, "点赞动态", "likeUserShare"),
    LIKE_COMMENT(4, "点赞评论", "likeComment"),
    LIKE_REPLY(5, "点赞回复", "likeReply"),
    COMMENT_RECIPE(6, "评论菜谱", "commentRecipe"),
    COMMENT_USER_SHARE(7, "评论动态", "commentUserShare"),
    REPLY_YOU(8, "回复", "replay"),
    ADMIN_NOTIFY(9, "管理员通知", "adminNotify"),
    SYSTEM_NOTIFY(10, "系统通知", "systemNotify");


    public final Integer type;
    public final String value;
    public final String enValue;

    MessageTypeEnum(Integer type, String value, String enValue) {
        this.type = type;
        this.value = value;
        this.enValue = enValue;
    }
}
