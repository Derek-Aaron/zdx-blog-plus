import request from "@/utils/request";

export const report = () => {
	return request({
		url:'/home/report',
		method:'GET',
	})
}

export const blogInfo = () => {
	return request({
		url:'/home/',
		method:'GET'
	})
}