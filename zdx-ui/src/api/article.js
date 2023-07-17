import request from "@/utils/request";


export const getHomeById = (id) => {
	return request({
		url: `/desk/zdx.article/getHomeById/${id}`,
		method: 'GET'
	})
}

export const likeArticle = (id) => {
	return request({
		url:`/zdx.article/likeArticle/${id}`,
		method: 'GET'
	})
}