import axios from 'axios'
import Vue from 'vue'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const request = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: 'http://localhost:8090/',
  // 超时
  timeout: 60 * 1000
})

Vue.prototype.$axios = request;

export default request
