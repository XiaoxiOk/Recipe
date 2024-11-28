<template>
  <div>
    <el-upload
      class="avatar-uploader"
      list-type="picture-card"
      :action="uploadURL"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
      ref="upload"
    >
      <img
        v-if="imageShowUrl != ''"
        :src="imageShowUrl"
        class="avatar"
        alt=""
      />
      <i slot="default" class="el-icon-plus"></i>
    </el-upload>
    <div slot="tip" class="el-upload__tip">只能上传图像文件，且不超过2MB</div>
  </div>
</template>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<script>
export default {
  name: "UploadImg",
  props: { dirType: "" },
  data() {
    return {
      uploadURL:
        "http://localhost:8099/upload?dirType=" +
        this.dirType +
        "&fileType=img",
      imageShowUrl: "",
      imageUrl: "",
    };
  },
  methods: {
    handleAvatarSuccess(res, file) {
      this.imageShowUrl = URL.createObjectURL(file.raw); //显示路径
      this.imageUrl = res.data.fileUrl; //文件物理路径
      console.log("imageUrl:" + res.data.fileUrl);
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isLt2M;
    },
    resetUploadImg() {
      this.$refs.upload.clearFiles();
      this.imageShowUrl = "";
      this.imageUrl = "";
    },
  },
};
</script>