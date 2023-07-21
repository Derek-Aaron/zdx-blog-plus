<script setup>
import {onMounted, ref} from "vue";
import {callback} from "@/api/login";
import {useRouter} from "vue-router";
import {setToken} from "@/utils/auth";
import router from "@/router";

const loading = ref(false)
onMounted(() => {
	loading.value = true
	const formBody = {
		state: useRouter().currentRoute.value.query.state,
		code: useRouter().currentRoute.value.query.code
	}
	callback(formBody).then((res) => {
		loading.value =false
		setToken(res.data.token)
		router.push("/profile")
	})
})
</script>

<template>
	<div v-loading="loading" style="height: 100%;width: 100%;">
		正在加载中...
	</div>
</template>

<style scoped lang="scss">

</style>