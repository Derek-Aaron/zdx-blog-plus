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
		url:'/user/info',
		method:'GET'
	})
}

export const getEmailCode = (email) => {
	return request({
		url:`/home/email/code/${email}`,
		method:'GET',
	})
}

export const register = (data) => {
	return request({
		url:'/home/register',
		method:'POST',
		headers:{
			isToken: false,
			isEncrypt: true
		},
		data: data
	})
}

export const logout = () => {
	return request({
		url:'/logout',
		method:'GET'
	})
}

export const authList = () => {
	return request({
		url:'/home/zdx.auth/list',
		method:'GET'
	})
}

export const loginType = (source) => {
	return request({
		url:`/oauth/render/${source}`,
		method:'GET'
	})
}

export function callback(params) {
	return request({
		url:'/oauth/callback',
		method:'GET',
		params: params
	})
}