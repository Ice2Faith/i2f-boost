# 一个基于JDBC+JSON实现的数据库表备份与还原工具
---
## 核心交互
- 核心交互使用JSON配置方式实现
- 下面给出一个典型的配置来说明
- 区别于真实的JSON文件，允许使用#开头的行作为注释行
- 一旦#开头，就认为是注释行 
```js
{
  # 必须的数据源配置，无论是备份，还是还原，都需要配置数据源
  # 这里只提供JDBC的数据源支持
  # 分别指定JDBC的配置，以及响应的驱动，因此，你需要将驱动文件放到本程序环境变量中
  "datasource": {
    "url": "jdbc:mysql://127.0.0.1:3306/test_db?useAffectedRows=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
    "username": "root",
    "password": "123456",
    "driver": "com.mysql.cj.jdbc.Driver"
  },
  # 备份的配置，主要就是从目标表中使用分页的方式读取数据
  "select": {
    # 指定你的查询SQL，其中分页参数将会被替代，分别是以下四个字符串，这四个对于大多数的数据库都已经覆盖
    # ${page.index} 分页的页码，从0开始
    # ${page.size} 分页的大小，例如每页30条
    # ${page.offset} 数据库从取数据的开始行索引，从0开始 = index*size
    # ${page.end} 数据库取数据的结束行索引 = (index+1)*size
    "sql": "select * from sys_user a order by id limit ${page.offset},${page.size}",
    # 指定是否适用分页，如果使用分页，则按照指定的页大小，进行分页参数查询
    # 不指定则直接使用SQL查询
    "pageable": true,
    # 分页大小，不指定则默认为20000，因为本程序的目的是备份和恢复，每页大小应该大一点比较合适
    "pageSize": 20000,
    # 输出配置
    "output": {
      # 是否启用压缩功能
      # 导出的数据默认是以文本JSON的方式进行保存的，如果不使用压缩，文件内容可能比较大
      # 启用压缩之后，文本将会打包成为一个zip文件，从而降低空间占用，默认不开启
      "compress": true,
      # 指定输出类型，此类型名称将会作为类名的一部分
      # JdbcBackupExecutor类同级的resolvers包下面，找File+OutputResolver的类进行执行输出操作
      # 这里是File,假设JdbcBackupExecutor的包名为com.test,则对应的输出类为：com.test.resolvers.FileOutputResolver
      # 这里通过反射实现，因此这个类必须具有无参构造方法
      "type": "File",
      # 为了兼顾对应type的实现类在其他包下面的情况，提供了包名数组，如果默认的包找不到，则会从下面这些包名中取查找
      # 例如，附加包名为：com.demo,则会查找com.demo.FileOutputResolver
      "packages": [],
      # 配置给到这个对应处理器的参数，例如默认的File处理器，需要的参数，都可以在其中随意指定，实际对应一个Map<String,Object>
      # 因此这里的配置是完全随意的，根据对应的Resolver给定参数即可
      "meta": {
        # File处理器输出到哪个路径
        "path": "./backup",
        # 文件名的前缀时什么，建议就是表名
        # 完整文件名规则：prefix+yyyyMMdd-HHmmss+数据开始索引+数据结束索引.x.json
        # 当使用压缩时，在上诉文件名后追加.zip后缀
        "prefix": "sys_user",
        # 设置需要删除多少天之前的备份数据
        "expireDays": 15
      }
    }
  },
  # 恢复的配置，其实也就是批量插入数据的配置
  "insert": {
    # 指定要插入的表，一般来说，备份的时候的表名就是此处的表名，
    # 这里允许你带上模式名，例如：web.sys_user
    "table": "sys_user",
    # 如果需要列名重新映射，则可以在此处指定，如果列名不需要重新映射，此处可以为空或无元素
    # 当为空或者无元素时，将按照要插入的表的全部列进行映射，假设这里是另一张表sys_role
    # 则将按照sys_role的全部列，作为插入列，因此，没有的字段为null
    "mapping": [
        {
            # 插入的列名，也就是要插入的表的列名
            "column":"user_id",
            # 数据的字段名，也就是备份时输出的列名，此值可以为空，则认为column==property
            # 二者名称一致进行映射
            "property":"userId"
        },
        {
            "column":"user_name"
        },
        {
            "column":"role_id",
            "property":"user_role_id"
        }
    ],
    # 插入前准备工作相关配置
    "prepare": {
      # 是否需要清空目标表
      "truncate": true,
      # 是否需要备份目标表，再执行清空操作
      "backup": true,
      # 备份的目标表，添加什么后缀
      "backupSuffix": "_bak"
    },
    # 是否适用insert into ...select ... from dual union all select ...形式批量插入
    # 这是提供了对于所有数据都适用的兼容批量插入模式
    # 对于MySQL这种，支持insert into ... values (...),(...)的这种，则无需开启此项，默认关闭
    "unionMode": false,
    # 批量插入的条数，默认2000
    "batchCount": 2000,
    # 数据的输入来源
    "input": {
      "compress": true,
      # 默认也是以文件作为输入，进行恢复
      # 同输出时，一样，后缀为InputResolver的处理类
      "type": "File",
      "packages": [],
      "meta": {
        # 需要指定数据的来源路径
        "path": "./backup",
        # 匹配的文件名，也就是按照此进行匹配指定路径下的文件，因为输出的时候，按照分页进行文件分隔
        # 因此，你可能需要同时恢复多个文件，因此，你可以添加这些文件名在下面
        # 也可以添加这些文件名的一些通配符
        # 任何一个匹配下面的文件，都会作为输入
        # 需要注意的点，使用通配符匹配时，文件匹配的情况下，匹配表名，应该加上分隔符+
        # 例如：存在两个文件：sys_user+2022... 和 sys_user_role+2022...
        # 这种情况下，如果使用sys_user*进行匹配，则两个都会匹配到，显然第二个是不想要的
        # 此时就应该使用sys_user+*进行匹配，精确匹配到sys_user的数据，而不是sys_user_role也会被匹配到
        "pattens": [
          "sys_user+*.x.json*"
        ]
      }
    }
  }

}

```
