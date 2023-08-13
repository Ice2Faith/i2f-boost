/**
 * 默认的axios示例
 */
import Vue from 'vue'
import axios from 'axios'
import server from './server'
import SecureTransferFilter from './core/secure-transfer-filter'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const request = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: server.baseUrl,
  // 超时
  timeout: 30 * 60 * 1000
})
// request拦截器
request.interceptors.request.use(config => {
  SecureTransferFilter.requestFilter(config)

  return config
}, error => {
  console.log(error)
  Promise.reject(error)
})

// 响应拦截器
request.interceptors.response.use(res => {
  SecureTransferFilter.responseFilter(res)

  // 未设置状态码则默认成功状态
  let code = res.data.code
  if (code == undefined || code == null) {
    code = 200
  }
  // 获取错误信息
  const msg = res.data.msg
  if (code === 401) {
    alert('登录状态已过期，您可以继续留在该页面，或者重新登录')
  } else if (code === 500) {
    console.error(msg)
    return Promise.reject(new Error(msg))
  } else if (code !== 200) {
    console.warn(msg)
    return Promise.reject('error')
  } else {
    return res
  }
},
error => {
  SecureTransferFilter.responseFilter(error.response)
  console.log('err', error)
  let { message } = error
  if (message == 'Network Error') {
    message = '后端接口连接异常'
  } else if (message.includes('timeout')) {
    message = '系统接口请求超时'
  } else if (message.includes('Request failed with status code')) {
    message = '系统接口' + message.substr(message.length - 3) + '异常'
  }
  // Message.error('错误：'+message);
  alert('错误：' + message)
  return Promise.reject(error)
}
)

Vue.prototype.$axios = request

export default request
