import axios from 'axios'
import Vue from 'vue'
import './secret-core'
import {SecretException, SecretWebConfig} from "./secret-core";

Vue.prototype.$secretWebConfig = SecretWebConfig();

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const secretAxios = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: 'http://localhost:8090/',
    // 超时
    timeout: 60 * 1000
})

Vue.prototype.$secretAxios = secretAxios;

// request拦截器
secretAxios.interceptors.request.use(config => {

    try {
        Vue.prototype.$secretWebConfig.secretFilter().requestFilter(config);
    } catch (e) {
        if (e.type && e.type == 'SecretException') {
            Promise.reject('SecretException:' + e.msg);
        } else {
            Promise.reject('解析消息失败');
        }
    }

    return config
}, error => {
    console.log(error)
    Promise.reject(error)
})

// 响应拦截器
secretAxios.interceptors.response.use(res => {

        try {
            Vue.prototype.$secretWebConfig.secretFilter().responseFilter(res);
        } catch (e) {
            if (e.type && e.type == 'SecretException') {
                Promise.reject('SecretException:' + e.msg);
            } else {
                Promise.reject('解析消息失败');
            }
        }

        return res;
    },
    error => {
        console.log('err', error)
        return Promise.reject(error)
    }
)

export default secretAxios
