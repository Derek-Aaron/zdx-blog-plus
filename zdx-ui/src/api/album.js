import request from "@/utils/request";

export const getAlbumList = () => {
	return request({
		url:'/home/zdx.album/list'
	})
}

export const getPhotoList = (albumId) => {
	return request({
		url:`/home/zdx.album/photoList/${albumId}`,
		method:'GET'
	})
}