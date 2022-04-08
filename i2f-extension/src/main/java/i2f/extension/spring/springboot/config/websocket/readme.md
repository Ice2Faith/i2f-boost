# websocket 使用简介
- 导入pom.xml中的依赖
- 在启动类上添加注解 @EnableWebsocketConfig
- 添加 application-websocket.properties 中的配置到 application.properties 配置中
- 启动项目
- 用浏览器打开 websocket.html
- 在输入框输入文本，点击send
- 查看到下方的echo回显，则websocket服务已经连接正常，可以正常数据传输
