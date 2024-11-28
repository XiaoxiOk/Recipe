// components/share-card/share-card.js
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
var likeType = require('../../utils/likeType.js');
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        item: Object,
        myId:{
            type: Number | null,
            value: 0
        },
        isUserSelf: Boolean
    },

    /**
     * 组件的初始数据
     */
    data: {
        previewSize:"250px",
        imageFit:'scaleToFill',
        previewFullImage: true,
        resourceRootPath: config.resourceRootPath,
        userInfo: Object
    },
    observers:{
        'item':function(item){
            this.setData({
                userInfo:{
                    userId: item.uid,
                    userName: item.userName,
                    profilePhoto: item.profilePhoto,
                    isUserSelf:item.isUserSelf,
                    doIFollowWriter: item.doIFollowWriter
                }
            })
        },
    },
    /**
     * 组件的方法列表
     */
    methods: {
        //去往动态详情
        goToShareDetail(e){
            var userShareId = e.currentTarget.dataset.usershareid;
            if (userShareId > 0) {
                wx.navigateTo({
                    url: '/pages/share-detail/share-detail?userShareId=' + userShareId,
                })
            }
        },
        //更新点赞信息
        updateLikeShare: function () {
            let doILikeThis = this.data.item.doILikeThis
            let fromUid = this.data.item.uid
            let shareId = this.data.item.id
            let oldCounts = this.data.item.likedCounts

            var requestUrl;
            if (doILikeThis) {
                requestUrl = "/like/unlike";
            } else {
                requestUrl = "/like/doLike?msgToUid=" + fromUid;
            }

            var params = {
                url: requestUrl,
                method: "POST",
                data: {
                    id: 0,
                    type: likeType.like_user_share,
                    typeId: shareId,
                    userId: this.properties.myId
                },
                callBack: (res) => {
                    var newCounts;//用于更新likedCounts
                    if (doILikeThis) {
                        newCounts = oldCounts - 1
                    } else {
                        newCounts = oldCounts + 1
                    }
                    this.setData({
                        [`item.doILikeThis`]: !doILikeThis,
                        [`item.likedCounts`]: newCounts
                    })
                    wx.hideLoading();
                }
            };
            http.request(params);
        },
        //预览图片
        onPreviewImage: function (event) {
            console.log('event:',event)
            if (!this.data.previewFullImage)
                return ;
            var index = event.currentTarget.dataset.index;
            var _a = this.data, lists = _a.item.mediaList;
            var itemImg = lists[index];
            wx.previewImage({
                current: _a.resourceRootPath + itemImg.fileUrl,
                urls: lists.filter(item=>item.coverUrl == item.fileUrl).map(function (item) { return _a.resourceRootPath + item.fileUrl; }),
                showmenu: true, //是否显示长按菜单。
                fail: function () {
                    wx.showToast({ title: '预览图片失败', icon: 'none' });
                },
            });
        },
        //预览视频
        onPreviewVideo: function (event) {
            if (!this.data.previewFullImage)
                return;
            var index = event.currentTarget.dataset.index;
            var lists = this.properties.item.mediaList;
            var  resourceRootPath = this.data.resourceRootPath
            var sources = [];
            var current = lists.reduce(function (sum, cur, curIndex) {
                if (cur.coverUrl==cur.fileUrl) {
                    return sum;
                }
                sources.push( { type: 'video', 
                url:resourceRootPath + lists[index].fileUrl,
                poster:resourceRootPath + lists[index].coverUrl});
                if (curIndex < index) {
                    sum++;
                }
                return sum;
            }, 0);
            wx.previewMedia({
                sources: sources,
                current: current,
                fail: function () {
                    wx.showToast({ title: '预览视频失败', icon: 'none' });
                },
            });
        },
      
    }
})
