import request from "@/utils/request";

export const report = () => {
	return request({
		url:'/desk/report',
		method:'POST',
	})
}

export const blogInfo = () => {
	return request({
		url:'/desk/',
		method:'GET'
	})
}