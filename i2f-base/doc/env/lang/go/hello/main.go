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
