# 文档（word/pdf）导出工具

## 简介
- 按照一定的word模板导出数据
- 其实在线上和线下交互时是比较常见的
- 一般的做法分为两种
- 第一种是直接操作Word，然后将Word转换为目标格式PDF、PNG等
- 第二种，模板就是图片，进行GDI绘图的方式
- 这里使用的是第一种方式

## 特点
- 按照XML格式的word进行模板渲染
- 支持内嵌图片

## 使用
### 模板处理
- 首先，拿到最原始的样例word文档
- 用word打开
- 另存为xml格式
- 在word中将想要渲染的地方
- 替换为velocity语法支持的变量
    - 需要注意
    - word常常会自作聪明的在你的变量中添加一些标记符号
    - 导致看起来是一个符合的语法
    - 但是实际在xml中，被拆开了
    - 渲染时就是不合法的
    - 因此，给出一些避免方法
        - 方法一
            - 切换到英文输入法
            - 按顺序输入字母
            - 不要使用上下左右键
            - 直到这个占位符写完
            - 这个方法，大部分时间是有效的
            - 少部分时间，如果占位符存在驼峰这些大小写的时候，也可能会发生问题
        - 方法二
            - 基于方法一进行改进
            - 同样英文输入法
            - 同样顺序输入
            - 但是不输入语法引导符号${和结束符}
            - 不进行大小写区分，一律小写
            - 比如：xxname,xxage
            - 这里我都以xx开头，方便后续搜索
- 保存模板
- 语法占位符处理
    - 由于word可能会加一些标记符号
    - 需要进行处理
        - 使用文本编辑器打开xml
        - 这时候应该关闭word，避免文件占用
        - 在xml中搜索自己的变量
        - 确认变量时正确的格式
        - 如果出现${}任意符号被分开
        - 直接删除这部分符号即可
        - 不要删除前后标签
        - 否则可能不配对标签
        - 然后在对应的变量上加上语法符号${}
        - 确认完之后，文本编辑器直接保存即可
        - 重新用word打开查看是否正确即可
            - 注意，word不要在进行操作
            - 否则还得记事本对一遍
- 关于图片占位符的处理
    - 图片，直接在word中插入图片即可
    - 但是呢，为了后面处理方便点
    - 插入的图片有几个要求
        - 图片大小越小越好
        - 颜色越纯粹越好
        - 分辨率越低越好
        - 为什么呢？
        - 因为保存为xml格式之后
        - 图片被编码为base64格式
        - 如果是作为占位符的图片
        - 那么会导致base64非常长
        - 不方便替换为占位符
    - 图片嵌入，实际上在xml格式中
    - 图片被编码为base64格式
    - 因此，只要嵌入图片不多
    - 文本编辑器中很容易找到很长一串的base64就是插入的图片
    - 工具中，使用XSTPLIMG%04d的格式对应图片
    - 加入要替换为第一张图片占位符
    - 则这一串base64内容直接替换为 XSTPLIMG0001
    - 0001 就是索引为1的图片用来填充渲染
    - 为什么是这么奇怪的一串，XSTPLIMG1不行吗
    - 不行，base64编码规则，没4字节对应实际数据的3个字节
    - 因此，base64一定是4字节的整数倍数
    - XSTPLIMG0001 正好12位
    - 同时%04d也预留了足够的图片个数
    - 因此，一般替换之后，就是这样的
    - 数据： <pkg:binaryData>XSTPLIMG0001</pkg:binaryData>
- 到这里，模板就准备好了
- 用word打开查看模板
- 可以看到，占位符显示语法格式
- 图片对应的则是一张损坏的图片的小红叉
- 那就是正确的了
- 那模板就是OK的，可以用来进行渲染生成导出文件了

### 导出代码
```java
File dir = new File("D:\\01test\\docuemnt");
File wordFile = new File(dir, "demo-tpl.xml");

File officeMarkFile = new File(dir, "office-mark.png");


File pdfFile = new File(dir, "output.pdf");
File pngFile = new File(dir, "output.png");
File docxFile = new File(dir, "output.docx");

Map<String, Object> params = new HashMap<>();
Map<Integer, File> xsTplImgFileMap = new HashMap<>();

params.put("dept", "人事");
params.put("name", "张三");
params.put("age", "23");
params.put("tel", "18011112222");
params.put("job", "软件测试");
params.put("address", "建军路88号");
params.put("limit", "2");
params.put("begin", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
params.put("end", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
params.put("mgt", "行政");
params.put("recv", "李四");
params.put("publish", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));


xsTplImgFileMap.put(1, officeMarkFile);

DocumentExportUtil.renderXmlWordTemplate(new FileInputStream(wordFile), "UTF-8",
        params, xsTplImgFileMap,
        SaveFormat.PDF, new FileOutputStream(pdfFile));

DocumentExportUtil.renderXmlWordTemplate(new FileInputStream(wordFile), "UTF-8",
        params, xsTplImgFileMap,
        SaveFormat.PNG, new FileOutputStream(pngFile));

DocumentExportUtil.renderXmlWordTemplate(new FileInputStream(wordFile), "UTF-8",
        params, xsTplImgFileMap,
        SaveFormat.DOCX, new FileOutputStream(docxFile));
```

- 非常简单
- 同时，你可以从一下路径查看样例模板和导出结果
```shell script
./test/demo-tpl.xml
./test/output.docx
./test/output.pdf
./test/output.png
```