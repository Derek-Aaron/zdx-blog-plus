import axios from "axios";
import { ElMessage, ElMessageBox } from "element-plus";
import router from "../router";
import { getToken } from "./auth";
import { encrypt } from "@/utils/ras";
import { useUserStore } from "@/stores/modules/useUserStore";

// 取消重复请求
const pending = [];
const CancelToken = axios.CancelToken;
// 移除重复请求
const removePending = (config) => {
  for (const key in pending) {
    const item = +key;
    const list = pending[key];
    // 当前请求在数组中存在时执行函数体
    if (list.url === config.url && list.method === config.method && JSON.stringify(list.params) === JSON.stringify(config.params) && JSON.stringify(list.data) === JSON.stringify(config.data)) {
      // 执行取消操作
      list.cancel('操作太频繁，请稍后再试');
      // 从数组中移除记录
      pending.splice(item, 1);
    }
  }
};
const service = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': "application/json;charset=UTF-8"
  }
})

service.interceptors.request.use(request => {
  removePending(request)
  const isToken = (request.headers || {}).isToken === false
  const isEncrypt = (request.headers || {}).isEncrypt === "true" || (request.headers || {}).isEncrypt === true
  if (!isToken && getToken()) {
    // 让每个请求携带自定义token 请根据实际情况自行修改
    if (request.headers) {
      request.headers['Authorization'] = 'Bearer ' + getToken()
    }
  }
  request.cancelToken = new CancelToken((c) => {

    pending.push({ url: request.url, method: request.method, params: request.params, data: request.data, cancel: c });
  });
  if (isEncrypt && (request.method === 'post' || request.method === 'POST')) {
    let str = encrypt(JSON.stringify(request.data))
    request.data = { encrypt: str }
  }
  if (isEncrypt && (request.method === 'get' || request.method === 'GET')) {
    let str = encrypt(JSON.stringify(request.params))
    request.params = { encrypt: str }
  }
  // get请求映射params参数
  if (request.method === 'get' && request.params) {
    let url = request.url + '?' + tansParams(request.params);
    url = url.slice(0, -1);
    request.params = {};
    request.url = url;
  }
  return request;
}, error => {
  return Promise.reject(error);
})


service.interceptors.response.use(response => {
  removePending(response.config);
  if (response.request.responseType === 'blob' || response.request.responseType === 'arraybuffer') {
    return response
  }
  const errorCode = response?.data?.code;
  if (errorCode === 401) {
    ElMessageBox.confirm( response.data.message + '，您可以继续留在该页面，或者重新登录', '系统提示', { confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning' }).then(() => {
      useUserStore().doLogout().then(() => {
        location.href = "/"
      })
    })
    return response.data
  }
  if (errorCode === 403) {
    ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', { confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning' }).then(() => {
      useUserStore().doLogout().then(() => {
        location.href = "/"
      })
    })
    return response.data
  }
  if (errorCode !== 200) {
    ElMessage.error(response.data.message)
  } else {
    return response.data
  }

}, error => {
  console.log(error)
  const response = error.response?.data;
  if (response?.code === 403 || response?.code === 401) {
    ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', { confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning' }).then(() => {
      useUserStore().doLogout().then(() => {
        location.href = "/"
      })
    })
  }
  if (response?.code === 404) {
    ElMessage.error("服务未找到")
  }
  if (response.code === 500) {
    ElMessage.error("服务器发生异常")
  }
  if (response.status === 404) {
    ElMessage.error("服务未找到")
  }
  if (response.status === 500) {
    ElMessage.error("服务器发生异常")
  }
})

export default service;

function tansParams(params) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName];
    let part = encodeURIComponent(propName) + "=";
    if (value !== null && typeof (value) !== "undefined") {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && typeof (value[key]) !== 'undefined') {
            let params = propName + '[' + key + ']';
            let subPart = encodeURIComponent(params) + "=";
            result += subPart + encodeURIComponent(value[key]) + "&";
          }
        }
      } else {
        result += part + encodeURIComponent(value) + "&";
      }
    }
  }
  return result
}