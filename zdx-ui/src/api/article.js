import request from "@/utils/request";


export const getHomeById = (id) => {
	return request({
		url: `/home/zdx.article/getHomeById/${id}`,
		method: 'GET'
	})
}

export const getArticleRecommend = () => {
	return request({
		url:'/home/zdx.article/recommend',
		method:'GET'
	})
}

export const likeArticle = (id) => {
	return request({
		url:`/zdx.article/likeArticle/${id}`,
		method: 'GET'
	})
}

export const getArchivesList = (params) => {
	return request({
		url:'/home/zdx.article/archives',
		method:'GET',
		params: params
	})
}