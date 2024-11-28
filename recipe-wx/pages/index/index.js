//index.js
//获取应用实例
var http = require("../../utils/http.js");
var config = require("../../utils/config.js");

Page({
    data: {
        autoplay: true,
        interval: 2000,
        myId: getApp().globalData.currentUserId,  // 当前登录用户ID
        resourceRootPath: config.resourceRootPath,
        adList: [],
        recommendList: [], // 个性化推荐的菜谱列表
        recommendRecipeIds: [], // 个性化推荐的菜谱id
        recipeList: [],
        seq: 0,
        sts: 0,
        recipeId: 0,
        page: {
            current: 1,
            size: 4,
            total: 0
        },
    },
    //事件处理函数
    bindViewTap: function () {
        wx.navigateTo({
            url: '../logs/logs'
        })
    },
    //重置数据信息
    reFreshData() {
        var self = this;
        self.data.page.current = 1;// 页码重新设置回1
        self.data.page.total = 0;
        this.setData({
            recommendList: [],
            recipeList: [], // 将显示列表数据清空
            myId: getApp().globalData.currentUserId,  // 当前登录用户ID
        })
        self.getAllData();
    },
    onLoad: function () {
        //  this.reFreshData();
    },
    //重置数据信息
    reFreshData() {
        this.data.page.current = 1;// 页码重新设置回1
        this.setData({
            recipeList: [],
            myId: getApp().globalData.currentUserId,  // 当前登录用户ID
        })
        // // 加载数据列表
        this.getAllData();
    },
    // 页面滚动到指定位置指定元素固定在顶部
    onPageScroll: function (e) { //监听页面滚动
        this.setData({
            scrollTop: e.scrollTop
        })
    },
    //跳转到京东购物小程序
    toShowAdPage: function (e) {

        // url是后台生成的短链，具体看文档
        let url = e.currentTarget.dataset.url;
        console.log("url:", url)
        var path = "pages/union/proxy/proxy?spreadUrl=" + encodeURIComponent(url);
        wx.navigateToMiniProgram({
            appId: 'wx91d27dbf599dff74', //京东购物小程序appId
            path: path,
            success(res) {
                console.log("进入推广平台")
            }
        })

    },
    toRecipePage: function (e) {
        var recipeId = e.currentTarget.dataset.recipeid;
        console.log('recipeId:', recipeId)
        if (recipeId > 0) {
            console.log('toRecipePage')
            wx.navigateTo({
                url: '/pages/recipe-detail/recipe-detail?recipeId=' + recipeId,
            })
        }
    },


    // 跳转搜索页
    toSearchPage: function () {
        wx.navigateTo({
            url: '/pages/search-page/search-page',
        })
    },

    //跳转菜谱菜单页面
    toClassifyPage: function (e) {
        var url = '/pages/index-menu/index-menu?option=' + e.currentTarget.dataset.sts;
        wx.navigateTo({
            url: url
        })
    },


    onShow: function () {
        this.reFreshData();
    },
    getAllData() {
        // http.getCartCount(); //重新计算购物车总数量  类似消息通知刷新数量
        this.getRecomendList();
        this.getAdList();
      //  this.getRecipeList();
    },
    getRecomendList() {
        var self = this
        var params = {
            url: "/recommend/getRecommendListByUserId?userId=" + self.data.myId,
            method: "POST",
            callBack: (resData) => {
                if (resData.hasOwnProperty('count') && resData.count == 0) {
                    console.log(resData.msg)
                } else if (resData.hasOwnProperty('records') && resData.hasOwnProperty('ids')) {
                    console.log('recommend-recipes', resData)

                    //搜出有结果时，拼接到列表中
                    self.setData({
                        recommendList: resData.records,
                        recommendRecipeIds: resData.ids
                    });
                }
                this.getRecipeList();
            }
        };
        http.request(params);

    },
    // 分页获取菜谱信息列表
    getRecipeList() {
        var self = this
        // 请求后台接口获取文章列表
        if (self.data.recipeList.length != 0 && self.data.recipeList.length === self.data.page.total) {
            return
        }
        console.log('this.recommendRecipeIds:', self.data.recommendRecipeIds)
        var params = {
            url: "/recipe/getRecipeFilterByPage?recommendRecipeIds="+self.data.recommendRecipeIds,
            method: "POST",
            data: self.data.page,
            callBack: (resData) => {
                console.log('recipe-resData', resData)
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
    //加载轮播图
    getAdList() {
        //加载轮播图
        var params = {
            url: "/ad/getAllAd",
            method: "GET",
            data: {},
            callBack: (res) => {
                this.setData({
                    adList: res,
                    seq: res
                });
            }
        };
        http.request(params);

        //真机测试跳转京东购物 url非本地服务器
        // var adDataStr = testData.adDataStr;
        // var res =  JSON.parse(adDataStr);
        // this.setData({
        //     adList: res.data
        // })
    },

    onReachBottom() {
        var self = this;
        if (self.data.recipeList.length != 0 && self.data.recipeList.length < self.data.page.total) {
            console.log("add current")
            self.data.page.current = self.data.page.current + 1;
            console.log('current:', self.data.page.current)
            self.getRecipeList()
        }
    },



    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    // onPullDownRefresh: function () {
    //     wx.request({
    //       url: '',
    //       data: {},
    //       method: 'GET',
    //       success: function (res) { },
    //       fail: function (res) { },
    //       complete: function (res) {
    //         wx.stopPullDownRefresh();
    //       }
    //     })
    // },

    onPullDownRefresh: function () {

        // wx.showNavigationBarLoading() //在标题栏中显示加载

        //模拟加载
        var ths = this;
        setTimeout(function () {

            ths.getAllData();

            // wx.hideNavigationBarLoading() //完成停止加载

            wx.stopPullDownRefresh() //停止下拉刷新

        }, 100);

    },

    /**
     * 跳转至商品详情
     */
    showProdInfo: function (e) {
        let relation = e.currentTarget.dataset.relation;
        if (relation) {
            wx.navigateTo({
                url: 'pages/prod/prod',
            })
        }
    }
})