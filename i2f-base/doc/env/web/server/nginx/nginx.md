## 安装 nginx
- 安装依赖环境
```shell script
yum install gcc-c++
yum install -y pcre pcre-devel
yum install -y zlib zlib-devel
yum install -y openssl openssl-devel
```
- 下载nginx
```shell script
echo http://nginx.org/en/download.html
wget -c https://nginx.org/download/nginx-1.22.1.tar.gz
```
- 解包
```shell script
tar -zxvf nginx-1.22.1.tar.gz
```
- 检查依赖
    - 没有ERROR则可以进行下一步
```shell script
cd nginx-1.22.1
./configure --prefix=/usr/local/nginx --with-http_stub_status_module --with-http_ssl_module
```
- 编译并安装
```shell script
make && make install
```
- 查看nginx路径
    - 其实上面配置的时候，已经指定路径了
    - /usr/local/nginx
```shell script
whereis nginx
```
- 进入nginx路径
```shell script
cd /usr/local/nginx
```
- 编写启动脚本
```shell script
vi start.sh
```
```shell script
./sbin/nginx -c ./conf/nginx.conf
```
- 编写停止脚本
```shell script
vi stop.sh
```
```shell script
./sbin/nginx -s stop
```
- 编写刷新配置脚本
```shell script
vi reload.sh
```
```shell script
./sbin/nginx -s reload
```
- 编写验证配置脚本
```shell script
vi verify.sh
```
```shell script
./sbin/nginx -t
```
- 给脚本添加执行权限
```shell script
chmod a+x *.sh
```
- 更改nginx配置
```shell script
vi ./conf/nginx.conf
```
```shell script
# set work user
user  root;
# set woker count as cpu core count * 2
worker_processes  4;

# open error log
error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

# open pid store
pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    # open log format
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';
   
    # open access log
    access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    # modify timeout
    keepalive_timeout  300;

    # open gzip support
    gzip on;
    gzip_min_length 2k;
    gzip_comp_level 9;
    gzip_types text/html text/xml text/plain text/css text/javascript application/javascript application/x-javascript application/xml image/jpeg image/gif image/png image/svg+xml;
    gzip_vary on;
    gzip_disable "(MSIE [1-6]\.)|(gozilla)|(traviata)";
    
    # set 8080 as an static  web server
    server {
        listen 8080;
        server_name localhost;

        charset utf-8;

        location / {
           autoindex on;
           root /root/apps/demo/dist;
           index index.html index.htm =404;
           try_files   $uri $uri/ /index.html;
        }

    }

    # ---------------------------------------------------------
    # after this line could be delete
    
    # default 80 config
    server {
        listen       80;
        server_name  localhost;

        charset utf-8;

        access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}
      # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

    # ---------------------------------------------------------
    # before this line cloud be delete

}

```
- 启动nginx
    - 这里使用创建的启动脚本
```shell script
./start.sh
```