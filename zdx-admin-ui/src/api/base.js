import request from "@/utils/request";

/**
 * 分页查询
 * @param {*} module 
 * @param {*} params 
 * @returns 
 */
export const page = (module, params) => {
    return request({
        url:`/zdx.${module}/page`,
        method: 'GET',
        params: params
    })
}

/**
 * 查询全部
 */
export const list = (module) => {
    return request({
        url:`/zdx.${module}/list`,
        method: 'GET',
    })
}


/**
 * 通过id查询
 * @param {} id 
 * @returns 
 */
export const getById = (module, id) => {
    return request({
        url:`/zdx.${module}/getById/${id}`,
        method:'GET',
    })
}

/**
 * 保存数据
 * @param {*} data 
 * @returns 
 */
export const save = (module, data) => {
    return request({
        url:`/zdx.${module}/save`,
        method: 'POST',
        data: data
    })
}

/**
 * 删除数据
 */
export const del = (module, id) => {
    return request({
        url:`/zdx.${module}/delete/${id}`,
        method:'GET',
    })
}

/**
 * 批量删除
 */

export const batchDel = (module, data) => {
    return request({
        url:`/zdx.${module}/batchDelete`,
        method:'POST',
        data: data,
    })
}

export const batchHandle = (module, name, data) => {
    return request({
        url:`/zdx.${module}/${name}`,
        method:'POST',
        data: data,
    })
}

export const baseById = (module, name, params) => {
    return request({
        url:`/zdx.${module}/${name}/${params}`,
        method:'GET'
    })
}

export const baseParams = (module, name, params) => {
    return request({
        url:`/zdx.${module}/${name}`,
        method:'GET',
        params: params
    })
}

export const baseApi = (module, name) => {
    return request({
        url:`/zdx.${module}/${name}`,
        method:'GET'
    })
}