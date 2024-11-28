// pages/edit-recipe/edit-recipe.js
import Notify from '../../miniprogram_npm/@vant/weapp/notify/notify';
var http = require('../../utils/http.js');
var config = require('../../utils/config.js');
var util = require('../../utils/util.js');
Page({

    /**
     * 页面的初始数据
     */
    /**
     * 页面的初始数据
     */
    data: {
        myId: getApp().globalData.currentUserId,
        recipeId: 0,
        resourceRootPath: config.resourceRootPath,
        sortOptions: [], //级联选择器选项列表
        fieldNames: {    //自定义options里字段名称
            text: 'typeName',
            value: 'id',
            children: 'children',
        },
        isShow: false, //级联选择器pop是否显示
        fieldValue: '',
        cascaderValue: '',
        beforeUpdateSort: '',
        showImage: '',
        mediaUrl: '',
        recipeName: '',
        detail: '',
        secSortId: 0,
        typeName: '',
        difficulty: '0', //van-radio对应String类型，需要传参是Number，记得转换类型
        materials: [],//
        steps: [],
        isHasExisted: false,
        isShowDelMaterial: false,
        isShowDelStep: false,
        isFoodNameNull: false, //判断食材名称项是否为空
        isStepNull: false, //判断步骤信息项是否为空
        coverMediaShowList: [],
        stepMediaShowList: [],

        stepIdsToDel: [],//用于记录被删除的步骤ID
        materialIdsToDel: [],//用于记录被删除的食材ID
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

    },
    onShow: function () {
        this.setData({
            myId: getApp().globalData.currentUserId,
        })
    },
    // 加载菜谱修改前的详细信息
    getRecipeInfo() {
        wx.showLoading();
        var self = this;
        console.log("recipeType:", this.data.recipeId)
        var params = {
            url: "/recipe/getRecipeToUpdateById",
            method: "GET",
            data: {
                recipeId: self.data.recipeId,
            },
            callBack: (resData) => {

                console.log("res.data:", resData);
                self.setData({
                    recipeName: resData.recipeName, //菜谱标题信息
                    showImage: resData.showImage, // 菜谱封面图
                    detail: resData.detail, //菜谱详情名字信息
                    difficulty: String(resData.difficulty),//难度等级
                    secSortId: resData.secSortId, //二级分类标签ID
                    typeName: resData.typeName,//二级分类标签名 
                    steps: resData.steps,//菜谱步骤信息 
                    materials: resData.materials,//菜谱用料信息
                });
                wx.hideLoading();
                this.setBeforeFieldValue();
                this.setMediaShowList();
            }
        };
        http.request(params);

    },
    //在级联器处显示之前的类别信息
    setBeforeFieldValue() {
        let self = this
        console.log('secSortId', self.data.secSortId)
        var params = {
            url: "/second-sort/getSortNameBySecSortId",
            method: "GET",
            data: {
                secSortId: self.data.secSortId,
            },
            callBack: (resData) => {
                console.log("res.data:", resData);
                self.setData({
                    beforeUpdateSort: resData.sortConcatName
                });
                wx.hideLoading();
            }
        };
        http.request(params);

    },
    //加载对应图片组件的展示图
    setMediaShowList() {
        let self = this
        let coverInfo = {
            type: 'image',
            url: config.resourceRootPath + self.data.showImage,
            fromType: 'recipeCover'
        }
        self.setData({
            coverMediaShowList: [coverInfo],

        })
        // 初始化获取封面组件
        self.selectComponent('#coverUploadImg').showFileInit()
        self.data.steps.forEach((item, index) => {
            var stepMediaShowList = []
            if (item.imgUrl != undefined && item.imgUrl != "" && item.imgUrl.length > 0) {
                stepMediaShowList.push({
                    type: 'image',
                    url: config.resourceRootPath + item.imgUrl,
                    fromType: 'step-' + index
                })
            }
            self.setData({
                stepMediaShowList: stepMediaShowList
            })

            // 初始化获取步骤图组件
            let childId = '#stepUploadImg-' + index
            self.selectComponent(childId).showFileInit()

        })


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
                    [`sortOptions[${index}].isGetSecItems`]: true,
                    [`sortOptions[${index}].children`]: resData
                });
                console.log('sortOptions````:', ths.data.sortOptions)
            }
        };
        http.request(params);
    },
    // 级联数据全部选项选择完毕后，会触发 finish 事件
    onFinish(e) {
        const { selectedOptions, value } = e.detail;
        console.log(e)
        const fieldValue = selectedOptions
            .map((option) => option.typeName || option.text)
            .join('/');
        this.data.secSortId = value
        this.setData({
            fieldValue,
            cascaderValue: value,
            isShow: false,
        })
        console.log('secId:', this.data.secSortId, ";cascaderValue", this.data.cascaderValue)
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

    //父组件获取子组件的值：封面图
    uploadShowImgEvent(e) {
        let fileList = e.detail.fileList;
        if (fileList.length == 1) {
            this.data.showImage = e.detail.fileList[0].coverUrl
            this.data.mediaUrl = e.detail.fileList[0].fileUrl
        } else if (fileList.length == 0) {
            this.data.showImage = ''
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
        if (foodName != "" && this.data.materials.map(item => item.foodName).indexOf(foodName) > -1) {
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
        let materials = this.data.materials
        if (this.data.isHasExisted && this.check_food_name(materials[index].foodName)) {
            //当有重复时，确认是否是待删除的造成的，如果是则需要置isHasExisted为false，因为要删除啦
            this.setData({
                isHasExisted: false
            })
        }
        //记录被删除的用料信息ID
        if (materials[index].hasOwnProperty("id") && materials[index].id != 0) {
            this.data.materialIdsToDel.push(materials[index].id)
            console.log('--->materialIdsToDel:', this.data.materialIdsToDel)
        }
        materials.splice(index, 1)

        console.log('here!!:', materials)
        this.setData({
            materials: materials
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
        let steps = this.data.steps
        //记录被删除的步骤信息ID
        if (steps[index].hasOwnProperty("id") && steps[index].id != 0) {
            this.data.stepIdsToDel.push(steps[index].id)
            console.log('--->stepIdsToDel:', this.data.stepIdsToDel)
        }
        steps.splice(index, 1)
        console.log('here!!Steps:', steps)
        //更新步骤序号值
        steps.forEach((item, index) => {
            item.seqNumber = index + 1
        })
        this.setData({
            steps: steps
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
    //父组件获取子组件的值：步骤示意图
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
        if (this.data.showImage == "") {
            this.notifyInfo("请选择封面图！")
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
        var recipeToUpdateInfo = {
            id: this.data.recipeId,
            userId: this.data.myId,
            recipeName: this.data.recipeName,
            detail: this.data.detail,
            difficulty: Number(this.data.difficulty),
            secSortId: this.data.secSortId,
            showImage: this.data.showImage,
            mediaUrl: this.data.mediaUrl,
            materials: this.data.materials,
            steps: this.data.steps,
            star: 0,
            state: 0,
            stepIdsToDel: this.data.stepIdsToDel,
            materialIdsToDel: this.data.materialIdsToDel
        }

        var params = {
            url: '/recipe/updateRecipeWhole',
            method: "POST",
            data: recipeToUpdateInfo,
            callBack: function (resData) {
                wx.showToast({
                    title: '修改成功',
                    msg: resData,
                    icon: 'success',
                    duration: 2000
                })
                setTimeout(function () {
                    wx.redirectTo({
                        url: '/pages/show-my-recipe/show-my-recipe'
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