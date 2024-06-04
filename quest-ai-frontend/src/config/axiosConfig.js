import axios from 'axios';

// 创建axios实例
const request = axios.create({
    baseURL: '/api', // API的基础URL
    timeout: 10000 // 请求超时时间
});

// 请求拦截器
request.interceptors.request.use(
    config => {
        // 在发送请求之前做些什么，例如添加token
        config.headers.Authorization = `Bearer ${localStorage.getItem('token') || ''}`;
        return config;
    },
    error => {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);

// 响应拦截器
request.interceptors.response.use(

    response => {
        console.log(response)
        // 对响应数据做点什么
        if (response.status === 200) {
            return response.data;
        } else {
            // 可以在这里处理非200的响应码
            return Promise.reject(new Error('Error'));
        }
    },
    error => {
        // 对响应错误做点什么
        if (error.response && error.response.data) {
            // 可以根据实际业务需求处理HTTP错误（如401、403、404等）
            alert(error.response.data.message);
        } else {
            alert(error.response.data.message);
        }
        return Promise.reject(error);
    }
);

export default request;
