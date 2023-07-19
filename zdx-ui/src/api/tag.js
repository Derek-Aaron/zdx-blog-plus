import request from "@/utils/request";

export const getTagList = () => {
	return request({
		url:'/home/zdx.tag/list',
		method:'GET'
	})
}

export const tagArticlePage = (params) => {
	return request({
		url:'/home/zdx.tag/articlePage',
		method:'GET',
		params: params
	})
}