<template>
  <div style="width: 100%; height: 100%">
    <div
      class="login-container"
      :style="
        'background-image: url(' +
        backgroundImage +
        ');background-size:100% 100%'
      "
    >
      <el-form
        ref="loginForm"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        auto-complete="on"
        label-position="left"
      >
        <div class="title-container">
          <h3 class="title">管理员登录</h3>
        </div>
        <el-form-item prop="admName">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            v-model="loginForm.admName"
            name="admName"
            type="text"
            tabindex="1"
            auto-complete="on"
            placeholder="管理员账户"
          />
        </el-form-item>

        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            v-model="loginForm.password"
            type="password"
            name="password"
            auto-complete="on"
            tabindex="2"
            show-password
            placeholder="管理员密码"
            @keyup.enter.native="handleLogin"
          />
        </el-form-item>

        <el-button
          :loading="loading"
          type="primary"
          style="width: 100%; margin-bottom: 30px"
          @click.native.prevent="handleLogin"
          >登录</el-button
        >
      </el-form>
    </div>
  </div>
</template>

<script>
import backgroundImage from "@/assets/image/background.jpg";
import { doLogin } from "@/api/user";
import { setToken } from "@/utils/auth";
export default {
  name: "Login",
  components: {},
  data() {
    return {
      loginForm: {
        admName: "admin",
        password: "admin",
        code: "",
      },
      codeImg: "",
      loginRules: {
        admName: [
          { required: true, message: "管理员账户不允许为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "管理员密码不允许为空", trigger: "blur" },
          {
            pattern: /^[_a-zA-Z0-9]{4,20}$/,
            message: "密码仅由英文字母，数字以及下划线组成（4~20位）",
            trigger: "blur",
          },
        ],
      },
      loading: false,
      backgroundImage: backgroundImage,
      codeDialogVisible: false,
    };
  },
  watch: {},
  created() {},
  destroyed() {},
  methods: {
    handleLogin() {
      console.log(this.loginForm.admName, this.loginForm.password);

      doLogin(this.loginForm.admName, this.loginForm.password).then((res) => {
        console.log(res);
        if (res.success) {
          this.$message({
            type: "success",
            message: "登录成功!",
          });
          //res.data.tokenValue
          setToken(res.data.tokenValue);
          this.$router.push({ path: "/dashboard" }).catch((err) => {
            console.log(err);
          });
        }
      });
    },
  },
};
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 55px;
    width: 90%;

    input {
      background: transparent;
      border: 0px;
      border-radius: 0px;
      padding: 12px 0px 12px 25px;
      color: $light_gray;
      height: 55px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 200px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }
  .login-code {
    padding-top: 5px;
    float: right;
    img {
      cursor: pointer;
      vertical-align: middle;
    }
  }
  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: #e6ebf5;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }
}
</style>
