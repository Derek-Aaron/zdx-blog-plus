import request from "@/utils/request";



export const batchTrash = (ids) => {
	return request({
		url:"/zdx.article/batchTrash",
		method:'POST',
		data: ids
	})
}