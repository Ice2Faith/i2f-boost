# 复杂Excel导出工具

## 简介
- 常规情况下，EasyExcel已经完全足够满足导出的需求
- 虽然也能够进行复杂Excel的导出
- 但是代码编写却不够简便
- 因此，进行一次封装
- 能够支持更加简便的导出

## 特点
- 基于EasyExcel进行拓展
- 完全兼容EasyExcel的导出模板语法
- 拓展多层级嵌入的Bean或者Map字段渲染
- 拓展单sheet多板块数据导出

## 出发点
- 现在有这样的一个bean结构数据
- 这里以js对象形式表示
```js
data={
    name: 'admin',
    age: 22,
    role: {
        roleKey: 'root',
        name: '超级管理员',
        parent: {
            roleKey: 'sys',
            name: '系统'
        },
        perms:[
             {
                 permKey: 'home',
                 name: '主页'
             }
        ]
    },
    addressList:[
         {
             name: '建军路',
             address: '建军路338号'
         }
    ]
}
```
- 对于这样的一个信息
- 想要直接进行导出为Excel是不方便操作的
- 因为EasyExcel是针对Collection或者Map进行模板渲染的
- 而默认是不支持嵌套bean的情况的
- 这里，data作为渲染的根元素
- 对于其中的name和age都可以使用EasyExcel标准的方式进行单条数据渲染
```bash
{name} {age}
```
- 但是对于role和addressList却不能直接进行模板语法的渲染
- 需要借助FillWrapper进行包装，才能正常渲染
```java
excelWriter.fill(new FillWrapper("role"),data.get("role"),writeSheet);
```
- 但是这样的用法，就相当于把已经组织好的数据，又给展开
- 十分的不方便
- 因此，对这种嵌套进行一层封装
- 实现自动的解析为FillWrapper进行自动调用

## 使用
- 上面出发点中，已经说明了为什么建这个工具类
- 下面来说使用
- 我们希望的使用方式
```bash
{name}
{age}
{role.roleKey}
{role.name}
{role.parent.roleKey}
{role.parent.name}
{role.perms.permKey}
{role.perms.name}
{addressList.name}
{addressList.address}
```
- 经过实际的封装，需要发生一点点变更
- 也就是除开最后一级的.之外，其他的.需要变为其他符号
- 就把他变为$符号吧
- 那实际的使用方式就是这样
```bash
{name}
{age}
{role.roleKey}
{role.name}
{role$parent.roleKey}
{role$parent.name}
{role$perms.permKey}
{role$perms.name}
{addressList.name}
{addressList.address}
```
- 看见了吗，这样好接受多了
- 对于编写导出模板来说
- 也算是能够接受的
- 好了，这就是使用方式
- 非常简单
- 同时，你可以从一下路径查看样例模板和导出结果
```shell script
./test/demo-tpl.xlsx
./test/output.xlsx
```