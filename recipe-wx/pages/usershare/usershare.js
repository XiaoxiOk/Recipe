// pages/usershare/usershare.js
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
var likeType = require('../../utils/likeType.js');
var testData = require('../../utils/testData.js')

Page({

    /**
     * 页面的初始数据
     */
    data: {
        resourceRootPath: config.resourceRootPath,
        myId: getApp().globalData.currentUserId, //当前登录用户ID，用于判断是否关注对方
        userShareList:[], //用户动态列表
        recommendShareList:[],
        isShowFollowButton: false,
        page:{
            current: 1,
            size: 10,
            total: 0
        },
        usershareId: 0,

    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        //获取用户动态列表 this.getUserShareList();
        // 测试数据
        this.getUserShareList();
    },
    getUserShareList(){
        var self = this;
        var params = {
            // 请求连接
            url: '/user-share/getUserShareAllByPage?fanId=' + self.data.myId,
            method: "POST",
            // 请求所需要的的参数
            data: self.data.page,
            callBack: (resData) => {
                console.log('UserShare-resData', resData)
                    self.setData({
                        page: {
                            current: resData.current,
                            size: resData.size,
                            total: resData.total
                        }
                    })
                    var dataRecords = resData.records;
                    //搜出有结果时，拼接到列表中
                    if (dataRecords) {
                        if (dataRecords.length >= 1) {
                            self.setData({
                                userShareList: self.data.userShareList.concat(dataRecords),
                            });
                        }
                    }
                    wx.hideLoading();
                


            }
        };
        http.request(params);
    },
    /*
    getTestData(){
    var userShareStr = testData.userShareStr;
    var res =  JSON.parse(userShareStr);
    var resData = res.data;
    this.setData({
        userShareList : resData.records,
        page : {
            current: resData.current,
            size: resData.size,
            total:  resData.total
        },
    })
    },
*/


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

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})