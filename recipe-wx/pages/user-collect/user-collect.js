// pages/user-collect/user-collect.js
var http = require("../../utils/http.js");
Page({

    /**
     * 页面的初始数据
     */
    data: {
        likeRecipeList: [],
        userId: 4,
        page: {
            current: 1,
            size: 2,
            total: 0
        },
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        
    },
    //请求商品接口
    getLikeRecipeList: function () {
        var self = this;
        console.log("like-request-page:",self.data.page)
        var params = {
            url: "/recipe/getLikeRecipeByUserId?userId=" + self.data.userId,
            method: "POST",
           // 请求所需要的的参数
           data:  self.data.page,
            callBack: function (resData) {
                console.log('resData:',resData)
                self.setData({
                    page: {
                        current: resData.current,
                        size: resData.size,
                        total: resData.total
                    }
                })
                var dataRecords = resData.records;

                
                //如果搜出来的结果<1 就说明后面已经没数据可加载了，所以将state设为0
                if(dataRecords){
                if (dataRecords.length  >= 1) {
                    self.setData({
                        likeRecipeList: self.data.likeRecipeList.concat(dataRecords),
                    });
                }
                wx.hideLoading();
            }
        }
        };
        http.request(params);
    },
    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        this.getLikeRecipeList()
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
        // 下拉刷新
        var self = this;
        wx.showLoading({
            title: '加载中...',
        });

        self.data.page.current = 1;// 页码重新设置回1
        self.data.likeRecipeList = []; // 将显示列表数据清空
        self.getLikeRecipeList();
        wx.stopPullDownRefresh();
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        var self = this;
        //已加载完毕时不再去请求
        if (self.data.likeRecipeList.length!=0 && self.data.likeRecipeList.length < self.data.page.total) {
            self.data.page.current = self.data.page.current + 1;
            self.getLikeRecipeList()
            // wx.hideLoading();
        }
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})