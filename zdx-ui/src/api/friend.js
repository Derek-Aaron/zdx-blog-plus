import request from "@/utils/request";

export const getFriendList = () => {
	return request({
		url:'/home/zdx.friend/list',
		method:'GET'
	})
}