# itl-crypt

## 简介
- 全称：i2f-tool-crypt
- 这个工具是专门为了简单的控制台加解密而生的
- 其中对JCE中的部分进行了控制台的包装
- 通过简单的控制台命令，得到JCE的结果
- 同时也对java开发中常用的spring-security中的PasswordEncoder进行了包装
- 使得能够容易的从控制台生成一个符合spring-security规则的密码
- 让直接修改数据库密码成为一个便捷的方式

## 用途
- 计算一些简单的散列值，MD5/SHA-1/SHA-256/HmacMD5/...
- 进行一些编解码，Base64/URLEncode/...
- 直接生成spring-security环境中的密码，替换数据库密码

## 使用
- 查看帮助
```shell script
java -jar itl-crypt.jar

java -jar itl-crypt.jar help
```
- 帮助信息
    - 具体帮助信息以输出为准，此处可能更新不同步
```
itl-crypt
提供命令行上进行部分的加解密或者编解码操作

命令行参数：
[选项] [参数]

[选项]
help 查看帮助
- 以下部分为基础部分
base64-en Base64.getEncoder()
base64-de Base64.getDecoder()
base64-url-en Base64.getUrlEncoder()
base64-url-de Base64.getUrlDecoder()
url-en URLEncoder
url-de URLDecoder
md2 MessageDigest.getInstance("MD2")
md5 MessageDigest.getInstance("MD5")
sha-1 MessageDigest.getInstance("SHA-1")
sha-224 MessageDigest.getInstance("SHA-224")
sha-256 MessageDigest.getInstance("SHA-256")
sha-384 MessageDigest.getInstance("SHA-384")
sha-512 MessageDigest.getInstance("SHA-512")
hmac-md2 Mac.getInstance("HmacMD2")
hmac-md5 Mac.getInstance("HmacMD5")
hmac-sha-1 Mac.getInstance("HmacSHA1")
hmac-sha-224 Mac.getInstance("HmacSHA224")
hmac-sha-256 Mac.getInstance("HmacSHA256")
hmac-sha-384 Mac.getInstance("HmacSHA384")
hmac-sha-512 Mac.getInstance("HmacSHA512")

- 以下部分为spring-security中PasswordEncoder的实例
- 均带有pe前缀
pe-argon2 Argon2PasswordEncoder()
pe-bcrypt BCryptPasswordEncoder()
pe-ldap-sha LdapShaPasswordEncoder()
pe-md2 MessageDigestPasswordEncoder("MD2")
pe-md4 Md4PasswordEncoder()
pe-md5 MessageDigestPasswordEncoder("MD5")
pe-no-op NoOpPasswordEncoder.getInstance()
pe-pbkdf2 Pbkdf2PasswordEncoder()
pe-pbkdf2-sec Pbkdf2PasswordEncoder(args[0])
pe-scrypt SCryptPasswordEncoder()
pe-sha-1 MessageDigestPasswordEncoder("SHA-1")
pe-sha-224 MessageDigestPasswordEncoder("SHA-224")
pe-sha-256 MessageDigestPasswordEncoder("SHA-256")
pe-sha-384 MessageDigestPasswordEncoder("SHA-384")
pe-sha-512 MessageDigestPasswordEncoder("SHA-512")

```
- 命令规范
```shell script
[选项] [选项参数]
[选项]不区分大小写
[选项]之后的每一个[参数]都会进行一次选项运算
某些[选项]需要{附加参数}的情况下，除去选项需要的{附加参数}，其余[参数]，同上，也都会进行一次运算
```
- 举例
- 无附加参数情况
```shell script
md5 hello world
```
- 则将会计算hello和word两个串分别的md5值
- 输出为
```shell script
hello ==> 5D41402ABC4B2A76B9719D911017C592
world ==> 7D793037A0760186574B0282F2F435E7
```
- 有附加参数情况
```shell script
hmac-sha-256 hello world
```
- 这里hmac需要一个key附加参数
- 这个附加参数就是hello
- 因此，输出一条world的hmac值
- 输出为
```shell script
world==> (key=hello) ==> F1AC9702EB5FAF23CA291A4DC46DEDDEEE2A78CCDAF0A412BED7714CFFFB1CC4
```

