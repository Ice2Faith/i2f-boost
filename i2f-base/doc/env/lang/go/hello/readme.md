# Goboot 配置化Web开发

## 简介
- 启动日志预览
```shell script
________  ________  ________  ________  ________  _________   
|\   ____\|\   __  \|\   __  \|\   __  \|\   __  \|\___   ___\ 
\ \  \___|\ \  \|\  \ \  \|\ /\ \  \|\  \ \  \|\  \|___ \  \_| 
 \ \  \  __\ \  \\\  \ \   __  \ \  \\\  \ \  \\\  \   \ \  \  
  \ \  \|\  \ \  \\\  \ \  \|\  \ \  \\\  \ \  \\\  \   \ \  \ 
   \ \_______\ \_______\ \_______\ \_______\ \_______\   \ \__\
    \|_______|\|_______|\|_______|\|_______|\|_______|    \|__|

[INFO] app [ go-server ] on [ dev ] run at port [ 8080 ] on time 2023-02-10 16:48:51
[INFO] local: http://localhost:8080/
```
- Goboot
- 是基于 Golang 的 web 开发框架 gin 实现的
- 一个二次包装的配置化开发模板
- 结合和部分Springboot的开发特性
- 在 Go 支持的情况下
- 实现配置化开发
- 实现常见配置支持
- 实现自动路由匹配

## 快速入门
- 第一步，安装GO环境
- 第二步，使用gomod初始化自己的项目
- 下面以项目名hello为例
- 新建项目 hello
```shell script
mkdir hello
```
- 进入项目
```shell script
cd hello
```
- 初始化gomod项目
- 注意，项目名要和创建的文件夹名称一致
```shell script
go mod init hello
```
- 第三步，拷贝goboot到项目中
- 这是目前的项目结构
```shell script
hello
|---goboot
    |---goboot.go
|---go.mod
```
- 第四步，编写自己的入口文件
- 也就是main.go
```shell script
vi main.go
```
```go
package main

import (
	"fmt"
	"hello/goboot"
	"time"

	"github.com/gin-gonic/gin"
)

// 定义API处理结构体
type Api struct {
}

// 给结构体绑定函数
// 这里的函数名为Hello
// 在后面会仔细讲解函数名的问题
func (api *Api) Hello(c *gin.Context) {
	c.JSON(200, gin.H{
		"hello": "hello",
	})
}

// 定义主函数
func main() {
	// 获得默认的应用对象
    app := goboot.GetDefaultApplication()
    // 添加mapping处理对象，注意是指针
	app.AddHandlers(&Api{})
    // 添加默认主页的GET请求处理
	app.App.GET("/", func(c *gin.Context) {
		stime := time.Now().Format("2006-01-02 15:04:05")
		c.String(200, fmt.Sprintf("现在是北京时间：%v",stime))
	})
    // 运行应用
	app.Run()
}
```
- 第四步，编写配置文件
- 也就是 goboot.yml
```shell script
vi goboot.yml
```
```yaml
goboot:
  application:
    name: go-server
  profiles:
    active: dev
  server:
    port: 8080
    bannerPath: ./banner.txt
    contextPath: /api
    staticResources:
      enable: true
      urlPath: /static
      filePath: ./static
    templateResources:
      enable: true
      filePath: ./templates/**/*.html
    https:
      enable: false
      pemPath: ./https/server.pem
      keyPath: ./https/server.key
    gzip:
      enable: false
      level: DefaultCompression
      excludeExtensions:
        - .mp4
      excludePaths:
        - /api/
      excludePathRegexes:
        - "*download"
    proxy:
      - name: github.com
        path: /github/
        redirect: http://github.com/
    mapping:
      - /api/
    cors:
      enable: true
      allowAllOrigins: true
      allowOrigins:
        - http://localhost/
      allowMethods:
        - GET
        - PUT
        - DELETE
        - POST
        - PATCH
        - OPTIONS
      allowHeaders:
        - token
        - Origin
        - secure
        - Auth
      exposeHeaders:
        - Content-Length
      allowCredentials: true
      maxAgeMinutes: 0
```
- 第五步，下载依赖
```shell script
go get github.com/gin-gonic/gin
go get github.com/gin-contrib/gzip
go get github.com/gin-contrib/cors
go get github.com/go-yaml/yaml
```
- 第六步，启动运行
```shell script
go run main.go
```
- 第七步，浏览器访问查看
```shell script
http://localhost:8080/
http://localhost:8080/hello
```
- 这是最终的文件结构
```shell script
hello
|---goboot
    |---goboot.go
|---go.mod
|---goboot.yml
|---main.go
```


