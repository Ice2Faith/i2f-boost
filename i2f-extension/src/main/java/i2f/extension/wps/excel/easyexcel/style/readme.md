# 基于EasyExcel的注解驱动单元格样式处理器

## 使用方法
- 1.编写一个用于EasyExcel导出的实体类
    - 也就是 @ExcelIgnoreUnannotated 和 @ExcelProperty 两个注解的使用
- 2.在需要自定义样式的字段上，添加样式注解
    - 也就是 @ExcelStyle 或 @ExcelCellStyle 两个注解的使用
- 3.使用EasyExcel进行导出
    - 区别在于，需要添加一个 registerWriteHandler() 处理器，用于实现注解样式的写入
    - 样式处理器的获取方式：AnnotatedCellWriteHandler.getInstance(...)
- 使用区别
    - 与直接使用EasyExcel无差别使用
    - 只是添加了EasyExcel关于单元格写入的开放插件处理器
    - 因此使用上，无区别
- 需要注意的点
    - 在使用EasyExcel的模板导出功能时，字段顺序可能和Excel模板中的顺序不一致
    - 因此，请使用ExcelProperty的index或order保持字段顺序和模板顺序一致
- 简单示例
    - TestExcelDo 实体类中，给定了基本所有的注解的使用方式
    - 请参考定义即可
- 简单示例-直接使用：
```java
OutputStream os;
List<TestExcelDo> list;
EasyExcelExportor.export(os,"导出表",list,TestExcelDo.class);
os.close();
```
- 简单示例-在SpringMVC中导出并下载
```java
HttpServletResponse resp;
resp.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
resp.setHeader("content-disposition", "attachment;fileName=" + "export.xlsx");
List<TestExcelDo> list;
EasyExcelExportor.export(resp.getOutputStream(),"导出表",list,TestExcelDo.class);
```

## 开发说明

### 注解
- @ExcelStyle
    - 改注解可以用于 行样式、列样式、单元格样式 的设置
    - 样式的来源根据指定的方法名进行调用的到样式值 WriteCellStyle 
        - 方法名，支持指向其他类，支持是静态或者非静态的类
        - 当不带类名时，默认从本类中查找方法
        - 带类名时，从指定的类中查找方法
        - 静态方法时，直接调用
        - 非静态方法时，优先从SpringContext中查找对应类的示例
        - 否则，直接newInstance(),这种情况需要保证，目标类具有无参构造
        - 指向一个方法，函数原型见： ExcelStyleCallback.class 中的定义
        - 但是支持匹配前N个参数的重载类型
        - 也就是说，如下参数列表重载也是支持的
        - WriteCellStyle style(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet, Workbook wb);
        - WriteCellStyle style(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet);
        - WriteCellStyle style(ExcelStyleCallbackMeta meta, Cell cell);
        - WriteCellStyle style(ExcelStyleCallbackMeta meta);
        - WriteCellStyle provide();
        - 同时支持返回值为void类型
        - 当返回值为WriteCellStyle时，返回值不为null时，样式将会进行覆盖
        - void style(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet, Workbook wb);
        - void style(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet);
        - void style(ExcelStyleCallbackMeta meta, Cell cell);
        - void style(ExcelStyleCallbackMeta meta);
        - void provide();
        - 因此，一般使用有两种方式，带返回值的，用来直接返回样式设置
        - 不带返回值的，用来自定义原始的样式
    - 同时也可以指向一个实现类 ExcelStyleCallback 
        - 从该实现类获取样式值
- @ExcelCellStyle
    - 虽然 @ExcelStyle 注解提供的能力非常强大，但是带来了复杂的代码编写的麻烦
    - 因此，本注解的出发点，就是提供一些简单的样式，直接在注解中指明，避免代码的编写和使用复杂度
    - 本注解是条件式的，基于单元格的样式，不作用于行和列
    - 条件又spel表达式的结果返回，这需要一个bool值，当bool值为true时，表示应用注解的样式
    - spel表达式的根数据为 ExcelStyleCallbackMeta 
    - 因此，这个实例中的属性，都可以在spel中使用
    
### 实现类
- AnnotatedCellWriteHandler
    - 实现类EasyExcel的 CellWriteHandler 单元格写入拓展接口
    - 样式的写入，就是本类进行实现
- AnnotationCellStyleParser
    - 提供了将类中的注解样式解析的能力，最终解析出 AnnotatedCellWriteHandler 实例

### 辅助类
- StandaloneSpelExpressionResolver
    - 提供了SpEL表达式的解析能力
    - 用于辅助解析 @ExcelCellStyle 的条件表达式

- PresetExcelStyles
    - 预置的表格样式提供能力
    - 提供几个简单的样式
    - 一方面简化一些重复的样式编写
    - 另一方面也作为自行实现样式时的参考
