/**
 * 服务器配置
 */
import Vue from 'vue'

const _server={
  baseUrl:'http://localhost:8081/app',
  loginUrl:'login'
};

Vue.prototype.$server=_server;

export default _server;
