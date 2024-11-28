function setGlobalUserInfo(UserVO){
    var app = getApp()
    var app = getApp();
    app.globalData.currentUserId = UserVO.id
    app.globalData.userName = UserVO.userName
    app.globalData.profilePhoto = UserVO.profilePhoto
    app.globalData.userToken = UserVO.userToken
    app.globalData.tokenInfo = UserVO.tokenInfo
    wx.setStorageSync('token', UserVO.userToken);
    console.log("globalData---->", app.globalData)
}

exports.setGlobalUserInfo = setGlobalUserInfo 