import request from "@/utils/request";

export const pageHomeComment = (params) => {
	return request({
		url:'/home/zdx.comment/page',
		method:'GET',
		params: params
	})
}

export const addComment = (data) => {
	return request({
		url:'/zdx.comment/add',
		method:'POST',
		data: data
	})
}

export const getReplyPage = (params) => {
	return request({
		url:'/home/zdx.comment/replyPage',
		method:'GET',
		params: params
	})
}

export const likeComment = (id) => {
	return request({
		url:`/zdx.comment/like/${id}`,
		method:'GET'
	})
}

export const getRecentComment = () => {
	return request({
		url:'/home/zdx.comment/recent',
		method:'GET',
	})
}