## 配置文件
- 配置文件，直接在配置文件中
- 加注释给出说明
- 注意，在此处的配置环境中
- 不同于springboot配置
- 这里是区分大小写，严格匹配的
- 这个java同学需要注意
```yaml
# 配置根节点
goboot:
  # 应用配置      
  application:
    # 应用名称
    name: go-server
  # 多环境配置
  profiles:
    # 激活的环境，找不到指定配置就是默认配置文件
    # 查找规则：goboot.yml goboot-${goboot.prfiles.active}.yml
    # 比如这里，就查找goboot-dev.yml
    active: dev
  # 服务配置
  server:
    # 服务的启动端口    
    port: 8080
    # 也可以配置自己的启动banner
    bannerPath: ./banner.txt
    # 可以指定context-path，目前暂未支持
    contextPath: /api
    # 静态资源配置  
    staticResources:
      # 是否启用
      enable: true
      # url中的路径
      urlPath: /static
      # 解析为静态资源的路径
      filePath: ./static
    # 模板文件配置
    templateResources:
      # 是否启用    
      enable: true
      # 模板文件的匹配规则  
      filePath: ./templates/**/*.html
    # HTTPS的配置部分
    https:
      # 是否启用
      enable: false
      # 分别配置HTTPS的pem文件和key文件
      pemPath: ./https/server.pem
      keyPath: ./https/server.key
    # gzip响应压缩配置
    gzip:
      # 是否启用
      enable: false
      # 压缩级别：BestCompression，BestSpeed，DefaultCompression，NoCompression
      level: DefaultCompression
      # 排除的后缀列表
      excludeExtensions:
        - .mp4
      # 排除的路径前缀列表
      excludePaths:
        - /api/
      # 排除的路径匹配正则列表
      excludePathRegexes:
        - "*download"
    # 代理配置
    proxy:
      # 可以配置多个代理配置
      # 代理的名称，可以随意  
      - name: github.com
        # 代理的路径
        path: /github/
        # 目标跳转路径
        redirect: http://github.com/
    # 自动路径映射配置
    mapping:
      # 可以配置多个进行按照匹配规则自动路由  
      - /api/
    # 跨域配置
    cors:
      # 是否启用
      enable: true
      allowAllOrigins: true
      allowOrigins:
        - http://localhost/
      allowMethods:
        - GET
        - PUT
        - DELETE
        - POST
        - PATCH
        - OPTIONS
      allowHeaders:
        - token
        - Origin
        - secure
        - Auth
      exposeHeaders:
        - Content-Length
      allowCredentials: true
      maxAgeMinutes: 0
```

## 接口开发
- 接口开发，可以使用gin框架自己的方式
- 也可以使用配置中的mapping自动映射两种模式
- 两种模式，都是基于封装的goboot

### gin模式接口开发
- gin模式，就是通过应用实例，获取App属性，得到gin.Engine实现
- 得到 boot 对象
```go 
boot := goboot.GetDefaultApplication()
```
- 拿到 gin.Engine 对象
```go
engine := boot.App
```
- 然后，就可以和原始的gin开发一样开发了
```go
engine.GET("/", func(c *gin.Context) {
    stime := time.Now().Format("2006-01-02 15:04:05")
    c.String(200, fmt.Sprintf("现在是北京时间：%v",stime))
})
```


