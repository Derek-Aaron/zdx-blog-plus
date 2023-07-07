import request from "@/utils/request";

export const login = (data) => {
    return request({
        url:'/login',
        method:'post',
        headers:{
            isToken: false
        },
        data: data
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