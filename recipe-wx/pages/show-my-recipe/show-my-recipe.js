// pages/show-my-recipe/show-my-recipe.js
var config = require('../../utils/config.js');
var http = require('../../utils/http.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        resourceRootPath: config.resourceRootPath,
        recipeList: [],
        page: {
            current: 1,
            size: 4,
            total: 0
        },
        myId: getApp().globalData.currentUserId,  // 当前登录用户ID
        isShowHandle: false, // 是否显示操作遮罩层
        toHandleRecipeId:0,//将要操作的菜谱ID
        toHandleRecipeState:0 ,//操作菜谱的状态，为1时表示被下架，只能删除不可编辑
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {

    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        this.reFreshData()
    },
    //重置数据信息
    reFreshData(){
        var self = this;
        self.data.page.current = 1;// 页码重新设置回1
        self.data.page.total = 0;
        this.setData({
            recipeList:[], // 将显示列表数据清空
            myId: getApp().globalData.currentUserId,  // 当前登录用户ID
        })
        self.getMyRecipeList();
    },
    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {
        this.reFreshData()
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
       
        wx.showLoading({
            title: '加载中...',
        });
        this.reFreshData()
       
        wx.stopPullDownRefresh();
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        var self = this;
        if (self.data.recipeList.length!=0 && self.data.recipeList.length < self.data.page.total) {
            console.log("add current")
            self.data.page.current = self.data.page.current + 1;
            self.getMyRecipeList()
        }
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    },
    //加载我的菜谱作品列表
    getMyRecipeList() {
        var self = this;
        // 请求后台接口获取菜谱作品列表
        if (self.data.recipeList.length!=0 && self.data.recipeList.length === self.data.page.total) {
            console.log("拒绝请求")
            return
        }
        var params = {
            // 请求连接
            url: '/recipe/getRecipeListByUserId?userId=' + self.data.myId +'&isDifferOnShow=false',
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
                    wx.hideLoading();
                }
            }
        };
        http.request(params);
    },
    //关闭[操作]遮罩层
    showHandle(e){
        console.log('long tap!!')
        let index = e.currentTarget.dataset.index
        let state = e.currentTarget.dataset.state
        let recipeId = this.data.recipeList[index].id

        this.setData({
            toHandleRecipeId: recipeId,
            isShowHandle: true,
            toHandleRecipeState: state
        })
    },
    //关闭[操作]遮罩层
    closeShowHandle(){
        this.setData({
            isShowHandle: false
        })
    },
    //跳转到编辑-菜谱页面
    toEditRecipe(){
        console.log('编辑菜谱ID->',this.data.toHandleRecipeId)
        wx.redirectTo({
            url: '/pages/edit-recipe/edit-recipe?recipeId=' + this.data.toHandleRecipeId,
        })
    },

    //删除菜谱
    toDelRecipe(){
        console.log('删除菜谱ID->',this.data.toHandleRecipeId)
        var self = this
        var params = {
            url: '/recipe/deleteRecipeWhole?recipeId=' + self.data.toHandleRecipeId,
            method: "POST",
            callBack: function (resData) {
                // console.log(res);
                console.log("res.data:", resData);
                wx.showToast({
                    title: '删除成功!',
                })
                self.onShow();
            }
        };
        http.request(params);
    }
})