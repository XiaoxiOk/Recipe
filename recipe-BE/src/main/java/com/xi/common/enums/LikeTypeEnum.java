package com.xi.common.enums;

import lombok.Getter;

public enum LikeTypeEnum {
    RECIPE(1), //"菜谱"
    USER_SHARE(2),//"用户动态"
    COMMENT(3),//"评论"
    REPLY(4);//"回复的评论"
    

    @Getter
    private final int code;

    LikeTypeEnum(int code) {
        this.code = code;
    }


    public static LikeTypeEnum getByCode(Integer code) {
        for (LikeTypeEnum e: LikeTypeEnum.values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

    public static boolean checkLikeTypeIsRight(Integer code) {
        if (code != LikeTypeEnum.RECIPE.code &&
                code != LikeTypeEnum.USER_SHARE.code &&
                code != LikeTypeEnum.COMMENT.code &&
                code != LikeTypeEnum.REPLY.code) {
            return false;
        }
        return true;
    }
}
