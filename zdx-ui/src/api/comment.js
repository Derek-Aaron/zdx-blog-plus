import request from "@/utils/request";

export const pageHomeComment = (params) => {
	return request({
		url:'/home/zdx.comment/page',
		method:'GET',
		params: params
	})
}