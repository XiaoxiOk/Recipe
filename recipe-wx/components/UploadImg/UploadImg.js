// components/UploadImg/UploadImg.js
var config = require('../../utils/config')
import Notify from '../../miniprogram_npm/@vant/weapp/notify/notify';
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        showType: {
            type: String,
            value: "add"
        },
        acceptType:{
            type: String,
            value: "media"
        },
        uploadIcon: {
            type: String,
            value:"plus"
        },
        fileShowList: Array,
        dirType: String,
        maxCount: {
            type: Number,
            value: 1
        },
        previewSize: {
            type: String,
            value: "250px"
        },
        showStyle: String
    },

    observers: {
        'dirType': function (dirType) {
            this.setData({
                uploadURL: config.domain + "/upload?dirType=" + dirType
            })
        },
    },
    /**
     * 组件的初始数据
     */
    data: {
        fileList: [],
        newArr: [],
        count: 1,
        maxSize: config.fileUploadMaxSize,
        resourceRootPath: config.resourceRootPath,
        show: false, // 是否预览
        showUrl: '',  //弹窗展示的图片/视频路径
        poster: '',//视频封面图
        urlType: '',  //弹窗展示的类型
    },

    /**
     * 组件的方法列表
     */
    methods: {
        //面对已有的--初始化（父组件调用）
        showFileInit() {
            var self = this
            console.log('fileShowList:', self.properties.fileShowList)
            this.setData({
                fileList: self.properties.fileShowList
            })
        },
        /**
     * 图片上传处理
     */

        afterRead(event) {
            var ths = this
            const { file } = event.detail;
            console.log("file:", file)


            // 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
            var fileType = file.type
            var targetUrl = this.data.uploadURL + '&fileType=' + fileType


            wx.uploadFile({
                url: targetUrl, // 仅为示例，非真实的接口地址
                filePath: file.url,
                name: 'file',
                formData: { user: 'test' },
                success(res) {
                    console.log('res', res)
                    // 上传完成需要更新 fileList
                    if (res.statusCode == 200) {
                        const { fileList = [] } = ths.data;
                        var jsonData = JSON.parse(res.data); //记得转换格式，不然url获取值为undefined
                        console.log('jsonData', jsonData)
                        fileList.push({ ...file, 
                            type: jsonData.data.fileType,
                            url: ths.data.resourceRootPath + jsonData.data.fileUrl, 
                            thumb: ths.data.resourceRootPath + jsonData.data.coverUrl,
                            fileUrl: jsonData.data.fileUrl, 
                            coverUrl: jsonData.data.coverUrl
                        });

                        ths.setData({ fileList });
                        ths.sendData()
                    }

                }
            });

        },
        sendData() {
            console.log("sendData:",this.data.fileList)
            this.triggerEvent('myevent', { fileList: this.data.fileList })
        },
        // 点击删除图片
        deleteImg(event) {
            let id = event.detail.index //能获取到对应的下标
            let newArr = this.data.newArr //上传的数组(不需要[{url:XXX}]格式) 
            let fileList = this.data.fileList //页面展示的数组(需要[{url:XXX}]格式)
            newArr.splice(id, 1) //根据下标来删除对应的图片
            fileList.splice(id, 1)
            this.setData({
                fileList: fileList,
                newArr: newArr
            })
            this.sendData()
        },
        //文件超出最大限制时
        tipOverSize() {
            Notify({
                type: 'warning',
                duration: 2000,
                context: this,
                selector: '#showImg-selector',
                message: '文件大小不能超过100MB！'
            });
        },
        //给父组件传fileList
        bindCode: function (e) {
            var that = this;
            var val = e.detail.value == undefined ? that.data.codes : e.detail.value;
            //这里针对输入框，判断e.detail.value（是否手动输入了值，没有输入直接赋值处理好的that.data.codes，如果输入了值，就直接使用e.detail.value）传递给父组件
            var myEventDetail = {
                val: val
            } // detail对象，提供给事件监听函数
            this.triggerEvent('myEvent', myEventDetail)
        },
    
    },



})
