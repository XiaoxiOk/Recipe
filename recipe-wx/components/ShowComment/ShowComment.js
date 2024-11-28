// components/ShowComment/ShowComment.js
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
var likeType = require('../../utils/likeType.js');

Component({
    options: {
        multipleSlots: true // 复数插槽: 是
    },
    /**
     * 组件的属性列表
     */
    properties: {
        count: {
            type: Number,
            value: 1
        },
        commentList: Array,
        upUserId: Number,//当前查看菜谱/动态的发布者ID

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
        myId: 0, //用于判断是否为博主或楼主->可以删除评论
        //评论回复对象
        basicReply: {
            commentId: 0, //当前所在顶层评论ID
            replyId: 0, //实际回复对象ID
            toUid: 5,
            content: "",
            toUserName: "",
        },
        index: 0, //操作对象序号
        isShowInput: false, //是否展示“回复输入框”
        isShowDelete: false, //是否展示“删除”遮罩层
        inputReplyContent: "",//用于获取输入的回复内容
        resourceRootPath: config.resourceRootPath,
        toDelCommentId: 0,// 用于记录删除评论
    },

    methods: {
        //点击评论展示“回复输入框”
        showInputDiag(e) {
            let index = e.currentTarget.dataset.index
            var replyInfo = this.data.basicReply
            replyInfo.commentId = this.properties.commentList[index].id
            replyInfo.replyId = this.properties.commentList[index].id
            replyInfo.toUid = this.properties.commentList[index].fromUid
            replyInfo.toUserName = this.properties.commentList[index].userName
            this.setData({
                index: index,
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
        //打开显示[删除]遮罩层
        showDeleteDiag(e) {
            let index = e.currentTarget.dataset.index
            let commentId = this.properties.commentList[index].id
            let cmtFromUid = this.properties.commentList[index].fromUid
            console.log('myId-cmtFromUid-upUserId', this.data.myId, cmtFromUid, this.properties.upUserId)
            if (this.data.myId === cmtFromUid || this.data.myId === this.properties.upUserId) {

                this.setData({
                    index: index,
                    toDelCommentId: commentId,
                    isShowDelete: true
                })
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
        // 查看用户主页
        goToOtherUserPage(e) {
            let index = e.currentTarget.dataset.index
            let userId = this.properties.commentList[index].fromUid
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
            }else{
                self.data.basicReply.content = inputContent
                var myEventDetail = {
                    index: self.data.index,
                    basicReplyInfo: self.data.basicReply
                } // detail对象，提供给事件监听函数
                this.triggerEvent('toSendReplyInfo', myEventDetail) //toSendReplyInfo自定义名称事件，父组件中使用
                this.setData({
                    isShowInput: false
                })
            }
           
        },
        //向父组件发送要删除的评论信息
        toDelComment: function (e) {
            var myEventDetail = {
                index: this.data.index,
                commentId: this.data.toDelCommentId
            } // detail对象，提供给事件监听函数
            this.triggerEvent('toDelComment', myEventDetail)
        },
        //更新点赞状态
        updateDoIlikeThis: function (e) {
            let index = e.currentTarget.dataset.index
            console.log("index->", index)
            let doILikeThis = this.data.commentList[index].doILikeThis
            let fromUid = this.data.commentList[index].fromUid
            let commentId = this.data.commentList[index].id
            let oldCounts = this.data.commentList[index].likedCounts

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
                    type: likeType.like_comment,
                    typeId: commentId,
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
                        [`commentList[${index}].doILikeThis`]: !doILikeThis,
                        [`commentList[${index}].likedCounts`]: newCounts
                    })
                    //   wx.hideLoading();
                }
            };
            http.request(params);
        }

    }
})