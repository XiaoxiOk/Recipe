// pages/show-other-user/show-other-user.js
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        tabIndex: 0,//默认【菜谱|动态】选择菜谱
        myId: getApp().globalData.currentUserId,//当前登录用户ID
        resourceRootPath: config.resourceRootPath,
        userId: 0, //所查看的对象用户ID
        mobile: "",
        userName: "",
        gender: 2,
        birthday: "",
        profilePhoto: "",
        createTime: "",
        thisFollowsCounts: 100,
        thisFansCounts: 200,
        doIFollowThisUser: false,
        recipeList: [],
        shareList: [],
        recipePage: {
            current: 1,
            size: 3,
            total: 0
        },
        sharePage: {
            current: 1,
            size: 3,
            total: 0
        },
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        console.log("has been in show-other-user page")
        if (options) {
            this.setData({
                userId: parseInt(options.userId), // 页面传过来的动态id是string类型
            });
            this.getShowUserById()
            this.getRecipesByThisUserId()
        }

    },
    //获取查看用户的基本信息
    getShowUserById() {
        wx.showLoading();
        var params = {
            url: "/user/getShowUserById",
            method: "GET",
            data: {
                userId: this.data.userId,
                myId: this.data.myId
            },
            callBack: (resData) => {
                console.log("res.data:", resData);
                this.setData({
                    mobile: resData.mobile,
                    userName: resData.userName,
                    gender: resData.gender,
                    birthday: resData.birthday,
                    frozen: resData.frozen,
                    profilePhoto: resData.profilePhoto,
                    createTime: resData.createTime,
                    thisFollowsCounts: resData.thisFollowsCounts,
                    thisFansCounts: resData.thisFansCounts,
                    doIFollowThisUser: resData.doIFollowThisUser
                });
                wx.hideLoading();
            }
        };
        http.request(params);
    },
    //关注/取关操作
    updateFollowInfo() {
        console.log('关注/取关操作', this.data.doIFollowThisUser)
        let doIFollowThisUser = this.data.doIFollowThisUser
        let fromUid = this.data.myId
        let toUid = this.data.userId

        var requestUrl = doIFollowThisUser ? "/follow/doCancelFollow" : "/follow/doFollow";
        var fanCounts  =  doIFollowThisUser ? this.data.thisFansCounts-1 :this.data.thisFansCounts+1
        var params = {
            url: requestUrl + "?fromUid=" + fromUid +
                "&toUid=" + toUid,
            method: "POST",
            callBack: (res) => {
                console.log(res)
                this.setData({
                    doIFollowThisUser: !doIFollowThisUser,
                    thisFansCounts: fanCounts
                })
                wx.showToast({
                    title: doIFollowThisUser ? '取关成功' : '关注成功',
                    icon: 'success'
                })
                wx.hideLoading();
            }
        };
        http.request(params);
    },
    //切换菜谱|动态
    onChangeTab(event) {
        const { index } = event.detail
        this.setData({
            tabIndex: index
        })
        if (this.data.tabIndex === 0) {
            this.getRecipesByThisUserId()
        } else if (this.data.tabIndex === 1) {
            this.getSharesByThisUserId()
        }
        console.log("tabIndex:", this.data.tabIndex)
    },
    //获取查看对方用户的菜谱列表
    getRecipesByThisUserId() {
        console.log('go to 对方用户的菜谱列表')
        var self = this;
        // 请求后台接口获取文章列表
        if (self.data.recipeList.length != 0 && self.data.recipeList.length === self.data.recipePage.total) {
            return
        }
        var params = {
            // 请求连接
            url: '/recipe/getRecipeListByUserId?userId=' + self.data.userId + '&isDifferOnShow=true',
            method: "POST",
            // 请求所需要的的参数
            data: self.data.recipePage,
            callBack: (resData) => {
                console.log('myRecipe-resData', resData)
                self.setData({
                    recipePage: {
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
    //获取查看对方用户的动态列表
    getSharesByThisUserId() {
        var self = this;
        // 请求后台接口获取文章列表
        if (self.data.shareList.length != 0 && self.data.shareList.length === self.data.sharePage.total) {
            return
        }
        var params = {
            // 请求连接
            url: '/user-share/getShareListByMyId?myId=' + self.data.userId,
            method: "POST",
            // 请求所需要的的参数
            data: self.data.sharePage,
            callBack: (resData) => {
                console.log('myShare-resData', resData)
                self.setData({
                    sharePage: {
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
                            shareList: self.data.shareList.concat(dataRecords),
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

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {
        this.setData({
            myId: getApp().globalData.currentUserId,//当前登录用户ID
        })
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
        //  var self = this;
        let index = this.data.tabIndex
        if (index == 0) {
            console.log('菜谱到底加页')
            if (this.data.recipeList.length != 0 && this.data.recipeList.length < this.data.recipePage.total) {
                console.log("add current")
                this.data.recipePage.current = this.data.recipePage.current + 1;
                this.getRecipesByThisUserId()
            }
        } else if (index == 1) {
            console.log('动态到底加页')
            if (this.data.shareList.length != 0 && this.data.shareList.length < this.data.sharePage.total) {
                console.log("add current")
                this.data.sharePage.current = this.data.sharePage.current + 1;
                this.getSharesByThisUserId()
            }
        }

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})