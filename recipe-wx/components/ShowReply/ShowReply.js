// components/ShowReply/ShowReply.js
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
var likeType = require('../../utils/likeType.js');
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        replyList: Array,
        upUserId: Number, //当前查看菜谱/动态的发布者ID
    },
    lifetimes: {
        //组件中的onLoad
        attached: function (e) {
            let loginUserId = getApp().globalData.currentUserId
            this.setData({
                myId: loginUserId
            })

        }
    },
    /**
     * 页面的初始数据
     */
    data: {
        myId: 0,
        resourceRootPath: config.resourceRootPath,
        isShowInput: false, //是否展示“回复输入框”
        inputReplyContent: "",//用于获取输入的回复内容
        isShowDelete: false, //是否展示“删除”遮罩层
        //评论回复对象
        basicReply: {
            commentId: 0, //当前所在顶层评论ID
            replyId: 0, //实际回复对象ID
            toUid: 5,
            content: "",
            toUserName: "",
        },
        toDelReplyObj: {
            index: 0, //用于记录当前索引
            toDelReplyId: 0,// 用于记录删除回复
            parentCommentId: 0//所在顶层评论ID
        }
    },

    ready: function () {
        // this._getReplyList()
    },
    /**
       * 组件的方法列表
       */

    methods: {
        //点击评论展示“回复输入框”
        showInputDiag(e) {
            let index = e.currentTarget.dataset.index
            var replyInfo = this.data.basicReply
            replyInfo.commentId = this.properties.replyList[index].commentId
            replyInfo.replyId = this.properties.replyList[index].id
            replyInfo.toUid = this.properties.replyList[index].fromUid
            replyInfo.toUserName = this.properties.replyList[index].fromUserName
            this.setData({
                inputReplyContent: "",
                isShowInput: true,
                basicReply: replyInfo
            })
        },
        //关闭[添加回复]对话框
        onCloseShowInput() {
            this.setData({
                isShowInput: false
            })
        },
        // 查看用户主页
        goToOtherUserPage(e) {
            let index = e.currentTarget.dataset.index
            let userId = this.properties.replyList[index].fromUid
            console.log('want to userPage:',userId)
            if (userId > 0) {
                wx.navigateTo({
                    url: '/pages/show-other-user/show-other-user?userId=' + userId,
                })
            }

        },
        //向父组件发送新的回复信息
        toSendReplyInfo: function (e) {
            var self = this;
            let inputContent = self.data.inputReplyContent
            if(inputContent === "" || inputContent.length<=0){
                wx.showToast({
                  title: '内容不可为空',
                  icon:"error"
                })
                return ;
            }
            self.data.basicReply.content = inputContent
            var myEventDetail = {
                basicReplyInfo: self.data.basicReply
            } // detail对象，提供给事件监听函数
            this.triggerEvent('toAddReply', myEventDetail) //toSendReplyInfo自定义名称事件，父组件中使用
            this.setData({
                isShowInput: false
            })
        },
        //打开显示[删除]遮罩层
        showDeleteDiag(e) {
            let index = e.currentTarget.dataset.index
            let replyId = this.properties.replyList[index].id
            let commentId = this.properties.replyList[index].commentId
            let replyFromUid = this.properties.replyList[index].fromUid
            console.log('myId-replyFromUid-upUserId', this.data.myId, replyFromUid, this.properties.upUserId)
            if (this.data.myId === replyFromUid || this.data.myId === this.properties.upUserId) {
                this.setData({
                    toDelReplyObj: {
                        index: index,
                        toDelReplyId: replyId,// 用于记录删除回复
                        parentCommentId: commentId//所在顶层评论ID
                    },
                    isShowDelete: true
                })
                console.log('has set isShowDelete: true')
            } else {
                console.log('不能删除-无权限')
            }

        },
        //关闭[删除]遮罩层
        closeShowDelete() {
            this.setData({
                isShowDelete: false
            })
        },
        //更新点赞状态
        updateDoIlikeThis: function (e) {
            let index = e.currentTarget.dataset.index
            console.log("index->", index)
            let doILikeThis = this.data.replyList[index].doILikeThis
            let fromUid = this.data.replyList[index].fromUid
            let replyId = this.data.replyList[index].id
            let oldCounts = this.data.replyList[index].likedCounts

            var requestUrl;
            if (doILikeThis) {
                requestUrl = "/like/unlike";
            } else {
                requestUrl = "/like/doLike?msgToUid=" + fromUid;
            }

            // wx.showLoading();
            var params = {
                url: requestUrl,
                method: "POST",
                data: {
                    id: 0,
                    type: likeType.like_reply,
                    typeId: replyId,
                    userId: this.data.myId
                },
                callBack: (res) => {
                    var newCounts;//用于更新likedCounts
                    if (doILikeThis) {
                        newCounts = oldCounts - 1
                    } else {
                        newCounts = oldCounts + 1
                    }
                    this.setData({
                        [`replyList[${index}].doILikeThis`]: !doILikeThis,
                        [`replyList[${index}].likedCounts`]: newCounts
                    })
                    //   wx.hideLoading();
                }
            };
            http.request(params);
        },
        //向父组件发送要删除的评论信息
        toDelReply: function (e) {
            var myEventDetail = {
                index: this.data.toDelReplyObj.index,
                replyId: this.data.toDelReplyObj.toDelReplyId,
                commentId: this.data.toDelReplyObj.parentCommentId
            } // detail对象，提供给事件监听函数
            this.triggerEvent('toDelReply', myEventDetail)
        },
    },

})