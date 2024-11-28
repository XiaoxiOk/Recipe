package com.xi.common.enums;

import lombok.Getter;

public enum RecipeStateEnum {
    ON_SHOW(0), //"上架/可操作"
    OFF_SHOW(1),//"下架"
    NOT_DIFFER(-1);//不区分

    @Getter
    private final int state;

    RecipeStateEnum(int state) {
        this.state = state;
    }


    public static RecipeStateEnum getByState(Integer state) {
        for (RecipeStateEnum e: RecipeStateEnum.values()) {
            if (e.getState() == state) {
                return e;
            }
        }
        return null;
    }

    public static boolean checkLikeTypeIsRight(Integer state) {
        if (state != RecipeStateEnum.ON_SHOW.state &&
                state != RecipeStateEnum.OFF_SHOW.state &&
                state != RecipeStateEnum.NOT_DIFFER.state) {
            return false;
        }
        return true;
    }
}
