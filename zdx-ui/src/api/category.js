import request from "@/utils/request";

export const getCategoryList = () => {
	return request({
		url:'/home/zdx.category/list',
		method:'GET'
	})
}

export const categoryArticlePage = (params) => {
	return request({
		url:"/home/zdx.category/articlePage",
		method:'GET',
		params: params
	})
}