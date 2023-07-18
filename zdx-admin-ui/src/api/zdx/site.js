import request from "@/utils/request";

export const updateSiteConfig = (data) => {
	return request({
		url:'/zdx.site/save',
		method:'POST',
		data: data
	})
}

export const getSite = () => {
	return request({
		url:"/zdx.site/getSite",
		method:'GET'
	})
}