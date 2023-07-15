import request from "@/utils/request";

export function uploadFile(file, params) {
    const formData = new FormData();
    formData.append("file", file)
    if (params) {
        formData.append("type", params)
    }
    return request({
        url:'/zdx.file/upload',
        method:"post",
        headers:{
            'Content-Type': 'multipart/form-data'
        },
        data: formData
    })
}


export function getFileUrl(fileId){
    return request({
        url:`/zdx.file/getUrl/${fileId}`,
        method:'get'
    })
}


export function downloadFile(id) {
    return request({
        url:`/zdx.file/${id}/download`,
        method:'get',
        responseType:'blob'
    })
}