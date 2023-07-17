import { ref } from 'vue'
import { defineStore } from 'pinia'
import { homeInfo, login, logout } from "@/api/login";
import { setToken, removeToken } from "@/utils/token";


export const useUserStore = defineStore('useUserStore', () => {
	const id = ref('')
	const avatar = ref('')
	const nickname = ref('')
	const email = ref('')
	const username = ref('')
	const webSite = ref('')
	const intro = ref('')
	const loginType = ref('')
	const path = ref('')
	const articleLikeSet = ref([])
	const commentLikeSet = ref([])
	const talkLikeSet = ref([])

	const doUserInfo = () => {
		homeInfo().then(res => {
			id.value = res.data.id
			avatar.value = res.data.avatar
			nickname.value = res.data.nickname
			email.value = res.data.email
			username.value = res.data.username
			webSite.value = res.data.webSite
			intro.value = res.data.intro
			loginType.value = res.data.loginType
			articleLikeSet.value = res.data.articleLikeSet
			commentLikeSet.value = res.data.commentLikeSet
			talkLikeSet.value = res.data.talkLikeSet
		})
	}

	const doLogin = (data) => {
		return new Promise((resolve, reject) => {
			login(data).then(res => {
				setToken(res.data.token)
				doUserInfo()
				resolve(res)
			}).catch(err => {
				reject(err)
			})
		})
	}
	const doLogout = () => {
		logout().then(() => {
			removeToken()
			forceLogOut()
		})
	}
	const forceLogOut = () => {
		id.value = ''
		avatar.value = ''
		nickname.value = ''
		email.value = ''
		username.value = ''
		webSite.value = ''
		intro.value = ''
		loginType.value = ''
		articleLikeSet.value = []
		commentLikeSet.value = []
		talkLikeSet.value = []
	}
	const savePath = (val) => {
		path.value = val
	}

	const articleLike = (articleId) => {
		const index = articleLikeSet.value.indexOf(articleId)
		if (index !== -1) {
			articleLikeSet.value.splice(index, 1);
		} else {
			articleLikeSet.value.push(articleId);
		}
	}

	const commentLike = (commentId) => {
		const index = commentLikeSet.value.indexOf(commentId)
		if (index !== -1) {
			commentLikeSet.value.splice(index, 1);
		} else {
			commentLikeSet.value.push(commentId);
		}
	}

	const talkLike = (talkId) => {
		const index = talkLikeSet.value.indexOf(talkId)
		if (index !== -1) {
			talkLikeSet.value.splice(index, 1);
		} else {
			talkLikeSet.value.push(talkId);
		}
	}
	const updateUserInfo = (user) => {
		nickname.value = user.nickname;
		webSite.value = user.webSite;
		intro.value = user.intro;
	}
	return {
		id,
		avatar,
		nickname,
		email,
		username,
		webSite,
		intro,
		loginType,
		path,
		articleLikeSet,
		commentLikeSet,
		talkLikeSet,
		doLogin,
		doUserInfo,
		doLogout,
		forceLogOut,
		savePath,
		articleLike,
		commentLike,
		talkLike,
		updateUserInfo
	}
}, {
	persist: sessionStorage
})