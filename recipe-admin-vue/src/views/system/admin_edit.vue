<template>
<div>
  <section class="box_main">
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="原管理员名" prop="oldName" >
          <el-input ref="oldName" v-model="ruleForm.oldName"></el-input>
        </el-form-item>
        <el-form-item label="原密码" prop="oldPwd">
          <el-input type="password"  v-model="ruleForm.oldPwd"></el-input>
        </el-form-item>
        <el-form-item label="新管理员名" prop="newName" >
          <el-input ref="newName" v-model="ruleForm.newName"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPwd">
          <el-input type="password"  v-model="ruleForm.newPwd"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="checkNewPwd">
          <el-input type="password"  v-model="ruleForm.checkNewPwd"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="button-footer">
        <el-button type="danger" @click="submitEditForm()" align="left">确 定</el-button>
        <el-button plain @click="resetEditForm()" align="right">重 置</el-button>
      </span>
  </section>
</div>
</template>
<style>
.box_main {
  width: 600px;
  margin: 0 auto;
  padding: 50px 0;
}
.button-footer {
  width: 600px;
  margin: 0 auto;
  padding: 50px 0;
  margin-top: 30px;
  margin-left: 40%;
}
</style>
<script>
import { updateAdminInfo } from "@/api/user";
  export default {
    data () {
      const judgePwd = (rule, value, callback) => {
        if (value === this.ruleForm.newPwd) {
          callback();     
        }else{
          callback(new Error('两次密码不一致'));
        }
      };
      return {
        ruleForm: {},
        rules: {
          oldName: [{required: true ,message:"管理员帐户名不能为空！", trigger: "blur"},
            { pattern: /^[a-zA-Z0-9_-]{3,20}$/, message:'管理员帐户名格式错误!', trigger:"blur"},],
          oldPwd: [{ required: true, message: "请输入密码", trigger: "blur" },
            { pattern:/^[_a-zA-Z0-9]{4,20}$/, message:'密码仅由英文字母，数字以及下划线组成（4~20位）', trigger:"blur"}],
          newName: [{required: true ,message:"管理员帐户名不能为空！", trigger: "blur"},
            { pattern: /^[a-zA-Z0-9_-]{3,20}$/, message:'管理员帐户名格式错误!', trigger:"blur"},],
          newPwd: [{ required: true, message: "请输入密码", trigger: "blur" },
          { pattern:/^[_a-zA-Z0-9]{4,20}$/, message:'新密码应由英文字母，数字以及下划线组成（4~20位）', trigger:"blur"}],
          checkNewPwd:[{ required: true, message: "请输入密码", trigger: "blur" },
          { pattern:/^[_a-zA-Z0-9]{4,20}$/, message:'新密码应由英文字母，数字以及下划线组成（4~20位）', trigger:"blur"},
          { validator: judgePwd, trigger:"blur"}],
      },
    };
    },
    methods:{
      //编辑对话框点击重置
      resetEditForm() {
        this.$refs.ruleForm.resetFields();
      },
      submitEditForm(){
        console.log(this.ruleForm)
        updateAdminInfo(this.ruleForm.oldName, this.ruleForm.oldPwd, this.ruleForm.newName,this.ruleForm.newPwd).then((res) => {
        console.log(res);
          if (res.data==1) {
            this.$message({
              type: "success",
              message: "操作成功!", 
            });
            this.resetEditForm();
          } else if(res.data==-1){
            
            this.$message({
              type: "warning",
              message: "请检查原账户名和密码！",
            });
            this.$refs.oldName.focus();
          }else{
            this.$message({
              type: "info",
              message: "内部操作失败",
            });
          }
      });

      }
    },
  }
</script>