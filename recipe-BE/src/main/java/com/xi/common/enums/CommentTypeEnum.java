package com.xi.common.enums;

import lombok.Getter;

public enum CommentTypeEnum {
    RECIPE(1), //"菜谱"
    USER_SHARE(2);//"用户动态"


    @Getter
    private final int code;

    CommentTypeEnum(int code) {
        this.code = code;
    }


    public static CommentTypeEnum getByCode(Integer code) {
        for (CommentTypeEnum e: CommentTypeEnum.values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

    public static boolean checkTypeIsRight(Integer code) {
        if (code != CommentTypeEnum.RECIPE.code &&
                code != CommentTypeEnum.USER_SHARE.code) {
            return false;
        }
        return true;
    }
}
