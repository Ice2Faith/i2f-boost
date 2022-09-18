# RSA+AES前端接入教程
---

## 系统rsa公钥获取接口
- 系统加载是就调用
    - 也就是 login.vue --> mounted
- 接口
```bash
POST secure/key
```
- 请求体
    - 无
- 响应体
    - 响应体中key的使用，见下方
    - 响应的data就是key,将key存储起来即可
```json
{
  "code": 0,
  "data": "$.2MI5Gf6MioDGkFOyF4FCaAQ8AB",
  "msg": "操作成功！",
  "success": true
}

```

## 保存靠西系统rsa公钥
- 客户端收到之后进行保存
- 默认是存储在session中，如有其他需要，请修改secure-transfer.js
```js
this.$axios({
    url: 'secure/key',
    method: 'POST'
  }).then(({data})=>{
    this.$secureTransfer.saveRsaPubKey(data);
  })
```

### 客户端（vue环境）
#### 安装
- 引入本包secure
- 添加package.json依赖
- 当然你也可以单独npm install这些依赖，这里使用另一种方式
- 先添加前三个依赖到对应的dependencies节点中，直接复制进去即可
- 这里保留了vue的两个依赖，方便做参考
```json
"dependencies": {
    "axios": "0.21.0",
    "js-base64": "^3.6.1",
    "crypto-js": "^4.1.1",
    "vue": "^2.5.2",
    "vue-router": "^3.0.1"
},
```
- 保存package.json之后，进入自己的项目路径
- 进行npm install,这就会自动把新加的依赖进行下载
```bash
npm install
```
- 将本包中的static文件夹中的jsencrtpt.js文件，复制到自身项目的static文件夹中
- 【注意】，你可能知道jsencrypt有现成的npm依赖可以用
- 但是不要那么做，npm中的jsencrypt不能使用，这是别人从jsencrypt分支出来的一个修复版本
- 所以，不要替换成npm依赖，否则将不会正常工作
- 下面是static文件夹结构
```bash
- web-root
    - build
    - config
    - node_modules
    - src
        - secure
            - secure-vue-main.js
            - ...
        - App.vue
        - main.js
    - static
    - index.html
```
- 在index.html中，添加jsencrypt.js的引用
```html
<head>
    <script src="static/jsencrypt.js"></script>
  </head>
```
- 在main.js中引入本包
```js
import './secure-vue-main'
```
- web端是基于过滤器实现的自动加解密
- 因此，需要对请求响应拦截器进行配置
- 以axios中使用请求响应拦截器为例
- 简单的封装，可以以此文件作为参考
```js
./secure/secure-axios.js
```
- 下面介绍，自己封装的过程
- 在axios包装中，引入过滤器（当然还有必不可少的axios）
- 引入axios
```js
import axios from 'axios'
```
- 引入过滤器
```js
import SecureTransferFilter from "../secure/secure-transfer-filter";
```
- 添加一个请求实例
```js
const request = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: 'http://localhost:9090',
  // 超时
  timeout: 60000
})
```
- 为这个实例，添加请求拦截器
```js
// request拦截器
request.interceptors.request.use(config => {

  console.log('headers:',config.headers);

  // 核心过滤器
  SecureTransferFilter.requestFilter(config)

  console.log('reqUrl:',config.url);

  return config
}, error => {
  console.log(error)
  Promise.reject(error)
})
```
- 添加响应拦截器
```js
// 响应拦截器
request.interceptors.response.use(res => {
    console.log('res:',res);

    // 核心过滤器
    SecureTransferFilter.responseFilter(res);

    // 未设置状态码则默认成功状态
    let code = res.data.code ;
    if(code==undefined || code==null){
      code=200;
    }
    // 获取错误信息
    const msg =  res.data.msg
    if (code !== 200) {
      console.warn(msg);
      return Promise.reject(new Error(msg))
    } else {
      return res
    }
  },
  error => {
    console.log('err' , error)
    return Promise.reject(error)
  }
)
```
- 下面为了方便使用，将其绑定到Vue原型上
```js
import Vue from 'vue'

Vue.prototype.$axios=request;
```
- 下面开始使用

#### 使用
- 使用post请求
- 主要的就是添加一个secure的请求头
- 过滤器，将会检测这个请求头，如果包含这个请求头，将会进行自动的data加密
```js
this.$axios({
    url: 'test/safe',
    method: 'POST',
    data:{
      userId:'1001',
      userName: '张',
      tel: '13122223333',
      password: 'pass'
    },
    headers:this.$secureTransfer.getSecureHeader(false)
  }).then(({data})=>{
    this.form.output=data;
  })
```
- 使用URL参数params
```js
this.$axios({
    url:'test/param',
    method:'POST',
    params:{
      password: this.form.input
    },
    headers:this.$secureTransfer.getSecureHeader(true)
  }).then(({data})=>{
    this.form.output=data
  })
```
- 上面可以看出，都调用了headers:this.$secureTransfer.getSecureHeader(true)
- 来设置加密请求头完成自动加密，所以
- 可以在系统中，考虑单独封装一个axios用以加密传输
