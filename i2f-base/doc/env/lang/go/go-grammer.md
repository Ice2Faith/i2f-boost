# go 语法简介

## 语法总结
- 在编译器的层面，保证代码的整洁
- 用编译器的方式避免部分代码异味
- 也就是在语法层面，加上了格式等可能出现异味的要求

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

## 快速入门
## 基本介绍
- 变量声明，关键字：var
- 变量声明并复制，操作符：:=
- 函数声明，关键字：func
- 函数支持多个返回值
- 可以给类型绑定函数，感觉和定义类函数一样
- 类型声明，关键字：type
- 数组或内建复杂类型，大部分都是使用中括号访问
    - 数组、切片：arr[0],arr[1]
    - 映射：map["a"],map[1]
- 变长参数，使用 ... 在类型之前表示，例如： strs ... string,nums ...int,args ... interface{}
- 数组或切片展开，使用 ... 在类型之后表示，例如：arr=append(arr,args ...)
- 使用单下划线表示匿名变量，不占用变量空间和变量名，用来接受不想处理的返回值：_,ok:=mp[1]
- 变量定义了，必须使用，否则编译不通过
- 操作符前后必须有一个空格，否则编译不通过
- 作用域开始符号（左花括号{）必须是行尾符号，并且必须和前置语句在同一行
- if / for 等条件部分，不需要圆括号

## 数据类型
- 在go中，有这严格的数据类型
- 虽然定义变量时，支持类型自动推导
- 导致在编码时感觉没有类型
- 但是，实际上有这严格的类型机制
- 没有所谓的类型自动提升
- 必须显式的进行类型转换
- 内建类型
    - nil 空值，其他语言中的null
    - true/false 逻辑值，和其他语言中bool一致
    - any/interface{} 接口类型，和其他语言中的Object类型一致
- 基础数据类型
    - 整形，默认值 0
        - int 根据机器位数，实际上为 int32 或 int64
        - int32 其他语言中的int
        - int64 其他语言中的long
    - 浮点型，默认值 0.0
        - float 根据机器位数，实际上为 float32 或 float64
        - float32 其他语言中的float
        - float64 其他语言中的double
    - 逻辑型，默认值 false
        - bool 布尔
    - 字符型，默认值 "",注意，其他语言中可能是null，但是go中，string不可为空
        - string 字符串
- 复合数据类型
    - [3]string/[3]int 数组，是带有长度的类型
        - 定义：[长度]类型
        - 举例：[3]string,[2]int,[5]float,[8]interface{}
        - 声明：var arr [3]int
        - 访问：通过下标访问：var a=arr[0] arr[1]=5 arr[2]
        - 长度：使用内建函数len获取：len(arr)
    - []string/[]int 切片，是不定长的数组，可以对等Java的ArrayList
        - 使用上和数组完全一致
        - 增加元素：使用内建函数append实现：arr=append(arr,elem)
        - 删除元素：没有提供相关操作函数，通过切片重组合实现
            - 例如，删除小标为2的元素：arr=append(arr[:2],arr[3]...)
    - map[string]int/map[int]int 映射，是基于Hash的映射表，对等于HashMap
        - 定义：map[键类型]值类型
        - 举例：map[string]string,map[string]int
        - 声明：var mp map[string]string
        - 分配空间：通过声明的map，需要进行分配空间，使用内建函数make：mp=make(map[string]string)
        - 访问：通过中括号访问：mp["hello"]
        - 是否存在：通过中括号访问，第二个返回值表示是否存在：_,ok:=mp["hello"]
    - chan 通道，用于多线程通信的结构，对等于BlockingQueue
- 类型转换
    - 前面说了，GO没有隐式类型转换
    - 所有的类型转换都必须是显式的
    - 类型转换，通过：T()形式转换，T为目标类型
    - 例如：int32(num),float64(num),string(bytes),byte(str),User(elem)
    - 或者通过类型断言，实现类型转换
    - 类型断言，格式：newValue,isOk := oldVal.(T)
        - 其中oldVal就是需要转换的数据，T就是目标类型，isOK是布尔类型，表示是否成功，newValue就是转换之后的值
        - 因此，如果isOK为true的话，就能够转换
        - 如果，能确定一定能够转换，则可以使用匿名变量替换isOK，直接得到转换后的值
        - 格式：newValue,_ := oldVal.(T)
        - 例如：user,_ := obj.(User)

## 变量与常量
- 变量，通过var关键字声明
- 常量，通过const关键字声明，常量声明时必须有初值
    - 其他定义上的使用和变量一致
    - 常量不可再赋值
    - 其他使用上和变量一致
- 声明变量
```shell script
var 变量名 变量类型 = 变量初值
```
```go
var a int = 12
var b int
```
- 给初值的情况下，可以类型推断
```shell script
var 变量名 = 变量初值
```
```go
var a = 12
var b = "b"
```
- 可以同时声明一批变量
```shell script
var (
变量名 变量类型 = 变量初值
变量名 变量类型 = 变量初值
)
```
```go
var (
a int = 12
b int
c string
)
```
- 可以使用，定义并赋值操作符 := 直接定义，省去 var 关键字
```shell script
变量名 := 变量初值
```
```go
a := 12
b := "b"
```
- 常量的定义和变量一致，区别于，不可省略const关键字，必须有初值
- 也就是说，如下的定义都是错误的
```go
const a int // 没有初值
a:=12 // 实际上定义了一个变量
```
- 以下常量的定义都是可以的
```go
const pi = 3.14
const one int = 1
const (
 OK = 200
 Err = 500
)
```
- 变量，一旦声明，类型就固定了
- 也就是说，不能将不同类型的值进行赋值操作

## 标准输出
- 学习，总要能够打印出内容
- 才能知道实际的运行结果
- 因此，这里先介绍标准输出
- 标准输入输出，在fmt包中
```go
import "fmt"
```
- 输出一行
```shell script
fmt.Println(变长参数)
```
```go
fmt.Println(1)
fmt.Println(1,2,true,"aaa")
```
- 格式化输出
    - 格式占位符
        - %v : 直接输出值
        - %T : 输出类型
        - %d/%f/%.2f: 这些和C语言一致
    - 转义符号
        - \r\n\t 都是常见的转义符号
```shell script
fmt.Printf(格式字符串,格式化变长参数)
```
```shell script
fmt.Printf("1")
fmt.Printf("%v, %T\n",1,1)
```
- 格式化为字符串返回
    - 在某些情况下，需要得到一个格式化之后的字符串
```shell script
接受变量 = fmt.Sprintf(格式字符串,格式化变长参数)
```
```shell script
str = fmt.Sprintf("1")
str = fmt.Sprintf("%v, %T\n",1,1)
```


## 函数
- 通过func声明函数
- 函数定义
- 前面说了，GO是支持多返回值的
```shell script
func 函数名(参数列表) (返回值列表){
  return 返回值
}
```
- 可以无返回值
```go
func one(){
}
func two(num int){
}
func three(num int,str string){
}
```
- 单个返回值
```go
func one() string {
    return "ok"
}
func two(num int) int {
    return 2006
}
func three(num int,str string) bool {
    return true
}
```
- 多个返回值
```go
func one() (string,int) {
    return "ok",200
}
func two(num int) (int,float32) {
    return 2006,1.2
}
func three(num int,str string) (bool,string) {
    return true,""
}
```
- 返回值可以预定义返回值名称
- 这种情况下，return语句可以省略具体的值
```go
func one() (str string,num int) {
    str = "ok"
    num = 24
    return
}
```
- 参数列表或返回值列表，连续相邻参数类型相同，可以省略
```go
func one(a,b,c int,d,e string,f bool) (num1,num2 int) {
    num1 = 0
    num2 = 1
    return
}
```
- 接受函数的返回值
- 单个返回值时，一般可以先定义变量
```go
var num int
num = one(1,2)
```
- 多个返回值时，一般定义并赋值
```go
num,ok := check()
```
- 可以使用匿名变量，忽略多个返回值中的某些值
```go
num,_ := check()
_,ok := check()
```

## 流程控制
- 在GO中，尽管流程体只有一句话，作用域符号{}也是不可省略的
### if 语句
- 在go中，流程控制语句的条件部分，都是不需要括号的
- 结构
```shell script
if 条件表达式 {
  条件体
}
```
```go
if err != nil {
    fmt.Println(err)
}
```
- 可以在条件部分嵌入语句
```go
if _,ok := isOk(); ok {
    fmt.Println("is ok")
}
```
- 换句话说，就是可以在条件位置，声明一个局部变量，仅在if条件内使用

### for 语句
- GO 中，for成分复杂，因为没有while语句
- 而是for的一种形式，取代了while语句
- 结构
```shell script
for 初始语句;条件语句;增量语句 {
  循环体
}
```
```go
for i := 0;i<10;i++{
    fmt.Println(i)
}
```
- 可以只有条件语句，那就等价于while语句
```go
i := 0
for i<10 {
    fmt.Println(i)
    i++
}
```
- 可以条件也没有，那就是死循环模式
```go
i := 0
for {
    if i >= 10 {
        break
    }
    fmt.Println(i)
    i++
}
```
- 其中，对于break和continue的使用，和其他语言一致

### switch 语句

## 数组与切片

## 映射

## 类型定义


## 结构体
