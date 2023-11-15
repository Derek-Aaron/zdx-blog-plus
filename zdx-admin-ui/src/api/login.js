import request from "@/utils/request";

export const login = (data) => {
    return request({
        url:'/login',
        method:'post',
        headers:{
            isToken: false,
            isEncrypt: true
        },
        data: data
    })
}

export const authList = () => {
    return request({
        url:'/home/zdx.auth/list/admin',
        method:'GET'
    })
}

export const loginType = (source) => {
    return request({
        url:`/oauth/render/${source}/admin`,
        method:'GET'
    })
}

export const sendEmailCode = (email) => {
    return request({
        url:`/home/email/code/${email}`,
        method:'get'
    })
}

export const register = (data) => {
    return request({
        url:'/home/register',
        method:'post',
        headers:{
            isToken: false,
            isEncrypt: true
        },
        data: data
    })
}

export function callback(params) {
    return request({
        url:'/oauth/callback/admin',
        method:'GET',
        params: params
    })
}


export const info = () => {
    return request({
        url:"/info",
        method:'get'
    })
}

export const getRouters = () => {
    return request({
        url:'/router',
        method:'get'
    })
}

export const logout = () => {
    return request({
        url:'/logout',
        method:'get'
    })
}