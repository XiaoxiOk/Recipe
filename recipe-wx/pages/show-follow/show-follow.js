// pages/show-follow/show-follow.js
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        myId: getApp().globalData.currentUserId,  // 当前登录用户ID 
        resourceRootPath: config.resourceRootPath,
        followList: [],
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
            followList: [],
            myId: getApp().globalData.currentUserId,  // 当前登录用户ID 
        })
        // 将显示列表数据清空
        this.getFollowList()
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
        var self = this;
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
        if (self.data.followList.length != 0 && self.data.followList.length < self.data.page.total) {
            console.log("add current")
            self.data.page.current = self.data.page.current + 1;
            self.getFollowList()
            // wx.hideLoading();
        }
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {
        this.onReachBottom()
    },
    //加载关注人用户列表
    getFollowList() {
        var self = this;
        if (self.data.followList.length != 0 && self.data.followList.length === self.data.page.total) {
            return
        }
        console.log('加载关注人用户列表')
        var params = {
            // 请求连接
            url: '/follow/getMyFollowsByPage?userId=' + self.data.myId,
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
                            followList: self.data.followList.concat(dataRecords),
                        });
                    }
                }
                wx.hideLoading();

            }
        };
        http.request(params);
    },
    //取关
    updateFollowInfo(e) {
        let index = e.currentTarget.dataset.index
        let mutual = this.data.followList[index].mutual
        console.log('取关操作')
        let fromUid = this.data.myId
        let toUid = this.data.followList[index].userId
        console.log('fromUid-toUid:', fromUid, toUid)

        var params = {
            url: "/follow/doCancelFollow" + "?fromUid=" + fromUid +
                "&toUid=" + toUid,
            method: "POST",
            callBack: (res) => {
                console.log(res)
                this.setData({
                    [`followList[${index}].mutual`]: !mutual
                })
                wx.showToast({
                    title: '取关成功',
                    icon: 'success'
                })
                this.onShow();
                //  wx.hideLoading();
            }
        };
        http.request(params);
    },
    // 查看用户主页
    goToOtherUserPage(e) {
        let index = e.currentTarget.dataset.index
        let userId = this.data.followList[index].userId
        if (userId > 0) {
            wx.navigateTo({
                url: '/pages/show-other-user/show-other-user?userId=' + userId,
            })
        }

    },
})