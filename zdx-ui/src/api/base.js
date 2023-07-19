import request from "@/utils/request";



export const page = (module, params) => {
	return request({
		url:`/home/zdx.${module}/page`,
		method:'GET',
		params: params
	})
}


export const getById = (module, id) => {
	return request({
		url:`/home/zdx.${module}/getById/${id}`
	})
}