<template>
	<div class="oauth-background">
		<div id="preloader_6">
			<span></span>
			<span></span>
			<span></span>
			<span></span>
		</div>
	</div>
</template>

<script setup>
import useStore from "@/stores";
import {setToken} from "@/utils/token";
import {useRoute, useRouter} from "vue-router";
import {onMounted} from "vue";
import {callback} from "@/api/login";

const {user} = useStore();
const router = useRouter();
const route = useRoute();
onMounted(() => {
	const formBody = {
		state: useRouter().currentRoute.value.query.state,
		code: useRouter().currentRoute.value.query.code
	}
	callback(formBody).then((res) => {

	})
});
</script>

<style scoped>
.oauth-background {
	position: fixed;
	top: 0;
	bottom: 0;
	left: 0;
	right: 0;
	background: #fff;
	z-index: 1000;
}

#preloader_6 {
	position: relative;
	top: 45vh;
	left: 47vw;
	animation: preloader_6 5s infinite linear;
}

#preloader_6 span {
	width: 20px;
	height: 20px;
	position: absolute;
	background: red;
	display: block;
	animation: preloader_6_span 1s infinite linear;
}

#preloader_6 span:nth-child(1) {
	background: #2ecc71;
}

#preloader_6 span:nth-child(2) {
	left: 22px;
	background: #9b59b6;
	animation-delay: 0.2s;
}

#preloader_6 span:nth-child(3) {
	top: 22px;
	background: #3498db;
	animation-delay: 0.4s;
}

#preloader_6 span:nth-child(4) {
	top: 22px;
	left: 22px;
	background: #f1c40f;
	animation-delay: 0.6s;
}

@keyframes preloader_6 {
	from {
		-ms-transform: rotate(0deg);
	}

	to {
		-ms-transform: rotate(360deg);
	}
}

@keyframes preloader_6_span {
	0% {
		transform: scale(1);
	}

	50% {
		transform: scale(0.5);
	}

	100% {
		transform: scale(1);
	}
}
</style>