import request from '@/utils/request'
export function getRecipeBasicByPage(page, state) {
    return request({
        url: '/recipe/getRecipesByPageAndState',
        method: 'post',
        params: { "state": state },
        data: page
    });
};
export function searchRecipeByNameOrState(page, inputStr, state) {
    return request({
        url: '/recipe/searchRecipeByNameOrState',
        method: 'post',
        params: { "inputStr": inputStr, "state": state },
        data: page
    });
};
export function getRecipeAppendById(recipeId) {
    return request({
        url: '/recipe/getRecipeAppendById',
        method: 'post',
        params: { "recipeId": recipeId }
    });
};

export function updateRecipeStateById(recipeId, state) {
    return request({
        url: '/recipe/updateRecipeStateById',
        method: 'post',
        params: { "recipeId": recipeId, "state": state }
    })
};