import request from "@/utils/request";



export const page = (module, params) => {
	return request({
		url:`/desk/zdx.${module}/page`,
		method:'GET',
		params: params
	})
}


export const getById = (module, id) => {
	return request({
		url:`/desk/zdx.${module}/getById/${id}`
	})
}