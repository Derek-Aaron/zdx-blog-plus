import request from "@/utils/request";

export const tree = (params) => {
    return request({
        url:'/zdx.menu/tree',
        method:'GET',
        params: params
    })
}

export const updateMenuStatic = (data) => {
    return request({
        url:'/zdx.menu/updateMenuStatic',
        method:'POST',
        data: data
    })
}