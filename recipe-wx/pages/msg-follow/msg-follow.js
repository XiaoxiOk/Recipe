// pages/msg-follow/msg-follow.js
const http = require('../../utils/http.js');
const config = require('../../utils/config.js');
Page({
    /**
     * 页面的初始数据
     */
    data: {
        myId: getApp().globalData.currentUserId,  // 当前登录用户ID 
        resourceRootPath: config.resourceRootPath,
        followMsgList: [],
        isShowHandle: false, // 是否显示操作遮罩层
        toHandleMsgId: 0,//将要操作的消息ID
        page: {
            current: 1,
            size: 8,
            total: 0
        },
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
            followMsgList: [],
            myId: getApp().globalData.currentUserId,  // 当前登录用户ID
        })
        // 将显示列表数据清空
        this.getFollowMsgList()
    },
    //显示[操作]遮罩层
    showHandle(e) {
        let msgId = e.currentTarget.dataset.msgid
        console.log('long tap!! MsgId:', msgId)
        this.setData({
            toHandleMsgId: msgId,
            isShowHandle: true
        })
    },
    //关闭[操作]遮罩层
    closeShowHandle() {
        this.setData({
            isShowHandle: false
        })
    },
    //删除消息
    toDelMsg() {
        console.log('删除消息ID->', this.data.toHandleMsgId)
        var self = this
        var params = {
            url: '/message/deleteMsgById?msgId=' + self.data.toHandleMsgId,
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
        console.log('show-follow-msg')
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
        console.log("onReachBottom")
        //已加载完毕时不再去请求
        if (self.data.followMsgList.length != 0 && self.data.followMsgList.length < self.data.page.total) {
            console.log("add current")
            self.data.page.current = self.data.page.current + 1;
            self.getFollowMsgList()
            // wx.hideLoading();
        }
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    },
    //加载关注人用户列表
    getFollowMsgList() {
        var self = this;
        if (self.data.followMsgList.length != 0 && self.data.followMsgList.length === self.data.page.total) {
            return
        }
        console.log('加载新增关注消息列表')
        var params = {
            // 请求连接
            url: '/message/getFollowMsgList?userId=' + self.data.myId,
            method: "POST",
            // 请求所需要的的参数
            data: self.data.page,
            callBack: (resData) => {
                console.log('follow-resData', resData)
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
                            followMsgList: self.data.followMsgList.concat(dataRecords),
                        });
                    }
                }
                wx.hideLoading();

            }
        };
        http.request(params);
    },

    // 查看用户主页
    goToOtherUserPage(e) {
        let index = e.currentTarget.dataset.index
        let userId = this.data.followMsgList[index].userId
        if (userId > 0) {
            wx.navigateTo({
                url: '/pages/show-other-user/show-other-user?userId=' + userId,
            })
        }

    },
})