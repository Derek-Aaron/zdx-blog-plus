<template>
  <div class="login">
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">小赵博客后台管理</h3>
      <el-form-item prop="username">
        <el-input
            v-model="loginForm.username"
            type="text"
            size="large"
            auto-complete="off"
            placeholder="账号"
        >
          <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            v-model="loginForm.password"
            type="password"
            size="large"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter="handleLogin(loginRef)"
        >
          <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0 0 25px 0;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
            :loading="loading"
            size="large"
            type="primary"
            style="width:100%;"
            @click.prevent="handleLogin(loginRef)"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right; width: 100%" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
			<el-divider v-if="auths.length !== 0">登录方式</el-divider>
			<div v-if="auths.length !== 0" style="display: flex;justify-content: space-between">
				<div v-for="item in auths">
					<svg-icon :icon-class="item.icon" size="2rem" @click="getLoginType(item.source)" />
				</div>
			</div>
        </div>
      </el-form-item>
    </el-form>
    <zdx-verify :captcha-type="captchaType" ref="verify" mode="pop" :imgSize="{width:'400px',height:'200px'}" @success="handleSuccess"/>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2018-2023 zhaodengxuan.top All Rights Reserved.</span>
    </div>
  </div>
</template>

<script setup>
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import zdxVerify from "@/components/Verifition/index.vue";
import {useStore} from "@/stores";
import {authList, loginType} from "@/api/login";
import SvgIcon from "@/components/SvgIcon/index.vue";

const loginRef = ref()
const router = useRouter();
const auths = ref([])

const loginForm = ref({
  username: "",
  password: "",
  rememberMe: false,
});

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
};

const loading = ref(false);
const captchaType = ref('');
const verify = ref();
// 注册开关
const register = ref(true);
const onShow = (type)=>{
  captchaType.value = type
  verify.value.show()
}
const handleLogin =  async (formEl) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      onShow('blockPuzzle')
      loading.value = true;
    } else {
      console.log('error submit!', fields)
    }
  })
}

const handleSuccess = async () => {
  await useStore().useUser.doLogin(loginForm.value).then(() => {
    router.push("/home")
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}
onMounted(() => {
	authAll()
})
const authAll = () => {
	authList().then((res) => {
		auths.value = res.data
	})
}

const getLoginType = (type) => {
	loginType(type).then((res) => {
		location.href = res.data.url
	})
}
</script>

<style lang='scss' scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 40px;
    input {
      height: 40px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial,serif;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 40px;
  padding-left: 12px;
}
</style>
