// pages/user-edit/user-edit.js
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
var storeUser = require('../../utils/storeUser.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        userId:0,//用于更新信息的用户ID
        beforeInfo:{},//用于判断用户是否进行了修改
        coverMediaShowList:[],//展示当前已上传的文件
        mobile: "",
        avatarUrl: "",//用户头像
        userName: "",//用户姓名
        gender: 0,//用户性别
        birthday: 0, //用户生日
        showDatePicker: false, //时间选择器弹窗标识
        currentDate: new Date().getTime(),
        maxDate: new Date().getTime(),
        minDate: 0,
        formatter(type, value) {
            if (type === 'year') {
                return `${value}年`;
            }
            if (type === 'month') {
                return `${value}月`;
            }
            if (type === 'day') {
                return `${value}日`;
            }
            return value;
        },
        genderOption: [
            { text: '请选择性别', value: -1 },
            { text: '隐藏', value: 0 },
            { text: '男', value: 1 },
            { text: '女', value: 2 }
        ],
    },
   //父组件获取子组件的值：图片/视频资源
   uploadShowImgEvent(e) {
    let fileList = e.detail.fileList
    this.data.avatarUrl = fileList[0].fileUrl,
        console.log('avatarUrl:', this.data.avatarUrl)
},
//设置用户名
setUserName(e) {
    this.setData({
        userName: e.detail.split('\n').join('')
    })
},
onChangeGender({ detail }) {
    this.setData({ gender: detail });
},
//显示生日选择器
showDatePicker() {
    const dateTime =  new Date(this.data.birthday).getTime()
    this.setData({ showDatePicker: true,
        currentDate : dateTime });
},
//选择一个日期时触发
onInput(event) {
    this.setData({
        currentDate: event.detail,
    });
},
//设置生日
setSelectedDate(event) {
    this.setData({
        currentDate: event.detail,
        birthday: event.detail,
        showDatePicker: false
    });
},
//关闭生日选择器
onCloseDatePicker() {
    this.setData({ showDatePicker: false });
},
//父组件获取子组件的值：头像
uploadAvatarEvent(e) {
    let fileList = e.detail.fileList;
    if (fileList.length == 1) {
        this.data.avatarUrl = e.detail.fileList[0].fileUrl
    } else if (fileList.length == 0) {
        this.data.avatarUrl = ''
    }
},
//设置用户信息
setUserInfo() {
    if(this.data.avatarUrl == ""){
        wx.showToast({
            title: '请上传头像',
            icon:'error'
          })
          return 
    }
    if(this.data.userName == ""){
        wx.showToast({
            title: '请输入昵称',
            icon:'error'
          })
          return 
    }
    if(this.data.gender==-1){
        wx.showToast({
          title: '请选择性别',
          icon:'error'
        })
        return 
    }
    if(this.data.birthday == 0){
        wx.showToast({
            title: '请选择生日',
            icon:'error'
          })
          return 
    }
    if(this.data.mobile != this.data.beforeInfo.mobile){
        wx.showToast({
            title: '请检查登录状态',
            icon:'error'
          })
          return 
    }
    if(this.data.mobile != this.data.beforeInfo.mobile){
        wx.showToast({
            title: '请检查登录状态',
            icon:'error'
          })
          return 
    }
    if(this.checkIsChange() === false){
        wx.showToast({
            title: '未作出任何修改',
            icon:'error'
          })
          return 
    }
    var userBO = {
        id:this.data.userId,
        mobile: this.data.mobile,
        userName: this.data.userName,
        gender: this.data.gender,
        birthday: this.data.birthday,
        profilePhoto: this.data.avatarUrl
    }
    console.log('修改的用户信息：', userBO)
    var ths = this
    var params = {
        url: '/user/updateUserById',
        method: "POST",
        data: userBO,
        callBack: function (resData) {
            console.log("update-userInfo-resData:",resData) 
              //  storeUser.setGlobalUserInfo(resData)
                wx.showToast({
                    title: '修改成功！',
                    icon: 'success',
                    duration: 2000
                })
                setTimeout(function () {
                    wx.redirectTo({
                        url: '/pages/user/user',
                    })
                }, 500)
            }
    };
    http.request(params);

},
//判断用户是否更新数据
checkIsChange(){
    let beforeInfo = this.data.beforeInfo
    if(this.data.profilePhoto != beforeInfo.profilePhoto){
        return true;
    }else if(this.data.userName != beforeInfo.userName){
        return true;
    }else if(this.data.gender != beforeInfo.gender){
        return true;
    }else if(this.data.birthday != beforeInfo.birthday){
        return true;
    }else if(this.data.mobile != beforeInfo.mobile){
        return true;
    }
    return false
},
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        if (options) {
            let beforeInfo = JSON.parse(options.beforeInfo)
            console.log("beforeInfo",beforeInfo)
            this.data.beforeInfo = beforeInfo
           
            this.setData({
                userId:beforeInfo.userId,
                gender: beforeInfo.gender,
                mobile:beforeInfo.mobile,
                birthday: beforeInfo.birthday,
                userName: beforeInfo.userName,
                avatarUrl: beforeInfo.profilePhoto,
                showDatePicker: false, //时间选择器弹窗标识
                coverMediaShowList: [beforeInfo.profilePhoto]
            })
            this.setMediaShowList();
        }
    },
    //加载对应图片组件的展示图
    setMediaShowList() {
        let self = this
        let avatarInfo = {
            type: 'image',
            url: config.resourceRootPath + self.data.avatarUrl,
            fromType: 'recipeCover'
        }
        self.setData({
            coverMediaShowList: [avatarInfo],

        })
        // 初始化获取封面组件
        self.selectComponent('#avatarUploadImg').showFileInit()
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