// pages/add-recipe/add-recipe.js
import Notify from '../../miniprogram_npm/@vant/weapp/notify/notify';
var http = require("../../utils/http.js");
Page({

    /**
     * 页面的初始数据
     */
    data: {
        sortOptions: [], //级联选择器选项列表
        fieldNames: {    //自定义options里字段名称
            text: 'typeName',
            value: 'id',
            children: 'children',
        },
        isShow: false, //级联选择器pop是否显示
        isSelectSecSort: false,
        fieldValue: '',
        cascaderValue: '',
        showImage: '',
        mediaUrl:'',
        recipeName: '',
        detail: '',
        secSortId: 0,
        difficulty: '0', //van-radio对应String类型，需要传参是Number，记得转换类型
        materials: [{
            foodName: "",
            consumption: "",
            recipeId: 0,
            id: 0
        }],
        steps: [{
            seqNumber: 1,
            description: "",
            imgUrl: ""
        }],
        isHasExisted: false,
        isShowDelMaterial: false,
        isShowDelStep: false,
        isFoodNameNull: true, //判断食材名称项是否为空
        isStepNull: true //判断步骤信息项是否为空
    },
    // click 类别级联器
    onClick() {
        if (this.data.sortOptions.length == 0) {
            this.writeSortOptions()
        }
        this.setData({
            isShow: true,
        });
    },
    //获取所有类别名信息供选择类别  
    writeSortOptions() {
        var self = this;
        //加载分类列表
        var params = {
            url: "/sort/getWholeSortName",
            method: "GET",
            callBack: function (resData) {
                self.setData({
                    sortOptions: resData
                })
            }
        };
        http.request(params);
    },
    //级联选择器内容变化时
    onChange(e) {
        console.log("event:", e)
    },
    // close 类别级联器
    onClose() {
        this.setData({
            isShow: false,
        });
    },
    // 级联数据全部选项选择完毕后，会触发 finish 事件
    onFinish(e) {
        const { selectedOptions, value } = e.detail;
        console.log(e)
        const fieldValue = selectedOptions
            .map((option) => option.typeName || option.text)
            .join('/');
        this.data.isSelectSecSort = true//表示已经选择了二级类别
        this.data.secSortId = value
        this.setData({
            fieldValue,
            cascaderValue: value,
            isShow: false,
        })
        console.log('secId:', this.data.secSortId, ";cascaderValue", this.data.cascaderValue)
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {

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

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {

    },


    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    },
    //更新菜谱名
    changeRecipeName(e) {
        this.setData({
            recipeName: e.detail.split('\n').join('')
        })
    },
    //更新介绍信息内容
    changeDetail(e) {
        this.setData({
            detail: e.detail.split('\n').join('')
        })
    },
    //获取所有类别名信息供选择类别  
    writeFirstSortInfo() {
        var ths = this;
        //加载分类列表
        var params = {
            url: "/first-sort/getFirstSortNameList",
            method: "GET",
            callBack: function (resData) {
                // console.log(res);
                console.log("res.data:", resData);
                resData.forEach((item, index) => {
                    Object.assign(item, { children: [], index: index, isGetSecItems: false });
                });
                ths.setData({
                    sortOptions: resData,
                });

                console.log("sortOptions:", ths.data.sortOptions)
            }
        };
        http.request(params);
    },
    getSecSortList(firstSortId, index) {
        var ths = this;
        //加载分类列表
        console.log("=====secSortList：", ths.data.sortOptions[index].children);
        var params = {
            url: "/second-sort/getSecSortBasicByFirstId",
            method: "GET",
            data: {
                firstSortId: firstSortId
            },
            callBack: (resData) => {
                resData.forEach((item) => {
                    Object.assign(item, { children: null });
                });
                ths.setData({
                    [`sortOptions[${index}].children`]: resData
                });
                console.log('sortOptions````:', ths.data.sortOptions)
            }
        };
        http.request(params);
    },

    /* 左上角点×返回  */
    goBackToPublish() {

        wx.navigateBack({
            delta: 1,
        })
    },

    //父组件获取子组件的值：封面图
    uploadShowImgEvent(e) {
        let fileList = e.detail.fileList;
        if (fileList.length == 1) {
            this.data.showImage = e.detail.fileList[0].coverUrl
            this.data.mediaUrl = e.detail.fileList[0].fileUrl
        } else if (fileList.length == 0) {
            this.data.showImage = ''
            this.data.mediaUrl = ''
        }
    },
    //更新难度选择
    updateDifficulty(e) {
        this.setData({
            difficulty: e.detail,
        });
    },
    //判断食材名称是否重复
    check_food_name(foodName) {
        if (foodName != "" && this.data.materials.map(item => item.foodName).indexOf(foodName) > 0) {
            return true
        } else {
            return false
        }
    },
    //添加material一个元素
    addMaterial() {
        if (this.data.isHasExisted == true) {
            this.notifyInfo("暂不可添加，建议您直接修改")
        } else if (this.data.isShowDelMaterial) {
            this.notifyInfo("请先保存编辑后的状态")
        } else {
            var materialObj = {
                foodName: "",
                consumption: "",
                recipeId: 0,
                id: 0
            }
            this.data.materials.push(materialObj)
            this.setData({
                materials: this.data.materials
            })//必须使用this.setData才能动态渲染、双向绑定
            console.log('materials:', this.data.materials)
        }

    },
    //删除material一个元素
    delMaterialItem(e) {
        let index = e.currentTarget.dataset.indextodel
        if (this.data.isHasExisted && this.check_food_name(this.data.materials[index].foodName)) {
            //当有重复时，确认是否是待删除的造成的，如果是则需要置isHasExisted为false，因为要删除啦
            this.setData({
                isHasExisted: false
            })
        }
        this.data.materials.splice(index, 1)

        console.log('here!!:', this.data.materials)
        this.setData({
            materials: this.data.materials
        })//必须使用this.setData才能动态渲染、双向绑定

    },
    //更改展示是否删除一个用料信息图标状态
    changeShowDelMaterial() {
        this.setData({
            isShowDelMaterial: !this.data.isShowDelMaterial
        })
    },
    //绑定用料-食材名称：新录入的值
    bind_m_foodName(e) {
        //去除换行
        var foodName = e.detail.value.split('\n').join('');
        if (this.check_food_name(foodName)) {
            this.setData({
                isHasExisted: true
            })
        } else {
            this.setData({
                isHasExisted: false
            })
        }
        let index = e.currentTarget.dataset.index
        this.setData({
            [`materials[${index}].foodName`]: foodName
        })
        if (foodName == "") {
            this.data.isFoodNameNull = true
        } else {
            this.data.isFoodNameNull = false
        }

    },
    //绑定用料-食材用量：新录入的值
    bind_m_consumption(e) {
        let index = e.currentTarget.dataset.index
        this.setData({
            [`materials[${index}].consumption`]: e.detail.value.split('\n').join('')
        })

    },

    //添加step一个元素
    addStep() {
        if (this.data.isShowDelStep) {
            this.notifyInfo("请先保存编辑后的状态")
        } else {
            var stepObj = {
                seqNumber: this.data.steps.length + 1,
                description: "",
                imgUrl: ""
            }
            this.data.steps.push(stepObj)
            this.setData({
                steps: this.data.steps
            })//必须使用this.setData才能动态渲染、双向绑定
            console.log('steps:', this.data.steps)
        }

    },
    //删除step一个元素
    delStepItem(e) {
        let index = e.currentTarget.dataset.indextodel
        this.data.steps.splice(index, 1)
        console.log('here!!Steps:', this.data.steps)
        this.setData({
            steps: this.data.steps
        })//必须使用this.setData才能动态渲染、双向绑定

    },
    //更改展示是否删除一个步骤信息图标状态
    changeShowDelStep() {
        this.setData({
            isShowDelStep: !this.data.isShowDelStep
        })
    },
    //绑定步骤-说明信息：新录入的值
    bind_s_description(e) {
        let index = e.currentTarget.dataset.index
        let description = e.detail.value.split('\n').join('')
        this.setData({
            [`steps[${index}].description`]: description
        })
        if (description == "") {
            this.data.isStepNull = true
        } else {
            this.data.isStepNull = false
        }
    },
    //父组件获取子组件的值：步骤图
    uploadStepImgEvent(e) {
        let index = e.currentTarget.dataset.index
        let fileList = e.detail.fileList;
        var stepImgUrl = ''
        if (fileList.length == 1) {
            stepImgUrl = e.detail.fileList[0].fileUrl
        }
        this.setData({
            [`steps[${index}].imgUrl`]: stepImgUrl
        })
    },
    //对所填写的信息进行校验
    checkRecipeInfo() {
        if (this.data.recipeName == "") {
            this.notifyInfo("菜谱标题不可为空！")
            return false
        }
        if (this.data.showImage == "" || this.data.mediaUrl == "" ) {
            this.notifyInfo("请选择封面！")
            return false
        }
        if (!this.data.isSelectSecSort) {
            this.notifyInfo("还未选择归属类别！")
            return false
        }
        if (this.data.isFoodNameNull) {
            this.notifyInfo("食材名称不能为空哦！")
            return false
        }
        if (this.data.isStepNull) {
            this.notifyInfo("步骤说明不能为空哦！")
            return false
        }
        return true
    },
    //发布菜谱
    publishRecipe() {
        if (!this.checkRecipeInfo()) {
            console.log("审核不通过！")
            return
        }
        console.log("审核通过！")
        var recipeInfo = {
            detail: this.data.detail,
            difficulty: Number(this.data.difficulty),
            recipeName: this.data.recipeName,
            secSortId: this.data.secSortId,
            showImage: this.data.showImage,
            mediaUrl:  this.data.mediaUrl,
            materials: this.data.materials,
            steps: this.data.steps,
            star: 0,
            id: 0,
            state: 0,
            userId: getApp().globalData.currentUserId
        }
        console.log('recipeInfo:', recipeInfo)
        var params = {
            url: '/recipe/addRecipeWhole',
            method: "POST",
            data: recipeInfo,
            callBack: function (resData) {
                wx.showToast({
                    title: '成功',
                    msg: resData,
                    icon: 'success',
                    duration: 2000
                })
                setTimeout(function(){
                    wx.redirectTo({
                        url: '/pages/show-my-recipe/show-my-recipe',
                      })
                  }, 500)
            }

        };
        http.request(params);
    },
    //校验不通过提示信息
    notifyInfo(msg) {
        Notify({
            type: 'warning',
            duration: 2000,
            context: this,
            selector: '#msg-selector',
            message: msg
        });
    }


})