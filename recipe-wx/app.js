//app.js
var http = require("utils/http.js");
App({
  onLaunch: function () {
    // http.getToken();
    // wx.getSetting({
    //   success(res) {
    //     if (!res.authSetting['scope.userInfo']) {
    //       wx.navigateTo({
    //         url: '/pages/login/login',
    //       })
    //     }
    //   }
    // })
  },
  globalData: {
    // 定义全局请求队列
    requestQueue: [],
    // 是否正在进行登陆
    isLanding: true,
    // 购物车商品数量 -->消息数量
    totalCartCount: 0,
    tokenInfo:{},
    userToken:"",
    //当前登录用户ID
    currentUserId: 3,
    userName:"",//id=3 "嫚丽1008"
    profilePhoto:"",///userAvatar/manli.jpg
  }
})