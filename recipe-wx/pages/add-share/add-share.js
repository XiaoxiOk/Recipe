// pages/add-share/add-share.js
var http = require("../../utils/http.js");
Page({

    /**
     * 页面的初始数据
     */
    data: {
        fileList: [],
        content: "",
        uid: getApp().globalData.currentUserId,// 发布者用户ID
        mediaList: []

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

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {
        this.setData({
            uid: getApp().globalData.currentUserId,// 发布者用户ID
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

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    },
    /* 返回上一页  */
    goBackToPublish() {
        wx.navigateBack({
            delta: 1,
        })
    },
    //父组件获取子组件的值：图片/视频资源
    uploadShowImgEvent(e) {
        let fileList = e.detail.fileList

        for (var i = 0; i < fileList.length; i++) {
            this.data.mediaList[i] = {
                id: 0,
                userShareId: 0,
                coverUrl: fileList[i].coverUrl,
                fileUrl: fileList[i].fileUrl,
            }
        }
        console.log('mediaList:', this.data.mediaList)
    },
    //更新动态发表文字内容
    changeContent(e) {
        this.setData({
            content: e.detail
        })
    },
    checkInfo() {
        if (this.data.content == "" && this.data.mediaList.length == 0) {
            console.log("文案和图像不可同时不空哦！")
            wx.showToast({
                title: '没内容不可发布哦',
                icon: 'error',
                duration: 1500
            })
            return false
        }
        return true
    },
    //发布动态
    publishShare() {
        if (!this.checkInfo()) {
            console.log("审核不通过！")
            return
        }
        var userShareInfo = {
            id: 0,
            uid: this.data.uid,
            content: this.data.content,
            mediaList: this.data.mediaList
        }
        console.log('userShareInfo:', userShareInfo)
        var params = {
            url: '/user-share/addUserShareWhole',
            method: "POST",
            data: userShareInfo,
            callBack: function (resData) {
                wx.showToast({
                    title: '发布成功',
                    msg: resData,
                    icon: 'success',
                    duration: 2000
                })
                setTimeout(function () {
                    wx.redirectTo({
                        url: '/pages/show-my-share/show-my-share',
                    })
                }, 800)
            }
        };
        http.request(params);
    },

})