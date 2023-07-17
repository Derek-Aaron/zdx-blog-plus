import request from "@/utils/request";


export const login = (data) => {
	return request({
		url:'/login',
		method:'POST',
		headers:{
			isToken: false,
			isEncrypt: true
		},
		data: data
	})
}


export const homeInfo = () => {
	return request({
		url:'/homeInfo',
		method:'GET'
	})
}

export const logout = () => {
	return request({
		url:'/logout',
		method:'GET'
	})
}