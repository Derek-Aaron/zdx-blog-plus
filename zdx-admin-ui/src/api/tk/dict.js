import request from "@/utils/request";


export const getDicts = (key) => {
    return request({
        url:`/zdx.dict/key/${key}`,
        method: 'GET',
    })
}