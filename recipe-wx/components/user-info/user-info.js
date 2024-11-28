// components/user-info/user-info.js
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        myId: {
            type: Number | null,
            value: 0
        },
        userId: {
            type: Number | null,
            value: 0
        },
        userName: {
            type: String | null,
            value: ""
        },
        profilePhoto: {
            type: String | null,
            value: ""
        },
        isUserSelf: {
            type: Boolean | null,
            value: true
        },
        doIFollowWriter: {
            type: Boolean | null,
            value: false
        },
        isShowFollowButton: {
            type: Boolean | null,
            value: false
        },
    },

    /**
     * 组件的初始数据
     */
    data: {
        resourceRootPath: config.resourceRootPath,

    },

    /**
     * 组件的方法列表
     */
    methods: {
        // 查看用户主页
        goToOtherUserPage() {
            let userId = this.properties.userId
            if (userId > 0) {
                wx.navigateTo({
                    url: '/pages/show-other-user/show-other-user?userId=' + userId,
                })
            }

        },
        //关注/取关操作
        updateFollowInfo() {
            console.log('关注/取关操作', this.data.doIFollowWriter)
            let doIFollowWriter = this.data.doIFollowWriter
            let fromUid = this.properties.myId
            let toUid = this.properties.userId
            console.log('fromUid-toUid:',fromUid, toUid)
            var requestUrl = doIFollowWriter?"/follow/doCancelFollow":"/follow/doFollow";
           
            var params = {
                url: requestUrl+"?fromUid="+fromUid+
                "&toUid="+toUid,
                method: "POST",
                callBack: (res) => {
                    console.log(res)
                    this.setData({
                        doIFollowWriter: !doIFollowWriter
                    })
                    wx.showToast({
                        title: doIFollowWriter ? '取关成功' : '关注成功',
                        icon: 'success'
                    })
                  //  wx.hideLoading();
                }
            };
            http.request(params);
        }
    }
})
