import request from "@/utils/request";


export const updateUserEmail = (data) => {
	return request({
		url:'/zdx.user/updateProfile',
		method:'POST',
		data: data
	})
}

export const updateUserAvatar = (data) => {
	return request({
		url:'/zdx.user/updateProfile',
		method: "POST",
		data: data
	})
}

export const updateUserInfo = (data) => {
	return request({
		url:'/zdx.user/updateProfile',
		method: "POST",
		data: data
	})
}