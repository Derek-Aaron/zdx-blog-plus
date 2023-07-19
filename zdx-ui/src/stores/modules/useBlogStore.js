import {reactive, ref} from 'vue'
import {defineStore} from 'pinia'


export const useBlogStore = defineStore('useBlogStore', () => {
	const blogInfo = reactive({
		articleCount: 0,
		categoryCount: 0,
		tagCount: 0,
		viewCount: 0,
		siteConfig: {
			userAvatar: 'http://static.zhaodengxuan.top/image/defalutAvatar.jpg',
			touristAvatar: 'http://static.zhaodengxuan.top/image/defalutAvatar.jpg',
			siteName: '小赵博客',
			siteAddress: 'www.zhaodengxuan.top',
			siteIntro: '',
			siteNotice: '',
			createSiteTime: '',
			recordNumber: '',
			authorAvatar: 'http://static.zhaodengxuan.top/image/defalutAvatar.jpg',
			siteAuthor: '小赵',
			articleCover: '',
			aboutMe: '',
			github: '',
			gitee: '',
			bilibili: '',
			qq: '',
			commentCheck: true,
			messageCheck: true,
			isReward: true,
			weiXinCode: '',
			aliCode: '',
			emailNotice: true,
			socialList: '',
			loginList: '',
			isMusic: true,
			musicId: '',
			isChat: '',
			websocketUrl: ''
		}
	})


	const setBlogInfo = (val) => {
		blogInfo.articleCount = parseInt(val.articleCount)
		blogInfo.categoryCount = parseInt(val.categoryCount)
		blogInfo.tagCount = parseInt(val.tagCount)
		blogInfo.viewCount = parseInt(val.viewCount)
		blogInfo.siteConfig = val.siteConfig
	}

	return {blogInfo, setBlogInfo}
}, {
	persist: sessionStorage
})