<template>
  <div style="margin-top: 10px; margin-left: 15%; margin-right: 10%">
    <el-table :data="tableData" style="width: 100%">
      <el-table-column label="广告位" width="120">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.position }}</span>
        </template>
      </el-table-column>
      <el-table-column label="广告图片" width="150" align="center">
        <template slot-scope="scope">
          <img
            v-if="!isEmpty(scope.row.imgUrl)"
            style="width: 120px; height: 80px; border: none"
            :src="imageRootPath + scope.row.imgUrl"
          />
          <img
            v-else
            style="width: 120px; height: 80px"
            src="@/assets/image/imgError.jpg"
          />
        </template>
      </el-table-column>
      <el-table-column label="关键信息" width="180" align="center">
        <template slot-scope="scope">
          <span style="margin-left: 10px" v-if="!isEmpty(scope.row.keyword)">{{
            scope.row.keyword
          }}</span>
          <span style="margin-left: 10px" v-else>（待添加）</span>
        </template>
      </el-table-column>
      <el-table-column label="推广短链" width="200" align="center">
        <template slot-scope="scope">
          <span
            style="margin-left: 10px"
            v-if="!isEmpty(scope.row.targetUrl)"
            >{{ scope.row.targetUrl }}</span
          >
          <span style="margin-left: 10px" v-else>（待添加）</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"
            >编辑</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 10px">
      <a href="https://union.jd.com/index" target="_blank">
        <img
          src="@/assets/image/jd-union.jpg"
          title="点此去获取短链xiaoxi_Ok"
          width="120px"
          border="0"
      /></a>
    </div>
    <el-dialog
      title="编 辑"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <el-form ref="editForm" :model="formData" :rules="formRules">
        <el-form-item label="广告图片" prop="imgurl">
          <ButtonUploadImg ref="ButtonUploadImg" dirType="ad"></ButtonUploadImg>
        </el-form-item>
        <el-form-item label="关键信息" prop="keyword">
          <el-input style="width: 60%" v-model="formData.keyword"></el-input>
        </el-form-item>
        <el-form-item label="推广短链" prop="keyword">
          <el-input style="width: 60%" v-model="formData.targetUrl"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleUpdateSubmit">确 定</el-button>
        <el-button @click="handleReset">重 置</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { getAllAd, updateAd } from "@/api/ad";
import ButtonUploadImg from "@/components/ButtonUploadImg/index";
export default {
  components: { ButtonUploadImg },
  data() {
    return {
      dialogVisible: false, //编辑对话框显示标识
      formData: {}, //记录编辑广告位时的数据
      beforeData: {}, //修改前的数据
      tableData: [],
      formRules: {
        keyword: [{ max: 20, message: "长度不超过20个字符", trigger: "blur" }],
        targetUrl: [
          { required: true, message: "请输入广告推广链接", trigger: "blur" },
        ],
      },
    };
  },
  computed: {
    ...mapGetters([
      "imageRootPath", //访问静态资源图片库根地址
    ]),
  },
  mounted() {
    this.writeAdList();
  },
  methods: {
    writeAdList() {
      getAllAd()
        .then((res) => {
          console.log(res);
          this.tableData = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //判断字符是否为空的方法
    isEmpty(obj) {
      var regu = "^[ ]+$";
      var re = new RegExp(regu);
      if (
        typeof obj == "undefined" ||
        obj == null ||
        obj == "" ||
        re.test(obj)
      ) {
        return true;
      } else {
        return false;
      }
    },
    handleEdit(index, row) {
      console.log(index, row);
      this.dialogVisible = true;
      this.formData = JSON.parse(JSON.stringify(row)); //深拷贝
      this.beforeData = JSON.parse(JSON.stringify(row));
    },
    handleUpdateSubmit() {
      var imageUrl = this.$refs.ButtonUploadImg.imageUrl;
      if (imageUrl != "") {
        this.formData.imgUrl = imageUrl;
      }
      console.log(this.formData);
      if (JSON.stringify(this.formData) == JSON.stringify(this.beforeData)) {
        this.$message.warning("您的信息未修改嘞！");
      } else {
        this.$refs.editForm.validate((validate) => {
          if (!validate) {
            return this.$message.error("请正确录入信息！");
          } else {
            updateAd(this.formData).then((res) => {
              console.log(res);
              this.dialogVisible = false;
              this.$message.success("修改成功！");
              this.$refs.ButtonUploadImg.resetButtonUploadImg();
              this.writeAdList();
            });
          }
        });
      }
    },
    handleClose() {
      this.formData = {};
      this.$refs.ButtonUploadImg.resetButtonUploadImg();
      this.dialogVisible = false;
    },
    handleReset() {
      if (this.$refs.editForm !== undefined) {
        this.$refs.ButtonUploadImg.resetButtonUploadImg();
        this.$refs.editForm.resetFields();
        this.formDat = this.beforeData;
      }
    },
  },
};
</script>