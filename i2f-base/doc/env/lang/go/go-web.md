# Golang 实现 WEB 开发
- Golang的web开发
- 常用的有gin框架和iris框架
- 这里以使用gin框架
- 官网地址
```shell script
https://gin-gonic.com/
```

## 准备工作
- 使用go-mod创建一个项目
- 项目名假如为hello
- 则文件结构如下
```shell script
hello
|---go.mod
|---main.go
```
- 编写第一个服务
- 搜索gin依赖
- 找到如下
```shell script
gin (github.com/gin-gonic/gin)
```
- 下载依赖
```shell script
go get github.com/gin-gonic/gin
```
- 修改 main.go
```go
package main

import (
	"github.com/gin-gonic/gin"
)

func main() {
	r := gin.Default()

	r.GET("/", func(c *gin.Context) {
		c.String(200, "hello gin.")
	})

	r.Run()
}
```
- 编译运行
```shell script
go run main.go
```
- 在浏览器查看
```shell script
http://localhost:8080/
```
- 浏览器中看到如下
```shell script
hello gin
```

## 快速入门
- 遇事不决，上官网示例
```shell script
https://gin-gonic.com/zh-cn/docs/examples/
```
### 基础部分
- 实例化引擎
- 也被称作路由
```go
engine := gin.Default()
```
- 这是最常用的方式
- 接着，就可以启动这个引擎
- 也就是运行
```go
engine.Run()
```
- 完整代码
```go
package main

import "github.com/gin-gonic/gin"

func main() {
	engine := gin.Default()

	engine.Run()
}
```
### 路由部分
- 路由，也就是处理URL请求
- 给请求分配处理函数
- 原生上支持RESTful风格接口
- 一个路由的基本形式是这样的
- 是基于 gin.Engine 定义的，也就是下面的 engine 结构
```go
engine.GET("/user/name",func (c * gin.Context)  {
	c.JSON(200,gin.H{
		"data":"admin"
	})
})
```
- 第一个参数，是URL的Path部分，也称作路径
- 第二个参数，是gin.HandlerFunc类型的函数，原型就是：func (c * gin.Context)
- 有时候，也分开写
```go
func getUserInfo(c *gin.Context){
	c.JSON(200,gin.H{
		"name":"user",
	})
}

engine.GET("user/info", getUserInfo)
```
- 类似的函数，大概有：GET，PUT，DELETE，POST，Any
- 完整代码如下
```go
package main

import "github.com/gin-gonic/gin"

func getUserInfo(c *gin.Context) {
	c.JSON(200, gin.H{
		"name": "user",
	})
}

func main() {
	engine := gin.Default()

	engine.GET("/user/name", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"data": "admin",
		})
	})

	engine.GET("user/info", getUserInfo)

	engine.POST("/user/add", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"msg": "ok",
		})
	})

	engine.PUT("/user/edit", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"msg": "ok",
		})
	})

	engine.DELETE("/user/delete", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"msg": "ok",
		})
	})

	engine.Any("/user/list", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"data": []string{"root", "admin"},
		})
	})
	
	engine.Run()
}

```

## 参数绑定
- 将请求参数绑定到结构上
- 是开发中常用的手段
- 因为结构在开发中更方便使用
- 在gin中，是基于 gin.Context 结构使用的
- 绑定参数使用 c 的Bind系列函数实现
- 首先，需要定义结构
```go
type User struct {
	Username string `form:"username"`
	Age      int    `form:"age"`
}
```
- 注意，因为绑定参数，gin底层使用的是反射实现的
- 因此，结构和结构的字段都需要是共有的
- 反射才能够获取得到
- 但是，如果直接定义结构，结构一旦需要公有
- 在Go中，首字母大写被视为公有，首字母小写被视为私有
- 因此，反射，就必须首字母大写
- 但是一般表单或者JSON数据，都是使用小驼峰的
- 也就是说，首字母是小写的
- 这样，就需要借助GO的TAG标签来实现
- 也就是上面，结构定义最后的字符串
- 其中form这个TAG就对应了gin框架实现参数绑定所查找的TAG
- form:"username"就可以理解为，让gin按照username这个形式的参数进行绑定
- 这样就解决了驼峰和Go访问性的问题
- 下面，来绑定参数
```go
engine.POST("/user/add", func(c *gin.Context) {
	var user User
	c.ShouldBind(&user)
	c.JSON(200, gin.H{
		"data": user.Username,
	})
})
```
- 这里使用，ShouldBind进行参数绑定
- 接受一个参数，需要为指针类型
- 由于声明的是值类型，因此取地址
- 当然，也可以直接声明初始化一个指针类型的
- 如下
```go
engine.POST("/user/add", func(c *gin.Context) {
	user := &User{}
	c.ShouldBind(user)
	c.JSON(200, gin.H{
		"data": user.Username,
	})
})
```
- 为什么需要为指针类型
- 因为，在Go中，结构体传参，是值传递
- 也就是说，如果不传地址，则实参和形参操作的数据就不是一个
- 就会出现，函数内修改了结构的值，函数外不受影响
- 重新回来
- ShouldBind函数，会自动根据请求的Method和ContentType自动解析请求参数
- 因此，大部分的场景都是可以使用的
- 如果，明确的知道需要绑定参数的来源和格式
- 则可以使用，明确的函数：ShouldBindJSON,ShouldBindQuery,ShouldBindXML等函数
- 完整代码，如下
```go
package main

import "github.com/gin-gonic/gin"

type User struct {
	Username string `form:"username"`
	Age      int    `form:"age"`
}

func main() {
	engine := gin.Default()

	engine.POST("/user/add", func(c *gin.Context) {
		// var user User
		// c.ShouldBind(&user)
		user := &User{}
		c.ShouldBind(user)
		c.JSON(200, gin.H{
			"data": user.Username,
		})
	})

	engine.Run()
}

```

