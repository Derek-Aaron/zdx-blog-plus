import request from "@/utils/request";


export const talkPage = (params) => {
	return request({
		url:'/home/zdx.talk/page',
		method:'GET',
		params: params
	})
}

export const getTalk = (id) => {
	return request({
		url:`/home/zdx.talk/getById/${id}`,
		method:'GET'
	})
}

export const getTalkHomeList = () => {
	return request({
		url:'/home/zdx.talk/list',
		method:'GET'
	})
}