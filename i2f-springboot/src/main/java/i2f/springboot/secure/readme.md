# RSA+AES secure transfer solution usage
---
## 简介
- 方案说明
    - 采用RSA+AES组合形式完成前后端交互的加解密过程
- 总体流程
    - 客户端
        - 登录后获取RSA公钥
    - 服务端
        - 项目启动后生成RSA公钥私钥
        - 公钥发送给登录成果的客户端
        - 私钥自己保存
- 发送数据
    - 客户端
        - 随机生成一个AES秘钥
        - 使用客户端的RSA公钥对AES秘钥加密，放入请求头secure
        - 使用AES秘钥对请求体进行加密（也可以对其他部分加密，比如URL参数等）
        - 发送请求
    - 服务端
        - 随机生成一个AES秘钥
        - 使用服务端的RSA私钥对AES秘钥加密，放入响应头secure
        - 使用AES秘钥对响应体进行加密
        - 结束响应
- 接受数据
    - 客户端
        - 从响应头中获取响应头secure
        - 将secure内容使用客户端RSA公钥解密得到随机的AES秘钥
        - 使用得到的AES秘钥解密响应体得到JSON串
        - 对JSON串解析得到JSON对象
        - 使用JSON对象即可
    - 服务端
        - 从请求头中获取请求头secure
        - 将secure内容使用服务端RSA私钥解密得到随机的AES秘钥
        - 使用得到的AES秘钥解密请求体得到解密内容
        - 将解密内容重新包装为请求交给spring处理，自动完成请求参数注入
        - 接口中直接使用即可
        - 特别的，如果这个接口的参数不再请求体中
        - 则使用@SecureParams注解作用在对应的参数上，AOP完成解密直接使用即可
- 注意
    - 请求和响应中，不包含secure则认为是不加密的
    - 如果实际数据时加密的，那将会失败，无法使用数据

---
## 使用示例
- 服务端
- 直接是请求体中的，则只需要请求头中存在secure即可
- 另外这里在方法上加了@SecureParams注解，其中out默认为true
- 则代表对返回值加密响应给前端
```java
@SecureParams
@PostMapping("safe")
public Object safe(@RequestBody UserDto user){
    return user;
}
```
- 这是另一种，加密参数在URL中的形式
- 因为这里的password在URL参数中，因此无法被正常的请求体解密处理
- 因此在参数上添加@SecureParams注解，其中in默认为true
- 则会自动进行解密
- 方法上也有该注解，上面已经说了，不再重复
```java
@SecureParams
@PostMapping("param")
public Object param(@SecureParams String password){
    System.out.println("password:"+password);
    return password;
}
```

## 如何获取与存储RSA公钥
- 服务端提供一个接口提供给客户端调用
- 接口返回内容从 SecureTransfer.getWebRsaPublicKey() 获取
- 可以如下定义：
- 也可以通过配置i2f.springboot.config.secure.api.enable=true直接启用内置的SecureController提供接口secure/key
```java
@RestController
@RequestMapping("secure")
public class SecureController {

    @Autowired
    private SecureTransfer secureTransfer;

    @RequestMapping("key")
    public Object rsa(){
        Map<String,Object> ret=new HashMap<>();
        String pubKey= secureTransfer.getWebRsaPublicKey();
        ret.put("code",200);
        ret.put("msg","ok");
        ret.put("data",pubKey);
        return ret;
    }
}
```
- 客户端收到之后进行保存
- 默认是存储在session中，如有其他需要，请修改secure-transfer.js
```js
this.$axios({
    url: 'secure/rsa',
    method: 'GET'
  }).then(({data})=>{
    this.$secureTransfer.saveRsaPubKey(data.data);
  })
```

---
## 如何使用
### 服务端（springboot环境）
#### 安装
- maven添加依赖
```xml
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk15on</artifactId>
    <version>1.52</version>
</dependency>
```
- 引入本包secure
- 如果本包在项目的扫描路径下，则不需要配置
- 如果不再扫描路径下，则在启动类上添加注解 @EnableSecureConfig 注解，以自动引入此功能
- 剩下就是使用了，在上面的示例中已经演示了，如何使用
#### 使用
- 查看上面的使用示例

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
import './secure/secure-vue-main'
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
import SecureTransferFilter from ".//secure-transfer-filter";
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