## 响应数据
- 响应数据，在当下，最流行的就是JSON格式
- 但是gin还是保留了拓展
- 主要分为三种：JSON，String，Html
- 响应数据，还是基于 gin.Context 结构
### 响应JSON数据
```go
engine.POST("/user/add", func(c *gin.Context) {
	c.JSON(200, gin.H{
		"data": 12,
	})
})
```
- 通过JSON方法，直接返回JSON数据即可
- 第一个参数，表示HTTP状态码，可以使用net/http包下面定义的Status系列常量表示
- 第二个参数，是一个任意数据类型
- 常见的有以下几种使用示例
- 其中，使用结构体是，需要借助json这个TAG转换为小驼峰
- 和上面说得绑定参数使用form这个TAG的原因一致
- go进行转换为JSON时，也是反射
- JSON响应完整示例
```go
package main

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

type User struct {
	Username string `json:"username"`
	Age      int    `json:"age"`
}

func main() {
	engine := gin.Default()

	engine.POST("/user/info", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"data": 12,
		})
	})

	engine.POST("/user/info", func(c *gin.Context) {
		c.JSON(200, map[string]interface{}{
			"data": 12,
		})
	})

	engine.POST("/user/info", func(c *gin.Context) {
		c.JSON(200, User{
			Username: "admin",
			Age: 22,
		})
	})

	engine.Run()
}
```
### 响应String数据
- 也就是直接响应TEXT文本
- 这种目前的使用比较少
- 部分还是有使用到
```go
engine.GET("/", func(c *gin.Context) {
	c.String(200,"hello gin")
})

engine.GET("/", func(c *gin.Context) {
	c.String(200,"hello %v","gin")
})
```
- String方法，也是接受两个参数
- 第一个参数，还是HTTP状态码
- 第二个参数，是字符串或格式化模板字符串
- 第三个参数，是变长参数，用于给参数二作为格式化参数

### 响应HTML渲染页面
- 这里，响应的HTML页面
- 不是存粹的WEB页面
- 而是后端渲染的模板页面
- 在Java中，等价于JSP
- 在springMVC中，等价于View
- 这个示例，先给完整代码
```go
package main

import (
	"github.com/gin-gonic/gin"
)


func main() {
	engine := gin.Default()

	engine.LoadHTMLGlob("./templates/*.html")

	engine.GET("/", func(c *gin.Context) {
		c.HTML(200, "index.html", gin.H{
			"title": "首页",
		})
	})

	engine.Run()
}

```
- 使用HTML方法，响应模板
- 方法有三个参数
- 第一个参数，HTTP状态码
- 第二个参数，模板文件ID，注意，我没有说是文件名称
- 第三个参数，模板渲染的绑定参数，是任意类型
- 要使用模板，必须调用 LoadHTMLGlob 方法，指定模板的路径
- 上面的实例中，匹配了templates目录下的*.html作为模板
- 并且，LoadHTMLGlob 必须在路由方法之前调用，也就是在 GET POST 等方法之前调用
- 准备模板文件
```shell script
templates/index.html
```
```html
{{ define "index.html" }}
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>首页</title>
</head>
<body>
    <h2>这是后端标题 {{.title}}</h2>
</body>
</html>
{{ end }}
```
- 在模板中，使用 {{ }} 来表示服务端标记
- 使用 {{ define 模板ID }} 来指定模板ID
- 只是，一般情况下，模板ID就写为路径文件名
- 因此，可能会带来一定的混淆
- 但是，记住，使用HTML方法，使用的是模板ID，不是模板文件路径
- 在模板中，使用 {{.title}}来取的了HTML函数的渲染参数中的title字段