### mapping自动映射模式接口开发
- 此模式，首先，你得知道工作原理
- 工作原理
    - 首先，需要在配置中 goboot.server.mapping 配置上自动映射的路径
    - 例如，举例配置中的 /api/ 这个路径
    - 则，如果请求路径为：http://localhost:8080/api/hello/go
    - 则 /api/hello/go 就是符合mapping配置的一个路径
    - 则，去除 /api/ 这一层之后，得到的路径为 hello/go
    - 在对boot的代码中，将一个带有方法的结构体，添加到 handlers 中
    - 则，表示这些对象的方法，都具备可以自动映射的能力
    - 假设，结构体为 Api , 具有一个 Hello_Go 方法
    - 那么，对于路径 hello/go 就被映射到 Hello_Go 方法上
    - 具体的映射规则如下：
        - URL路径: /gin-web/hello-goboot
        - 将URL路径按照每层路径分隔
        - 得到：gin-web，hello-goboot
        - 对每一级，按照横向分隔
        - 得到：gin,web和hello,goboot
        - 按照每一级内使用大驼峰（Capital）组合
        - 得到：GinWeb，HelloGoboot
        - 对每一级使用下划线组合
        - 得到：GinWeb_HelloGoboot
        - 这个就是这个路径对应要映射的方法名
        - 那么，将会在注册的 handlers 中，查找名称为这样的一个方法来处理请求
        - 下面给出一些映射案例：
        - 一： hello ---> Hello
        - 二： helloWorld --> HelloWorld
        - 三： hello-world --> HelloWorld
        - 四： hello-go/hello-gin --> HelloGo_HelloGin
    - 映射函数的要求：
        - 入参可以有多个
        - 顺序可以任意
        - 也可以无参数
        - 支持的参数如下
            - c *gin.Context
            - boot * goboot.GobootApplication
            - engine * gin.Engine
            - request * http.Request
            - resp * goboot.ApiResp
            - ctxResp * goboot.CtxResp
            - 自定义绑定请求参数的结构体
                - 注意，必须是结构体类型
                - 结构体支持值类型或指针类型
        - 方法案例
            - 一：func (api *Api) Hello()
            - 二：func (api *Api) Hello(c *gin.Context)
            - 三：func (api *Api) Hello(boot *goboot.GobootApplication,c *gin.Context)
            - 四：func (api *Api) Hello(c *gin.Context, post User, boot *goboot.GobootApplication, engine *gin.Engine, request *http.Request)
- 使用代码示例
```go
package main

import (
	"hello/goboot"

	"github.com/gin-gonic/gin"
)

// 定义API处理结构体
type Api struct {
}

// 给结构体绑定函数
// 这里的函数名为Hello
// 在后面会仔细讲解函数名的问题
func (api *Api) Hello(c *gin.Context) {
	c.JSON(200, gin.H{
		"hello": "hello",
	})
}

// 定义主函数
func main() {
	// 获得默认的应用对象
    app := goboot.GetDefaultApplication()
    // 添加mapping处理对象，注意是指针
	app.AddHandlers(&Api{})
    // 运行应用
	app.Run()
}
```
- 因此，如果使用mapping模式开发
- 分三步走
- 第一步，确认配置文件中的mapping有添加
- 第二步，编写一个符合mapping要求的结构体，也就是具有方法
- 第三步，调用boot对象的AddHandles方法，添加处理的所有结构体

## 自动映射函数
- 上面说了mapping模式的自动映射函数
- 只是简单的介绍了映射函数
- 下面就来详细的说明映射函数
- 以及自动注入的入参的作用或设计初衷
- 下面讲解映射函数，绑定的结构体，都以 Api 讲解
- 配置的mapping 为 /api/
```go
type Api struct{
}
```
### 需要手动指定路径
- 缺少像springmvc的注解声明方式
- 则取而代之，使用函数名作为路径匹配规则
- 原始写法
```go
engine.GET("/api/hello",func(c *gin.Context){
  c.JSON(200,gin.H{
    "data":"hello",
  })
})
```
- 使用Goboot之后
- 则可以改写为如下方式
```go
func (api * Api) Hello(c * gin.Context){
  c.JSON(200,gin.H{
    "data":"hello",
  })
}
```
- 按照匹配规则，Hello函数名进行匹配请求路径
- 这样，避免了两个问题
- 直接使用engine对象
- 写明请求路径
- 这两个问题，都极大的增大了耦合性

### 数据响应之后，必须自行return
- 在使用gin进行响应数据时
- 需要明确的指定return
- 即时是abort也必须进行返回
- 否则如果后续有其他响应逻辑
- 则会连带执行其他响应
- 导致两个响应结合再一起
- 问题如下
```go
engine.GET("/api/hello", func(c *gin.Context) {
  c.AbortWithStatusJSON(200, gin.H{
    "data": "error",
  })
  // 如果此处没有return
  // 则下面的正常响应将会继续响应
  // return
  c.JSON(200, gin.H{
    "data": "hello",
  })
})
```
- 这里演示的这种情况实际中是很常见的
- 只不过，实际中对于abort是有条件的
- 但是依旧不能避免return
- 下面，在goboot中
- 封装了两个结构，来解决此问题
- 这都是基于自动映射实现的
- 因为自动映射，不关心返回值，返回值不会被处理
- 方法一，使用goboot.ApiResp结合gin.Context实现直接返回
```go
func (api *Api) Hello(resp *goboot.ApiResp, c *gin.Context) *goboot.ApiResp {
	return resp.GinOk(c, "hello")
}
```
- 这种方式，通过自动注入goboot.ApiResp结合gin.Context实现
- 方式二，和方法一一致，只不过自己实例化goboot.ApiResp指针
```go
func (api *Api) Hello(c *gin.Context) *goboot.ApiResp {
	return (&goboot.ApiResp{}).GinOk(c, "hello")
}
```
- 这种方式，只需要注入gin.Context即可
- 方式三，和方法二类似，只不过通过方法实例化goboot.ApiResp指针
```go
func (api *Api) Hello(c *gin.Context) *goboot.ApiResp {
	return goboot.ApiOk(nil).GinOk(c, "hello")
}
```
- 这种方式，比较起来容易接受
- 方式四，推荐方式，直接使用goboot.CtxResp实现
```go
func (api *Api) Hello(resp *goboot.CtxResp) *goboot.CtxResp {
	return resp.ApiJsonOk("hello")
}
```
- 这种方式，最为简单实用
- 一般业务场景中，这种模式，在加上自动解析请求参数注入
- 就是一般的使用模式
- 如下
```go
func (api *Api) Login(resp *goboot.CtxResp,user * User) *goboot.CtxResp {
	return resp.ApiJsonOk("ok")
}
```

