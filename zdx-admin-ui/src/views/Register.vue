<template>
	<div class="login">
		<el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
			<h3 class="title">小赵博客后台管理注册</h3>
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
			<el-form-item prop="confirmPassword">
				<el-input
					v-model="loginForm.confirmPassword"
					type="password"
					size="large"
					auto-complete="off"
					placeholder="确认密码"
					@keyup.enter="handleLogin(loginRef)"
				>
					<template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
				</el-input>
			</el-form-item>
			<el-form-item prop="email" >
				<el-input
					v-model="loginForm.email"
					auto-complete="off"
					placeholder="邮箱"
					@keyup.enter.native="handleLogin(loginRef)"
				>
					<template #prefix><svg-icon icon-class="email" class="el-input__icon input-icon" /></template>
				</el-input>
			</el-form-item>
			<el-form-item prop="code" >
				<el-input
					v-model="loginForm.code"
					auto-complete="off"
					placeholder="验证码"
					style="width: 60%"
					@keyup.enter.native="handleLogin(loginRef)"
				>
					<template #prefix><svg-icon icon-class="code" class="el-input__icon input-icon" /></template>
				</el-input>
				<div class="register-code">
					<el-button class="register-btn" v-if="!codeSuccess" @click="sendCode" style="width:100%;" type="danger">发送</el-button>
					<el-button class="register-btn" v-else disabled @click="sendCode" style="width:100%;" type="success">
						{{ count }}</el-button>
				</div>
			</el-form-item>
			<el-form-item style="width:100%;">
				<el-button
					:loading="loading"
					size="large"
					type="primary"
					style="width:100%;"
					@click.prevent="handleLogin(loginRef)"
				>
					<span v-if="!loading">注 册</span>
					<span v-else>注 册 中...</span>
				</el-button>
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
import SvgIcon from "@/components/SvgIcon/index.vue";
import {sendEmailCode} from "@/api/login";
import {ElMessage} from "element-plus";

const loginRef = ref()
const router = useRouter();

const loginForm = ref({
	username: "",
	password: "",
	confirmPassword:"",
	email:"",
	code:"",
});

const equalToPassword = (rule, value, callback) => {
	if (loginForm.value.password !== value) {
		callback(new Error("两次输入的密码不一致"));
	} else {
		callback();
	}
};

const loginRules = {
	username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
	password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
	email: [{ required: true, trigger: "blur", message: "请输入您的邮箱" }],
	code: [{ required: true, trigger: "blur", message: "请输入您的验证码" }],
	confirmPassword: [{ required: true, message: "确认密码不能为空", trigger: "blur" }, { required: true, validator: equalToPassword, trigger: "blur" }]
};

const loading = ref(false);
const codeSuccess = ref(false);
const count = ref(60)
const captchaType = ref('');
const interval = ref();
const verify = ref();
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

const sendCode = () => {
	sendEmailCode(loginForm.value.email).then(res => {
		if (res.code === 200) {
			codeSuccess.value = true
			ElMessage.success("验证码发送成功有效期：2分钟")
			interval.value =  setInterval(() => {
				if (count.value > 0) {
					count.value--;
				} else {
					codeSuccess.value = false;
					clearInterval(interval.value)
				}
			},1000)
		}
	})
}

const handleSuccess = async () => {
	await useStore().useUser.doRegister(loginForm.value).then(() => {
		router.push("/home")
		loading.value = false
	}).catch(() => {
		loading.value = false
	})
}
onMounted(() => {
})
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
.register-code {
	width: 33%;
	float: right;
	padding-left: 12px;
	.el-button {
		height: 35px;
	}
}
</style>
