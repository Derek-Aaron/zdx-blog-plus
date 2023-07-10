import request from "@/utils/request";

export const getRoleIds = (userId) => {
    return request({
        url:`/zdx.role/roleIds/${userId}`,
        method:'get'
    })
}

export const addOrDelResources = (data) => {
    return request({
        url:'/zdx.role/addOrDelResources',
        method:'POST',
        data: data
    })
}