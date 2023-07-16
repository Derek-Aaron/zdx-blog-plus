import request from "@/utils/request";


export const addView = (id) => {
	return request({
		url:`/desk/zdx.article/addView/${id}`,
		method:'GET'
	})
}