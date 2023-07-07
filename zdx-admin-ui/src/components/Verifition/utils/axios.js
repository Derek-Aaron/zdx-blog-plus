import axios from 'axios';


const service = axios.create({
    baseURL:'/api',
    timeout: 40000,
    headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'Content-Type': 'application/json; charset=UTF-8'
    },
})
service.interceptors.request.use(
    config => {
        return config
    },
    error => {
        Promise.reject(error)
    }
)

// response interceptor
service.interceptors.response.use(
    response => {
        return response.data
    },
    error => {
    }
)
export default service