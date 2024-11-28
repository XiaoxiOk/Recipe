<template>
  <div>
    <el-upload
      class="upload-demo"
      ref="upload"
      :action="uploadURL"
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      :file-list="fileList"
      list-type="picture"
      :on-success="handleSuccess"
      :on-error="handleError"
      :limit="1"
      :on-exceed="handleExceed"
    >
      <el-button size="small" type="primary">更换图片</el-button>
      <div slot="tip" class="el-upload__tip">只能上传图像文件，且不超过2MB</div>
    </el-upload>
  </div>
</template>
<script>
export default {
  name: "ButtonUploadImg",
  props: { dirType: "" },
  data() {
    return {
      uploadURL:
        "http://localhost:8099/upload?dirType=" +
        this.dirType +
        "&fileType=img",
      fileList: [],
      imageUrl: "",
    };
  },
  methods: {
    beforeAvatarUpload(file) {
      //  const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isLt2M;
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
      this.imageUrl = "";
    },
    handleSuccess(response) {
      this.imageUrl = response.data;
    },
    handlePreview(file) {
      console.log(file);
      window.open(file.url, "_blank");
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择1个文件`);
    },
    handleError() {
      this.$message.error("上传失败");
    },
    resetButtonUploadImg() {
      this.$refs.upload.clearFiles();
      this.fileList = undefined;
      this.imageUrl = "";
    },
  },
};
</script>