import request from "@/utils/request";



export const batchTrash = (ids) => {
	return request({
		url:"/zdx.article/batchTrash",
		method:'POST',
		data: ids
	})
}

export const batchSync = (ids) => {
	return request({
		url:'/zdx.article/sync',
		method:"POST",
		data: ids
	})
}

export const batchRecover = (ids) => {
	return request({
		url:"/zdx.article/batchRecover",
		method:'POST',
		data: ids
	})
}

export const articleUpload = (formData) => {
	return request({
		url:'/zdx.article/upload',
		method:"post",
		headers:{
			'Content-Type': 'multipart/form-data'
		},
		data: formData
	})
}