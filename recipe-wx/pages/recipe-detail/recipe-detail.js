// pages/recipe-detail/recipe-detail.js
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
var commentType = require('../../utils/commentType.js');
var likeType = require('../../utils/likeType.js');
var testData = require('../../utils/testData.js')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        isShowComment: false,
        commentList: [], //评论数据列表
        myId: getApp().globalData.currentUserId, //登录用户ID 
        myUserName: getApp().globalData.userName, //当前登录用户名
        myProfilePhoto: getApp().globalData.profilePhoto,//当前登录用户头像
        recipeId: 0, //菜谱ID
        resourceRootPath: config.resourceRootPath,
        recipeName: "", //菜谱标题信息
        showImage: "", // 菜谱封面图
        mediaUrl: "",//媒体资源路径（图片、视频）
        detail: "", //菜谱详情名字信息
        userId: 0, //发布者用户ID
        userName: "",//发布者用户名
        profilePhoto: "", //发布者用户头像
        star: 0, //点赞收藏量
        state: 0, //上架状态
        difficulty: 0,//难度等级
        secSortId: 0, //二级分类标签ID
        typeName: "",//二级分类标签名 
        steps: [],//菜谱步骤信息 
        materials: [],//菜谱用料信息
        rateId: -1,//评分编号
        rateValue: 1,//评分值
        doIFollowWriter: false, // 我是否关注发布者用户
        doILikeThis: false, //我是否点赞收藏该菜谱
        likedCounts: 0, //菜谱被点赞总数
        commentCounts: 0, //菜谱被评论总数
        commentLoadPage: {
            current: 1,
            size: 4,
            total: 0
        },
        replyLoadPage: {
            "current": 1,
            "size": 1,
            "total": 0
        },

        isShowCmtInput: false, //是否展示“评论输入框”
        inputCmtContent: "",//用于获取输入的评论内容
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        console.log("has been in recipe-detail page")
        if (options) {
            this.setData({
                recipeId: parseInt(options.recipeId), // 页面传过来的菜谱id是string类型
            });

        }
        // 加载菜谱详情信息
        this.getRecipeInfo();
        //this.getTestRecipeData();
        // 加载评论数据
        this.getRecipeComments();
        // this.getTestCommentData();
        // 加载评论项
        // this.getLittleRecipeCommments();
        // 查看用户是否关注
        // this.getUserCollection();
    },
    //点击评分控件元素个数变化时
    onChangeRate(event) {
        this.setData({
            rateValue: event.detail,
        });
        if (this.data.rateId == -1) {
            this.addRate()
        } else {
            this.updateRate()
        }
    },
    //新增评分
    addRate() {
        var self = this
        let rate = {
            id: 0,
            score: self.data.rateValue,
            userId: self.data.myId,
            recipeId: self.data.recipeId
        }
        var params = {
            url: "/rate/addRate",
            method: "POST",
            data: rate,
            callBack: (resData) => {
                wx.showToast({
                    title: '评价成功',
                    icon: 'success'
                })
                console.log("AddRate:", resData)
                self.setData({
                    rateId: resData.rateId, //评价编号
                    rateValue: resData.rateValue //评价值
                })
            }
        };
        http.request(params);
    },
    //更新评分
    updateRate() {
        var self = this
        console.log("updateRateById---rateId:", self.data.rateId)
        var params = {
            url: "/rate/updateRateById?rateId=" + self.data.rateId + "&score=" + self.data.rateValue,
            method: "POST",
            callBack: (resData) => {
                console.log("updated the Rate; res.data:", resData);
                wx.showToast({
                    title: '更新成功',
                    icon: 'success'
                })
            }
        };
        http.request(params);
    },
    initLoadData() {
        this.data.commentLoadPage.current = 1;// 页码重新设置回1
        this.data.commentList = []; // 将显示列表数据清空
        // this.getRecipeInfo();
        this.getRecipeComments();
    },
    //测试数据--菜谱信息
    getTestRecipeData() {
        var recipeDataStr = testData.recipeDataStr;
        var res = JSON.parse(recipeDataStr);
        var resData = res.data;
        console.log(resData)
        this.setData({
            recipeName: resData.recipeName, //菜谱标题信息
            showImage: resData.showImage, // 菜谱封面图
            mediaUrl: resData.mediaUrl, //媒体资源路径
            detail: resData.detail, //菜谱详情名字信息
            userId: resData.userId, //发布者用户ID
            userName: resData.userName,//发布者用户名
            profilePhoto: resData.profilePhoto, //发布者用户头像
            star: resData.star, //点赞收藏量
            state: resData.state, //上架状态
            difficulty: resData.difficulty,//难度等级
            secSortId: resData.secSortId, //二级分类标签ID
            typeName: resData.typeName,//二级分类标签名 
            steps: resData.steps,//菜谱步骤信息 
            materials: resData.materials,//菜谱用料信息
            rateId: resData.rateId,
            rateValue: resData.rateValue,
            doIFollowWriter: resData.doIFollowWriter, // 我是否关注发布者用户
            doILikeThis: resData.doILikeThis, //我是否点赞收藏该菜谱
            likedCounts: resData.likedCounts, //菜谱被点赞总数
            commentCounts: resData.commentCounts, //菜谱被评论总数
        });
    },
    //分页加载菜谱评论
    getRecipeComments() {
        //  wx.showLoading();
        var self = this
        var params = {
            url: "/comment/getCommentListByPage?type=" + commentType.comment_recipe + "&typeId=" + self.data.recipeId + "&userId=" + self.data.myId,
            method: "POST",
            data: self.data.commentLoadPage,
            callBack: (resData) => {
                console.log("getRecipeComments res.data:", resData);
                self.setData({
                    commentLoadPage: {
                        current: resData.current,
                        size: resData.size,
                        total: resData.total
                    }
                })
                var dataRecords = resData.records;

                if (dataRecords) {
                    //搜出有结果时，拼接到列表中
                    if (dataRecords.length >= 1) {
                        dataRecords.forEach((item) => {
                            Object.assign(item, { isShowReply: false, replyList: [] });
                        });
                        self.setData({
                            commentList: self.data.commentList.concat(dataRecords),
                        });
                    }
                }
                console.log("commentList::::", self.data.commentList)
                wx.hideLoading();
            }
        };
        http.request(params);

    },
    //测试数据--评论信息
    // getTestCommentData() {
    //     var commentDataStr = testData.commentDataStr;
    //     var res = JSON.parse(commentDataStr);
    //     var resData = res.data.records;
    //     resData.forEach((item) => {
    //         Object.assign(item, { isShowReply: false, replyList: [] });
    //     });
    //     this.setData({
    //         commentList: resData
    //     });
    //     console.log("commentList::::", this.data.commentList)
    // },

    // 获取菜谱信息
    getRecipeInfo() {
        wx.showLoading();
        var self = this
        console.log("recipeType:", typeof (this.data.recipeId))
        var params = {
            url: "/recipe/getRecipeWholeById",
            method: "GET",
            data: {
                recipeId: this.data.recipeId,
                myId: this.data.myId,
            },
            callBack: (resData) => {

                console.log("res.data:", resData);
                self.setData({
                    recipeName: resData.recipeName, //菜谱标题信息
                    showImage: resData.showImage, // 菜谱封面图
                    mediaUrl: resData.mediaUrl, //媒体资源路径
                    detail: resData.detail, //菜谱详情名字信息
                    userId: resData.userId, //发布者用户ID
                    userName: resData.userName,//发布者用户名
                    profilePhoto: resData.profilePhoto, //发布者用户头像
                    star: resData.star, //点赞收藏量
                    state: resData.state, //上架状态
                    difficulty: resData.difficulty,//难度等级
                    secSortId: resData.secSortId, //二级分类标签ID
                    typeName: resData.typeName,//二级分类标签名 
                    steps: resData.steps,//菜谱步骤信息 
                    materials: resData.materials,//菜谱用料信息
                    rateId: resData.rateId, //评分ID
                    rateValue: resData.rateValue,//评分值

                    doIFollowWriter: resData.doIFollowWriter, // 我是否关注发布者用户
                    doILikeThis: resData.doILikeThis, //我是否点赞收藏该菜谱
                    likedCounts: resData.likedCounts, //菜谱被点赞总数
                    commentCounts: resData.commentCounts, //菜谱被评论总数
                });
                wx.hideLoading();
            }
        };
        http.request(params);
    },

    /** 添加或者取消收藏菜谱 */
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
                type: likeType.like_recipe,
                typeId: this.data.recipeId,
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

    //预览图片
    onPreviewImage: function (event) {
        var itemImg = this.data.resourceRootPath + this.data.showImage;
        wx.previewImage({
            current: itemImg,
            urls: [itemImg],
            showmenu: true, //是否显示长按菜单。
            fail: function () {
                wx.showToast({ title: '预览图片失败', icon: 'none' });
            },
        });
    },
    //预览视频
    onPreviewVideo: function (event) {

        var resourceRootPath = this.data.resourceRootPath
        var sources = [];
        sources.push({
            type: 'video',
            url: resourceRootPath + this.data.mediaUrl,
            poster: resourceRootPath + this.data.showImage
        });
        wx.previewMedia({
            sources: sources,
            current: 0,
            fail: function () {
                wx.showToast({ title: '预览视频失败', icon: 'none' });
            },
        });
    },

    /* 加载评论模块 */
    locateComments() {
        wx.pageScrollTo({
            duration:300,
            selector:'#rateBox'
          })
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
    //展示对菜谱[添加评论]对话框
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
    //添加对该菜谱的顶层评论
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
            topicType: commentType.comment_recipe,//当前为菜谱
            topicId: this.data.recipeId,//当前为菜谱ID
        }
        let msgToUid = this.data.userId //当前菜谱的发布者ID
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
                // self.data.commentLoadPage.total = self.data.commentLoadPage.total + 1
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
        console.log('父Recipe-toAddReplyInCmt:', replyInfo)
        var params = {
            url: '/reply/addReply?type=' + commentType.comment_recipe + '&typeId=' + self.data.recipeId,
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
                        item.replyList.unshift(resData)
                        item.repliedCounts = item.repliedCounts + 1
                        self.data.replyLoadPage.total = self.data.replyLoadPage.total + 1
                        if (item.replyList.length < item.repliedCounts) {
                            item.isShowReply = true
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
        console.log('父Recipe-toAddNewReply:', replyInfo)
        var params = {
            url: '/reply/addReply?type=' + commentType.comment_recipe + '&typeId=' + self.data.recipeId,
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
    //删除评论信息
    toDeleteComment(e) {
        var self = this
        let commentId = e.detail.commentId
        let indexToDel = e.detail.index
        let objToDelCmt = {
            id: commentId,
            topicType: commentType.comment_recipe,//当前为菜谱
            topicId: self.data.recipeId,//当前为菜谱ID
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
    //根据评论id加载回复列表
    getReplyList(index, commentId) {
        console.log("------commentId--totalCount-:-")
        console.log(commentId, typeof (commentId))
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
    /**
     * 跳转到首页
     */
    toHomePage: function () {
        wx.switchTab({
            url: '/pages/index/index',
        })
    },
    /**
     * 展示用户信息
     */
    showUserPage() {

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
            myUserName: getApp().globalData.userName, //当前登录用户名
            myProfilePhoto: getApp().globalData.profilePhoto,//当前登录用户头像
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
        // 下拉刷新
        var self = this;
        self.onLoad()
        //   wx.stopPullDownRefresh();
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        if (this.data.commentList.length != 0 && this.data.commentList.length < this.data.commentLoadPage.total) {
            console.log("add current")
            this.data.commentLoadPage.current = this.data.commentLoadPage.current + 1;
            this.getRecipeComments()
        }
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})