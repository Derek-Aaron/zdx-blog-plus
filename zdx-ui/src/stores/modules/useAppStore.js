import { ref } from 'vue'
import { defineStore } from 'pinia'


export const useAppStore = defineStore('useAppStore', () => {
	const isCollapse = ref(false)
	const searchFlag = ref(false)
	const loginFlag = ref(true)
	const registerFlag = ref(false)
	const forgetFlag = ref(false)
	const emailFlag = ref(false)
	const sideFlag = ref(false)

	const setCollapse = (flag) => {
	  isCollapse.value = flag
	}
	const setLoginFlag = (flag) => {
	  loginFlag.value = flag
	}

	const setRegisterFlag = (flag) => {
		registerFlag.value = flag
	}

	const setForgetFlag = (flag) => {
		forgetFlag.value = flag
	}
	return {isCollapse, searchFlag, loginFlag, registerFlag, forgetFlag, emailFlag, sideFlag, setCollapse, setLoginFlag, setRegisterFlag, setForgetFlag}
}, {
	persist: sessionStorage
})