<template>
  <div>
    <el-form
      :model="formData"
      :rules="formRules"
      ref="ruleForm"
      label-width="100px"
    >
      <el-form-item
        v-if="handleType == 'addSecondSort'"
        label="类别封面"
        prop="image"
      >
        <UploadImg ref="UploadImg" dirType="secSort"></UploadImg>
      </el-form-item>

      <el-form-item
        v-if="handleType == 'editSecondSort'"
        label="类别封面"
        prop="image"
      >
        <ButtonUploadImg
          ref="ButtonUploadImg"
          dirType="secSort"
        ></ButtonUploadImg>
      </el-form-item>

      <el-form-item label="一级类别" prop="firstId">
        <el-select v-model="selectedTypeId" placeholder="请选择">
          <el-option
            v-for="item in firstSortOpts"
            :key="item.value"
            :label="item.firstName"
            :value="item.firstId"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="二级类别" prop="secName">
        <el-input style="width: 88%" v-model="formData.secName"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { getFirstSortNameList } from "@/api/sort";
import UploadImg from "@/components/UploadImg/index";
import ButtonUploadImg from "@/components/ButtonUploadImg/index";
export default {
  name: "SecondEditSort",
  components: { UploadImg, ButtonUploadImg },
  props: {
    formParentData: {},

    elemType: { type: String, require: true },
    firstSortId: { type: Number, require: false },
  },
  data() {
    return {
      formData: JSON.parse(JSON.stringify(this.formParentData)), //深拷贝，formParentData还要用于比较是否做出了修改
      handleType: this.elemType,
      //  addImgShow:this.formParentData.image.length == 0,
      selectedTypeId: this.firstSortId,
      firstSortOpts: [], //记录一级类别列表
      formRules: {
        firstSortId: [{ required: true, message: "请选择", trigger: "blur" }],
        secName: [
          { required: true, message: "请输入二级类别名称", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "长度在 2 到 20 个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted() {
    this.WriteFirstSortNameList();
  },
  methods: {
    WriteFirstSortNameList() {
      getFirstSortNameList().then((res) => {
        var dataList = res.data;
        dataList.forEach((element) => {
          this.firstSortOpts.push({
            firstId: Number(element.id),
            firstName: element.typeName,
          });
        });
        console.log("dataList -- res", this.firstSortOpts);
      });
    },

    getImgUrl() {
      var imageUrl;
      if (this.elemType === "addSecondSort") {
        imageUrl = this.$refs.UploadImg.imageUrl;
      } else if (this.elemType === "editSecondSort") {
        imageUrl = this.$refs.ButtonUploadImg.imageUrl;

        if (imageUrl == "") {
          imageUrl = this.formParentData.image;
        }
      }
      console.log("dialog[img]:" + this.imageUrl);
      return imageUrl;
    },
    getSecondSortInfo() {
      var secondSort = {
        id: this.formData.secId, // 二级类别编号
        image: this.getImgUrl(), // 类别封面图片
        typeName: this.formData.secName, // 二级类别名称
        parent: this.selectedTypeId, // 一级类别Id
        createTime: this.formData.createTime, //二级类别创建时间
        updateTime: this.formData.updateTime, //二级类别修改时间
      };

      console.log(secondSort);
      return secondSort;
    },
    //重置表单
    resetEditForm() {
      if (this.$refs["ruleForm"] !== undefined) {
        console.log("reset...");
        this.$refs["ruleForm"].resetFields();
        this.selectedTypeId = this.firstSortId;
        if (this.elemType === "addSecondSort") {
          this.$refs.UploadImg.resetUploadImg();
        } else if (this.elemType === "editSecondSort") {
          this.$refs.ButtonUploadImg.resetButtonUploadImg();
        }
      }
    },
  },
};
</script>