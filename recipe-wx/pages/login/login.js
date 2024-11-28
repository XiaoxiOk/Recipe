var http = require("../../utils/http.js");
var storeUser = require('../../utils/storeUser.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        // 用户名
        mobile: '',
        mobileInput: '',
        errorMsg: '',
        isShow: false,//是否展示弹窗
    },
    //判断用户是否已注册，若已注册直接登录，若未则跳转到信息录入界面
    judgeRegisterAndLogin() {
        var self = this
        let mobile = this.data.mobileInput
        if (this.checkModbile(mobile)) {
            console.log("go to request:", mobile)
            var params = {
                url: '/user/judgeIsRegisterAndLogin?mobile=' + mobile,
                method: "POST",
                callBack: function (resData) {
                    console.log(resData)
                    self.onClose()
                    if (resData.hasOwnProperty("needToRegister")) {
                        if (resData.needToRegister) {
                            wx.showToast({
                                title: '需要完善信息',
                                icon: "none",
                                duration: 2000
                            })
                            setTimeout(function () {
                                wx.redirectTo({
                                    url: '/pages/register-info/register-info?mobile=' + mobile,
                                })
                            }, 500)
                        }
                    } else if (resData.hasOwnProperty("id")) {


                        storeUser.setGlobalUserInfo(resData)
                        wx.showToast({
                            title: '登录成功',
                            msg: resData,
                            icon: 'success',
                            duration: 2000
                        })
                        setTimeout(function () {
                            wx.redirectTo({
                                url: '/pages/index/index',
                            })
                        }, 500)
                    }


                }

            };
            http.request(params);
        }

    },
    checkModbile(mobile) {
        var re = /^1[3,4,5,6,7,8,9][0-9]{9}$/;
        if (!re.test(mobile)) {
            return false;//若手机号码格式不正确则返回false
        }
        return true;
    },
    bind_mobile() {
        if (this.checkModbile(this.data.mobileInput)) {
            this.setData({
                errorMsg: ""
            })
        } else {
            this.setData({
                errorMsg: "手机号格式不正确"
            })

        }
    },
    onGotUserInfo: function (res) {
        http.updateUserInfo();
        wx.navigateBack({
            delta: 1
        })
    },
    showPopUp() {
        this.setData({
            isShow: true
        })
    },
    onClose() {
        this.setData({
            isShow: false
        })

    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

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

    /**
     * 输入框的值
     */
    getInputVal: function (e) {
        const type = e.currentTarget.dataset.type
        if (type == 'account') {
            this.setData({
                userName: e.detail.value
            })
        } else if (type == 'password') {
            this.setData({
                password: e.detail.value
            })
        }
    },
    /* 回到首页 */
    toHomePage: function () {
        wx.switchTab({
            url: '/pages/index/index',
        })
    },
    getPhoneNumber: function (e) {
        console.log(e)
        console.log(e.detail.errMsg)
        console.log(e.detail.iv)
        console.log(e.detail.encryptedData)
    },
    /**
     * 注册/登录按钮
     */
    handleLoginOrRegister() {
        const that = this
    }
})
