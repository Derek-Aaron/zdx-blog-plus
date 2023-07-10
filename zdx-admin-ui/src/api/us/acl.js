import request from "@/utils/request";

export const subject = (params) => {
    return request({
        url:'/zdx.acl/subject',
        method: 'GET',
        params: params
    })
}