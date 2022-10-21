# 对应的vue/webpack依赖包
---

## 使用方式
- package.json > dependencies 节点下添加依赖
```json
"dependencies": {
    "axios": "0.21.0",
    "js-base64": "^3.6.1",
    "crypto-js": "^4.1.1",
    "jsrsasign": "10.5.27",
    "vue": "^2.5.2",
    "vue-router": "^3.0.1"
  },
```
- 重新安装依赖
```shell script
npm install
```
- 修改 main.js
```js
import "./secret/secret-core"
import "./secret/secret-axios";
```
- 将 jsencrypt.js 文件放到 static 目录下
- 修改 index.html
```html
<head>
    <script src="static/jsencrypt.js"></script>
  </head>
```
- 使用前需要和服务端握手
```js
this.$axios({
    url: 'test/handle',
    method: 'post',
    data: {}
  }).then(({data}) => {
    let opk = data.data;
    this.$secretWebConfig
      .secretWebCore()
      .storeOtherPublicKey(opk);
  });
```
- 接下来正常使用请求即可
```js
this.$secretAxios({
    url: 'test/both',
    method: 'post',
    data: JSON.parse(this.form.input)
  }).then(({data}) => {
    this.form.output = JSON.stringify(data);
  });
```