### 测试Demo
- 文件结构
```shell script
hello
|---goboot
    |---goboot.go
|---templates
    |---index
        |---index.html
|---main.go
|---go.mod
|---goboot.yml
```
- 入口程序
- main.go
```go
package main

import (
	"hello/goboot"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
)

type Api struct {
}

func (api *Api) Hello(c *gin.Context) {
	c.JSON(200, gin.H{
		"hello": "hello",
	})
}

func (api *Api) ApiResp(resp *goboot.ApiResp, c *gin.Context) {
	c.JSON(200, resp.Ok(gin.H{
		"hello": "hello",
	}))
}

func (api *Api) GinResp(resp *goboot.ApiResp, c *gin.Context) *goboot.ApiResp {
	return resp.GinOk(c, "hello")
}

func (api *Api) GinApiResp(c *gin.Context) *goboot.ApiResp {
	return goboot.ApiOk(nil).GinOk(c, "hello")
}

func (api *Api) CtxResp(resp *goboot.CtxResp) *goboot.CtxResp {
	return resp.ApiJsonOk("hello")
}

func (api *Api) Login(resp *goboot.CtxResp, user *User) *goboot.CtxResp {
	return resp.ApiJsonOk("ok")
}

type User struct {
	Username string `form:"username"`
}

func (user *User) User_Info(c *gin.Context, post User, boot *goboot.GobootApplication, engine *gin.Engine, request *http.Request) {
	c.JSON(200, gin.H{
		"user": post.Username,
		"boot": boot.Config.ConfigFile,
	})

}

func main() {
	app := goboot.GetDefaultApplication()

	app.AddHandlers(&Api{}).
		AddHandlers(&User{})

	app.App.GET("/", func(c *gin.Context) {
		stime := time.Now().Format("2006-01-02 15:04:05")
		c.HTML(200, "index/index.html", gin.H{
			"now": stime,
		})
		// c.String(200, stime)
	})

	app.Run()
}

```
- 模板文件
- templates/index/index.html
```html
{{ define "index/index.html" }}
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>首页</title>
</head>
<body>
    <h2>现在是北京时间 {{.now}}</h2>
</body>
</html>
{{ end }}
```
- 配置文件
- goboot.yml
```yml
goboot:
  application:
    name: go-server
  profiles:
    active: dev
  server:
    port: 8080
    bannerPath: ./banner.txt
    contextPath: /api
    staticResources:
      enable: true
      urlPath: /static
      filePath: ./static
    templateResources:
      enable: true
      filePath: ./templates/**/*.html
    https:
      enable: false
      pemPath: ./https/server.pem
      keyPath: ./https/server.key
    gzip:
      enable: false
      level: DefaultCompression
      excludeExtensions:
        - .mp4
      excludePaths:
        - /api/
      excludePathRegexes:
        - "*download"
    proxy:
      - name: github.com
        path: /github/
        redirect: http://github.com/
    mapping:
      - /api/
    cors:
      enable: true
      allowAllOrigins: true
      allowOrigins:
        - http://localhost/
      allowMethods:
        - GET
        - PUT
        - DELETE
        - POST
        - PATCH
        - OPTIONS
      allowHeaders:
        - token
        - Origin
        - secure
        - Auth
      exposeHeaders:
        - Content-Length
      allowCredentials: true
      maxAgeMinutes: 0


```