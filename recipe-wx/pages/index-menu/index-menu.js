// pages/index-menu/index-menu.js
var config = require('../../utils/config.js');
var http = require('../../utils/http.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        option: 1,
        titleText: '',
        recipeList: [],
        page: {
            current: 1,
            size: 4,
            total: 0
        },
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

        if (options) {
            this.setData({
                recipeList: [],
                option: parseInt(options.option), // 页面传过来的菜谱id是string类型
            });
        }

        console.log("has been in index-menu page option:", this.data.option)
        var titleText

        switch (this.data.option) {
            case 1: titleText = '新谱上线'; break;
            case 2: titleText = '每周热榜'; break;
            case 3: titleText = '新手入门'; break;
        }
        this.setData({
            titleText: titleText
        })
        this.getRecipeList()
    },
    getRecipeList() {
        var url;
        console.log('this.data.option:', this.data.option, typeof (this.data.option))
        switch (this.data.option) {
            case 1: url = '/recipe/getLatestRecipes'; break;
            case 2: url = '/recipe/getWeeklyHotRecipes'; break;
            case 3: url = '/recipe/getEasyRecipes'; break;
        }
        this.questRecipes(url)
    },
    //重置数据信息
    reFreshData() {
        var self = this;
        self.data.page.current = 1;// 页码重新设置回1
        self.data.page.total = 0;
        this.setData({
            recipeList: [], // 将显示列表数据清空
        })
        self.getRecipeList()
    },
    questRecipes(targetUrl) {
        console.log('targetUrl', targetUrl)
        var self = this;
        // 请求后台接口获取菜谱作品列表
        if (self.data.recipeList.length != 0 && self.data.recipeList.length === self.data.page.total) {
            console.log("拒绝请求")
            return
        }
        console.log("index-menu开始请求")
        var params = {
            // 请求连接
            url: targetUrl,
            method: "POST",
            // 请求所需要的的参数
            data: self.data.page,
            callBack: (resData) => {
                console.log('myRecipe-resData', resData)
                self.setData({
                    page: {
                        current: resData.current,
                        size: resData.size,
                        total: resData.total
                    }
                })
                var dataRecords = resData.records;
                if (dataRecords) {
                    //搜出有结果时，拼接到列表中
                    if (dataRecords.length >= 1) {
                        self.setData({
                            recipeList: self.data.recipeList.concat(dataRecords),
                        });
                    }
                    console.log("index-menu-recipeList:", self.data.recipeList)
                }
            }
        };
        http.request(params);
    },
    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        var self = this;
        if (self.data.recipeList.length != 0 && self.data.recipeList.length < self.data.page.total) {
            console.log("add current")
            self.data.page.current = self.data.page.current + 1;
            self.getRecipeList()
        }
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})