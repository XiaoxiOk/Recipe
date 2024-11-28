// pages/category/category.js

var http = require("../../utils/http.js");
var config = require("../../utils/config.js");

Page({

  /**
   * 页面的初始数据
   */
  data: {
    selIndex: 0,
    firstSortList:[],
    secSortList: [],
    resourceRootPath: config.resourceRootPath,
    prodid:'',

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var ths = this;
      ths.data.firstSortList=[]
    //加载分类列表
    var params = {
      url: "/first-sort/getFirstSortNameList",
      method: "GET",
      callBack: function (resData) {
        // console.log(res);
        console.log("res.data:",resData);
        ths.setData({
          firstSortList: resData,
        });
        console.log("firstSortList:", ths.firstSortList)
        ths.getSecSortList(resData[0].id)
        console.log("SecSortList::", ths.secSortList)
      }
    };
    http.request(params);
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
   


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

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  /* 分类点击事件 */
  onMenuTab: function (e) {
    var index = e.currentTarget.dataset.index;
    this.getSecSortList(this.data.firstSortList[index].id); //获取二级类别数据
    this.setData({  selIndex: index }); //更新已选择的一级类别标签
  },
 /* 根据一级类别ID请求二级类别 */
  getSecSortList(firstSortId) {
    var ths = this;
    ths.setData({
        secSortList: [],
      });
    var params = {
      url: "/second-sort/getSecSortByFirstId",
      method: "GET",
      data: {
        firstSortId: firstSortId
      },
      callBack: (resData) => {
        console.log("res.data:",resData);
        ths.setData({
          secSortList: resData,
        });
      }
    };
    http.request(params);
  },
  
 // 跳转搜索页
 toSearchPage: function () {
    wx.navigateTo({
      url: '/pages/search-page/search-page',
    })
  },
//跳转商品详情页  TODO 搜索页！
  toSearchPage: function (e) {
      console.log('dataset:',e.currentTarget.dataset)
    var secId = e.currentTarget.dataset.secid;
    var secName =  e.currentTarget.dataset.secname
    wx.navigateTo({
      url: '/pages/search-recipe-show/search-recipe-show?isShowTag=true&tagName=' + secName + '&secId=' + secId,
    })
  },
})