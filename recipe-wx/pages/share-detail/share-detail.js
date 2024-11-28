// pages/share-detail/share-detail.js
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
var commentType = require('../../utils/commentType.js');
var likeType = require('../../utils/likeType.js');
var commentType = require('../../utils/commentType.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        resourceRootPath: config.resourceRootPath,
        previewSize: "250px",
        imageFit: 'scaleToFill',
        previewFullImage: true,
        myId: getApp().globalData.currentUserId, //当前登录用户ID，用于判断是否关注对方
        myUserName: getApp().globalData.userName, //当前登录用户名
        myProfilePhoto: getApp().globalData.profilePhoto,//当前登录用户头像
        userShareId: 0, //动态ID
        userId: 0,//当前动态发布者ID
        userName: "",//用户昵称
        profilePhoto: "",  //用户头像
        content: "", //动态文案内容
        mediaList: [], //分享的图片列表
        likedCounts: 0, //被点赞总数
        commentCounts: 0, //被评论总数
        createTime: '', //发布时间
        doILikeThis: false,//是否点赞
        isShowComment: false,
        commentList: [], //评论数据列表
        commentLoadPage: {
            current: 1,
            size: 6,
            total: 0
        },
        replyLoadPage: {
            current: 1,
            size: 1,
            total: 0
        },
        isShowCmtInput: false, //是否展示“评论输入框”
        inputCmtContent: "",//用于获取输入的评论内容
        isReFresh: false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        console.log("has been in share-detail page")
        if (options) {
            this.setData({
                userShareId: parseInt(options.userShareId), // 页面传过来的动态id是string类型
            });
        }
        this.getUserShareWhole()
        // 加载评论数据
        this.getShareComments();
    },
    initLoadData() {
        var self = this
        self.data.commentLoadPage.current = 1;// 页码重新设置回1
        self.data.replyLoadPage.current = 1;
        // this.getUserShareWhole();
        self.getShareComments();

    },
    //获取全部动态信息
    getUserShareWhole() {
        wx.showLoading();
        console.log("userShareIdType:", typeof (this.data.userShareId), this.data.userShareId)
        var params = {
            url: "/user-share/getUserShareWholeById",
            method: "GET",
            data: {
                userShareId: this.data.userShareId,
                myId: this.data.myId,
            },
            callBack: (resData) => {
                console.log("res.data:", resData);
                this.setData({
                    content: resData.content, //动态文案信息
                    userId: resData.uid, //发布者用户ID
                    userName: resData.userName,//发布者用户名
                    profilePhoto: resData.profilePhoto, //发布者用户头像
                    mediaList: resData.mediaList,//动态图片组信息 
                    createTime: resData.createTime,//发布时间
                    doIFollowWriter: resData.doIFollowWriter, // 我是否关注发布者用户
                    doILikeThis: resData.doILikeThis, //我是否点赞该动态
                    likedCounts: resData.likedCounts, //动态被点赞总数
                    commentCounts: resData.commentCounts, //动态被评论总数
                });
                wx.hideLoading();
            }
        };
        http.request(params);
    },

    //分页加载动态评论
    getShareComments() {
        //  wx.showLoading();
        var self = this
        var params = {
            url: "/comment/getCommentListByPage?type=" + commentType.comment_user_share + "&typeId=" + self.data.userShareId + "&userId=" + self.data.myId,
            method: "POST",
            data: self.data.commentLoadPage,
            callBack: (resData) => {
                console.log("getShareComments res.data:", resData);

                self.setData({
                    commentLoadPage: {
                        current: resData.current,
                        size: resData.size,
                        total: resData.total
                    },

                })
                var dataRecords = resData.records;

                if (dataRecords) {
                    console.log('请求了！！isReFresh:', self.data.isReFresh)
                    if (self.data.isReFresh) {
                        console.log("清空！！")
                        self.setData({
                            commentList: []
                        })
                    }
                    //搜出有结果时，拼接到列表中
                    if (dataRecords.length >= 1) {
                        dataRecords.forEach((item) => {
                            Object.assign(item, { isShowReply: false, replyList: [] });
                        });


                        self.setData({
                            commentList: self.data.commentList.concat(dataRecords),
                            isReFresh: false
                        });
                    }
                }
                wx.hideLoading();
            }
        };
        http.request(params);
    },
    //根据评论id加载回复列表
    getReplyList(index, commentId) {
        console.log("------commentId--totalCount-:-")
        console.log(commentId, index)
        var ths = this;
        // var replyList =  this.data.commentList[index].replyList
        var params = {
            url: '/reply/getReplyListByPage?commentId=' + commentId + '&userId=' + ths.data.myId,
            method: "POST",
            data: ths.data.replyLoadPage,
            callBack: function (resData) {
                // console.log(res);
                let dataRecords = resData.records;
                console.log("res.data:", resData);
                ths.setData({
                    [`commentList[${index}].replyList`]: ths.data.commentList[index].replyList.concat(dataRecords),
                    replyLoadPage: {
                        current: resData.current,
                        size: resData.size,
                        total: resData.total
                    }
                });
            }
        };
        http.request(params);

    },
    /** 回复分页加页加载*/
    goToAddReplyPage(e) {
        this.data.replyLoadPage.current = this.data.replyLoadPage.current + 1;
        let index = e.currentTarget.dataset.index
        let commentId = this.data.commentList[index].id
        this.getReplyList(index, commentId)
    },
    /** 添加或者取消点赞该动态 */
    addOrCannelLike() {
        var requestUrl;
        if (this.data.doILikeThis) {
            requestUrl = "/like/unlike";
        } else {
            requestUrl = "/like/doLike?msgToUid=" + this.data.userId;
        }
        wx.showLoading();
        var params = {
            url: requestUrl,
            method: "POST",
            data: {
                id: 0,
                type: likeType.like_user_share,
                typeId: this.data.userShareId,
                userId: this.data.myId
            },
            callBack: (res) => {
                var count;//用于更新likedCounts
                if (this.data.doILikeThis) {
                    count = this.data.likedCounts - 1
                } else {
                    count = this.data.likedCounts + 1
                }
                this.setData({
                    doILikeThis: !this.data.doILikeThis,
                    likedCounts: count
                })
                wx.hideLoading();
            }
        };
        http.request(params);

    },
    //点击展开回复
    goToShowReply(e) {
        let index = e.currentTarget.dataset.index
        let commentId = this.data.commentList[index].id
        this.getReplyList(index, commentId)
        this.setData({
            [`commentList[${index}].isShowReply`]: true
        });

    },
    //展示对动态[添加评论]对话框
    showInputCmtDiag() {
        this.setData({
            inputCmtContent: "",
            isShowCmtInput: true,
        })
    },
    //关闭[添加评论]对话框
    closeShowCmtInput() {
        this.setData({
            isShowCmtInput: false
        })
    },
    //添加对该动态的顶层评论
    toAddCmtInfo() {
        if (this.data.inputCmtContent === "" || this.data.inputCmtContent.length <= 0) {
            wx.showToast({
                title: '内容不可为空',
                icon: "error"
            })
            return;
        }
        let cmtObj = {
            id: 0,
            content: this.data.inputCmtContent,
            fromUid: this.data.myId,
            topicType: commentType.comment_user_share,//当前为动态
            topicId: this.data.userShareId,//当前为动态ID
        }
        let msgToUid = this.data.userId //当前动态的发布者ID
        var self = this
        var params = {
            url: '/comment/addComment?msgToUid=' + msgToUid,
            method: "POST",
            data: cmtObj,
            callBack: function (resData) {
                console.log("res.data:", resData);
                resData.userName = self.data.myUserName
                resData.profilePhoto = self.data.myProfilePhoto
                self.data.commentList.unshift(resData);
                self.data.commentLoadPage.total = self.data.commentLoadPage.total + 1
                wx.showToast({
                    title: '评论成功!',
                })
                self.setData({
                    isShowCmtInput: false,
                    commentList: self.data.commentList,
                    commentCounts: self.data.commentCounts + 1
                })
            }
        };
        http.request(params);
    },
    //获取点击回复-进行回复的内容，并添加回复
    toAddReplyInCmt(e) {
        var self = this
        let basicReplyInfo = e.detail.basicReplyInfo
        let replyInfo = {
            id: 0,
            fromUid: self.data.myId,
            commentId: basicReplyInfo.commentId,
            replyId: basicReplyInfo.replyId,
            toUid: basicReplyInfo.toUid,
            content: basicReplyInfo.content,
            toUserName: basicReplyInfo.toUserName,
        }
        console.log('父UserShare-toAddReplyInCmt:', replyInfo)
        var params = {
            url: '/reply/addReply?type=' + commentType.comment_user_share + '&typeId=' + self.data.userShareId,
            method: "POST",
            data: replyInfo,
            callBack: function (resData) {
                // console.log(res);
                console.log("res.data:", resData);
                resData.fromUserName = self.data.myUserName
                resData.profilePhoto = self.data.myProfilePhoto
                resData.toUserName = replyInfo.toUserName
                let commentList = self.data.commentList
                for (let i = 0; i < commentList.length; i++) {
                    let item = commentList[i]
                    if (item.id == replyInfo.commentId) {
                        item.repliedCounts = item.repliedCounts + 1
                        if (item.replyList.length > 0) {
                            item.replyList.unshift(resData)
                            if (item.replyList.length < item.repliedCounts) {
                                item.isShowReply = true
                            }
                        }
                    }
                }
                self.setData({
                    commentList: commentList,
                    commentCounts: self.data.commentCounts + 1
                })
                wx.showToast({
                    title: '回复成功!',
                })

            }
        };
        http.request(params);
    },
    //获取点击评论Item，完成输入的回复内容
    toAddNewReply(e) {
        var self = this
        let basicReplyInfo = e.detail.basicReplyInfo
        let replyInfo = {
            id: 0,
            fromUid: self.data.myId,
            commentId: basicReplyInfo.commentId,
            replyId: basicReplyInfo.replyId,
            toUid: basicReplyInfo.toUid,
            content: basicReplyInfo.content,
            toUserName: basicReplyInfo.toUserName,
        }
        console.log('父UserShare-toAddNewReply:', replyInfo)
        var params = {
            url: '/reply/addReply?type=' + commentType.comment_user_share + '&typeId=' + self.data.userShareId,
            method: "POST",
            data: replyInfo,
            callBack: function (resData) {
                console.log("res.data:", resData);
                resData.fromUserName = self.data.myUserName
                resData.profilePhoto = self.data.myProfilePhoto
                resData.toUserName = replyInfo.toUserName
                let commentList = self.data.commentList
                for (let i = 0; i < commentList.length; i++) {
                    let item = commentList[i]
                    if (item.id == replyInfo.commentId) {
                        item.repliedCounts = item.repliedCounts + 1
                        if (item.replyList.length > 0) {
                            item.replyList.unshift(resData)
                            if (item.replyList.length < item.repliedCounts) {
                                item.isShowReply = true
                            }
                        }
                    }
                }
                self.setData({
                    commentList: commentList,
                    commentCounts: self.data.commentCounts + 1
                })
                wx.showToast({
                    title: '回复成功!',
                })
            }
        };
        http.request(params);
    },
    //删除评论信息
    toDeleteComment(e) {
        var self = this
        let commentId = e.detail.commentId
        let indexToDel = e.detail.index
        console.log("indexToDel:", indexToDel)
        let objToDelCmt = {
            id: commentId,
            topicType: commentType.comment_user_share,//当前为动态
            topicId: self.data.userShareId,//当前为动态ID
        }
        var params = {
            url: '/comment/deleteComment',
            method: "POST",
            data: objToDelCmt,
            callBack: function (resData) {
                // console.log(res);
                console.log("res.data:", resData);
                let list = self.data.commentList
                list.splice(indexToDel, 1);
                self.data.commentLoadPage.total = self.data.commentLoadPage.total - 1
                self.setData({
                    commentList: list,
                    commentCounts: self.data.commentCounts - 1
                })
                wx.showToast({
                    title: '删除成功!',
                })

            }
        };
        http.request(params);

    },
    //删除回复信息
    toDeleteReply(e) {
        var self = this
        let indexToDel = e.detail.index
        let replyId = e.detail.replyId
        let commentId = e.detail.commentId
        var params = {
            url: '/reply/deleteReply?replyId=' + replyId + '&commentId=' + commentId,
            method: "GET",
            callBack: function (resData) {
                // console.log(res);
                console.log("res.data:", resData);
                let commentList = self.data.commentList
                for (let i = 0; i < commentList.length; i++) {
                    let item = commentList[i]
                    if (item.id == commentId && item.replyList.length > 0) {
                        var length = item.replyList.splice(indexToDel, 1);
                        item.repliedCounts = item.repliedCounts - 1
                        // 
                        self.data.replyLoadPage.current = self.data.replyLoadPage.current - 1
                        if (item.repliedCounts == length) {
                            item.isShowReply = false
                        }
                    }
                }
                self.setData({
                    commentList: commentList
                })
                wx.showToast({
                    title: '删除成功!',
                })

            }
        };
        http.request(params);

    },

    //预览图片
    onPreviewImage: function (event) {
        console.log('event:', event)
        if (!this.data.previewFullImage)
            return;
        var index = event.currentTarget.dataset.index;
        var _a = this.data, lists = _a.mediaList;
        var itemImg = lists[index];
        wx.previewImage({
            current: _a.resourceRootPath + itemImg.fileUrl,
            urls: lists.filter(item => item.coverUrl == item.fileUrl).map(function (item) { return _a.resourceRootPath + item.fileUrl; }),
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
        var lists = this.data.mediaList;
        var resourceRootPath = this.data.resourceRootPath
        var sources = [];
        var current = lists.reduce(function (sum, cur, curIndex) {
            if (cur.coverUrl == cur.fileUrl) {
                return sum;
            }
            sources.push({
                type: 'video',
                url: resourceRootPath + lists[index].fileUrl,
                poster: resourceRootPath + lists[index].coverUrl
            });
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
            myId: getApp().globalData.currentUserId, //登录用户ID 
            myUserName:getApp().globalData.userName, //当前登录用户名
            myProfilePhoto:getApp().globalData.profilePhoto,//当前登录用户头像
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
        console.log(" isReFresh: false!!")
        this.data.isReFresh = true
        this.initLoadData()
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        if (this.data.commentList.length != 0 && this.data.commentList.length < this.data.commentLoadPage.total) {
            console.log("add current")
            this.data.commentLoadPage.current = this.data.commentLoadPage.current + 1;
            this.getShareComments()
        }
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})