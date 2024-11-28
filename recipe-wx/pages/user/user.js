// pages/user/user.js
var http = require("../../utils/http.js");
var config = require('../../utils/config.js');
var util = require("../../utils/util.js");
var testData = require('../../utils/testData.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        resourceRootPath: config.resourceRootPath,
        userId: 3,
        mobile: "15246869746",
        userName: "嫚丽1008",
        gender: 2,
        birthday: "",
        profilePhoto: "/userAvatar/manli.jpg",
        createTime: "2023-02-14T15:32:58.000+00:00",
        myFollowsCounts: 0,
        myFansCounts: 2,
        messageCounts: 1,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        // this.getTestUserData()
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        this.setData({
            userId: getApp().globalData.currentUserId,//当前登录用户ID
        })
        this.getUserBasicInfo()
    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },
    getUserBasicInfo() {
        var self = this
        var params = {
            // 请求连接
            url: '/user/getUserById?userId=' + self.data.userId,
            method: "POST",
            // 请求所需要的的参数
            data: self.data.page,
            callBack: (resData) => {
                console.log('curUser-resData', resData)
                self.setData({
                    userId: resData.id,
                    mobile: resData.mobile,
                    userName: resData.userName,
                    gender: resData.gender,
                    birthday: resData.birthday,
                    profilePhoto: resData.profilePhoto,
                    createTime: resData.createTime,
                    myFollowsCounts: resData.myFollowsCounts,
                    myFansCounts: resData.myFollowsCounts,
                })
            }
        };
        http.request(params);
    },
    //测试数据--用户信息
    getTestUserData() {
        var userDataStr = testData.userDataStr;
        var res = JSON.parse(userDataStr);
        var resData = res.data;
        console.log(resData)
        this.setData({
            userId: 3,
            mobile: resData.mobile,
            userName: resData.userName,
            gender: resData.gender,
            birthday: resData.birthday,
            profilePhoto: resData.profilePhoto,
            createTime: resData.createTime,
            tokenInfo: resData.tokenInfo,
            userToken: resData.userToken,
            myFollowsCounts: resData.myFollowsCounts,
            myFansCounts: resData.myFollowsCounts,

        });
    },
    //点击"关注"
    toFollowListPage() {
        wx.navigateTo({
            url: '/pages/show-follow/show-follow',
        })
    },
    //点击"粉丝"
    toFanListPage() {
        wx.navigateTo({
            url: '/pages/show-fan/show-fan',
        })
    },
    //点击"我的收藏"
    toCollectPage: function () {
        wx.navigateTo({
            url: '/pages/user-collect/user-collect',
        })
    },
    //点击"我的消息"
    toMessageCenter: function () {
        wx.navigateTo({
            url: '/pages/message-index/message-index',
        })
    },
    //点击"菜谱作品集"
    toMyRecipePage: function () {
        wx.navigateTo({
            url: '/pages/show-my-recipe/show-my-recipe',
        })
    },
    //点击"我的动态"
    toMySharePage: function () {
        wx.navigateTo({
            url: '/pages/show-my-share/show-my-share',
        })
    },
    //点击"编辑个人资料"
    toUserEditPage: function () {
        let beforeInfo = {
            userId: this.data.userId,
            mobile: this.data.mobile,
            userName: this.data.userName,
            gender: this.data.gender,
            birthday: this.data.birthday,
            profilePhoto: this.data.profilePhoto         
        }
        wx.navigateTo({
            url: '/pages/user-edit/user-edit?beforeInfo=' + JSON.stringify(beforeInfo),
        })
    },
    /**
   * 退出登录
   */
    logout: function () {
        // 请求退出登陆接口
        http.request({
            url: '/user/logout',
            method: 'get',
            callBack: res => {
                util.removeTabBadge() // 移除购物车Tabbar的数字-->消息
                wx.removeStorageSync('token');

                // this.$Router.pushTab('/pages/index/index')
                wx.showToast({
                    title: "退出成功",
                    icon: "none"
                })


                setTimeout(() => {
                    wx.switchTab({
                        url: "/pages/index/index"
                    })
                }, 1000)
            }
        })
    },



})