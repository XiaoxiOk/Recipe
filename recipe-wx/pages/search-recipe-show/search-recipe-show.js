// pages/search-recipe-show/search-recipe-show.js
var config = require('../../utils/config.js');
var http = require('../../utils/http.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    resourceRootPath: config.resourceRootPath,
    isShowTag:"false",
    secId:-1, //检索时是否匹配二级类别，-1时默认不匹配
    tagName:"",
    show: {
        primary: true,
        success: true,
      },
    sts: 0,
    showType:2,
    recipeList:[],
    recipeName:"",
    page: {
        current: 1,
        size: 2,
        total: 0
    },
  },
//   清除标签
  onClose(event) {
    this.setData({
      [`show.${event.target.id}`]: false,
      secId: -1
    });
    if(this.data.recipeName===""){
        this.setData({
            recipeList:[]
        })
        console.log('当前内容为空！')
    }else{
        this.toLoadData()
    }
  },
  changeShowType:function(){
    var showType = this.data.showType;
    if (showType==1){
      showType=2;
    }else{
      showType = 1;
    }
    this.setData({
      showType: showType
    });
  },
   //重置数据信息
   reFreshData() {
    var self = this;
    self.data.page.current = 1;// 页码重新设置回1
    self.data.page.total = 0;
    this.setData({
        recipeList: [], // 将显示列表数据清空
    })
    self.toLoadData()
},
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      console.log("options--->",options)
    this.setData({
      isShowTag: options.isShowTag
    });
    console.log("--->isShowTag:",this.data.isShowTag)
    if(this.data.isShowTag === "true"){
        this.setData({
            tagName: options.tagName,
            secId: Number(options.secId)
        })
        console.log("load tagName:", this.data.tagName)
    }else if(this.data.isShowTag === "false"){
        this.setData({
            recipeName: options.recipeName,
            secId: -1
        })
    }
    this.toLoadData();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  //输入菜谱名获取数据
  getSearchContent: function (e) {
    this.setData({
      recipeName: e.detail.value
    });
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

//请求菜谱接口
  toLoadData:function(){
    var ths = this;
    // 请求后台接口获取菜谱作品列表
    if (ths.data.recipeList.length != 0 && ths.data.recipeList.length === ths.data.page.total) {
        console.log("拒绝请求")
        return
    }
    //热门搜索
    console.log("recipeName",this.data.recipeName, ths.data.secId)
    var params = {
      url: "/recipe/searchRecipeByNameOrSecId?secId="+ths.data.secId+"&recipeName="+ths.data.recipeName,
      method: "POST",
      data: { page:ths.data.page,
      },
      callBack: function (resData) {
          console.log('search-resData:',resData)
          ths.setData({
            page: {
                current: resData.current,
                size: resData.size,
                total: resData.total
            }
        })
        var dataRecords = resData.records;
        if (dataRecords) {
            //搜出有结果时，拼接到列表中
            if (dataRecords.length >= 1) {
                ths.setData({
                    recipeList: ths.data.recipeList.concat(dataRecords),
                });
            }
            console.log("index-menu-recipeList:", ths.data.recipeList)
        }
        
      },
    };
    http.request(params);
  },

//当前搜索页二次搜索菜谱
  toSearchConfirm:function(){
      console.log('confirm!!')
      if(this.data.recipeName==="" && this.data.secId==-1){
        this.setData({
            recipeList:[]
        })
        wx.showToast({
          title: '请输入菜谱名',
          duration:1000,//显示时长
          icon:'error'
        })
      }else{
        this.reFreshData()
      }
    
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
    var self = this;
    if (self.data.recipeList.length != 0 && self.data.recipeList.length < self.data.page.total) {
        console.log("add current")
        self.data.page.current = self.data.page.current + 1;
        self.toLoadData()
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

    /**
   * 状态点击事件
   */
  onStsTap: function(e) {
    var sts = e.currentTarget.dataset.sts;
    this.setData({
      sts: sts
    });
    this.toLoadData();
  },

  toProdPage: function (e) {
    var prodid = e.currentTarget.dataset.prodid;
    wx.navigateTo({
      url: '/pages/prod/prod?prodid=' + prodid,
    })
  },
})