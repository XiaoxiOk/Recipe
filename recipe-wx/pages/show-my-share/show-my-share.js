// pages/show-my-share/show-my-share.js
var config = require('../../utils/config.js');
var http = require('../../utils/http.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        resourceRootPath: config.resourceRootPath,
        shareList: [],
        page: {
            current: 1,
            size: 5,
            total: 0
        },
        myId: getApp().globalData.currentUserId,  // 当前登录用户ID
        isShowHandle: false, // 是否显示操作遮罩层
        toHandleShareId: 0,//将要操作的菜谱ID
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
    },
    //重置数据信息
    reFreshData() {
        this.data.page.current = 1;// 页码重新设置回1
        this.setData({
            shareList: [],
            myId: getApp().globalData.currentUserId,  // 当前登录用户ID
        })
        // 加载我的动态作品列表
        this.getMyShareList();
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
        this.reFreshData();
        wx.stopPullDownRefresh();
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        var self = this;
        if (self.data.shareList.length != 0 && self.data.shareList.length < self.data.page.total) {
            console.log("add current")
            
            self.data.page.current = self.data.page.current + 1;
            console.log('current:',self.data.page.current)
            self.getMyShareList()
        }
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    },
    //加载我的动态作品列表
    getMyShareList() {
        var self = this;
        // 请求后台接口获取文章列表
        if (self.data.shareList.length != 0 && self.data.shareList.length === self.data.page.total) {
            return
        }
        var params = {
            // 请求连接
            url: '/user-share/getShareListByMyId?myId=' + self.data.myId,
            method: "POST",
            // 请求所需要的的参数
            data: self.data.page,
            callBack: (resData) => {
                console.log('myShare-resData', resData)
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
                            shareList: self.data.shareList.concat(dataRecords),
                        });
                    }
                    wx.hideLoading();
                }
            }
        };
        http.request(params);
    },
    //关闭[操作]遮罩层
    showHandle(e) {
        console.log('long tap!!')
        let index = e.currentTarget.dataset.index
        let shareId = this.data.shareList[index].id

        this.setData({
            toHandleShareId: shareId,
            isShowHandle: true
        })
    },
    //关闭[操作]遮罩层
    closeShowHandle() {
        this.setData({
            isShowHandle: false
        })
    },
    //删除动态
    toDelShare() {
        var self = this
        console.log('删除动态ID->', self.data.toHandleShareId)
        var params = {
            url: '/user-share/deleteUserShareWhole?userShareId=' + self.data.toHandleShareId,
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