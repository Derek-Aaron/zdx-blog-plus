import request from "@/utils/request";


export const getProfile = () => {
    return request({
        url:'/zdx.user/profile',
        method:'get'
    })
}

export const updateProfile = (data) => {
    return  request({
        url:'/zdx.user/updateProfile',
        method:'post',
        data: data
    })
}

export const resetPassword =  (data) => {
    return request({
        url:'/zdx.user/resetPwd',
        method:'post',
        data: data
    })
}

export const updateUserStatus = (data) => {
    return request({
        url:'/zdx.user/updateUserStatus',
        method:'post',
        data: data
    })
}

export const listUserAll = (params) => {
    return request({
        url:'/zdx.user/listUserAll',
        method:'get',
        params: params
    })
}