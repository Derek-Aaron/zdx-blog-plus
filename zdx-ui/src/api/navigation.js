import request from "@/utils/request";


export const group = () => {
	return request({
		url:'/home/zdx.navigation/group',
		method:'get'
	})
}