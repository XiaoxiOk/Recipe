// components/recipe-cross/recipe-cross.js
var config = require('../../utils/config.js');
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        item: Object,
    },

    /**
     * 组件的初始数据
     */
    data: {
        resourceRootPath: config.resourceRootPath,
    },

    /**
     * 组件的方法列表
     */
    methods: {
        toRecipePage: function (e) {
            var recipeId = e.currentTarget.dataset.recipeid;
            wx.navigateTo({
                url: '/pages/recipe-detail/recipe-detail?recipeId=' + recipeId,
            })
        }
    }
})
