# go 语法简介

## 基础横向对比
- 语句之后，不强制也不建议加分号
```go
var name = "marks"
```
- 具有语法格式上的要求
- 比如，作用域括号符号位置
- 运算符前后需要有空格等
```
var age = 12
```
- 对未使用变量的编译器要求
- 未使用的变量将会在编译时报错
```
func main(){
    var name = "marks"
}
```
- 作用域开始符号，必须是行最后一个字符
- 最常见的就是花括号{
```go
func main(){

}
```
- 不过，上面说到的，针对语法格式的要求中
- 可以直接在对应的IDE环境中，添加插件进行格式化得到解决
- 也可以使用go提供的命令行格式化工具
```shell script
go fmt hello.go
```
- 面向过程开发
- 也可以通过一定手段使得面向对象开发
- 声明变量使用var关键字
- 变量类型在变量名之后
- 支持自动类型推导
```go
var 变量名 变量类型 = 变量值

var name string
var name string = "marks"
```
- 类型推导情况下，可以去掉变量类型
```go
var 变量名 = 变量值

var name = "marks"
```
- 使用立即定义，可以省略var关键字
```go
变量名 := 变量值

name := "marks"
```
- 可以使用解包定义多个变量
```go
var (
    变量名1 变量类型1 = 变量值1
    变量名2 变量类型2 = 变量值2
)

var (
    name string
    age int = 14
    grade int
)
```
- 使用nil表示空
```go
nil
```
- 函数允许返回多个值
```go
func getData()(ret int,err erros){
    return 1,nil
}
```
- 可以使用下划线_定义匿名变量
- 用来忽略一些不想处理的值
- 比如，用来忽略函数返回的异常
```go
ret,_ := getData()
```
- 使用const关键字定义常量
- 常量必须有定义值
```go
const 变量名 变量类型 = 变量值

const pi = 3.1415926
```
- 主文件必须包是main，具有main函数
```go
package main

func main(){

}
```

## 常用的包
- fmt 标准输入输出
- math 数学运算
- net 网络
- net/http HTTP
- io IO处理
- io/fs 文件系统
- io/ioutil IO工具
- os 操作系统
- os/exec 命令执行
- flag 命令行参数解析
- errors 异常
- strconv 字符串与基本类型转换
- strings 字符串操作