import request from "@/utils/request";

export const getMessageList = () => {
	return request({
		url:'/home/zdx.message/list',
		method:'GET'
	})
}

export const addMessage = (data) => {
	return request({
		url:'/home/zdx.message/add',
		method:'POST',
		data: data
	})
}