# linux 发送邮件

---
- 安装
```shell script
yum install mailx -y
```
- 配置发送账号信息
```shell script
#未加密的发送方式通过25端口,会被公有云封掉.
cat >>/etc/mail.rc <<EOF
set from=xxx@163.com
set smtp=smtp.163.com
set smtp-auth-user=xxx@163.com
set smtp-auth-password=xxx123
set smtp-auth=login
EOF

#加密的方式465端口
cat >>/etc/mail.rc <<EOF
set nss-config-dir=/etc/pki/nssdb/          #加密方式配置
set smtp-user-starttls                      #加密方式配置
set ssl-verify=ignore                       #加密方式配置
set from=mufengxiaoyue@163.com                #配置发件人
set smtp=smtps://smtp.163.com:465            #配置使用163邮箱发送邮件，不加密方式参考上面
set smtp-auth-user=mufengxiaoyue@163.com      #邮箱名
set smtp-auth-password=HMACZIJFDUKRWPSX             #授权码
set smtp-auth=login                         #认证形式
EOF
```
- 发送
```shell script
echo "内容" | mail -s [标题] [接受账号]
```
- 或者
```shell script
mail -s [标题] [接受账号] < [正文文件路径]
```