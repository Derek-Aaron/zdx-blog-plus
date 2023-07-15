import request from "@/utils/request";


export const addPhoto = (data) => {
	return request({
		url:'/zdx.photo/add',
		method:'POST',
		data: data
	})
}