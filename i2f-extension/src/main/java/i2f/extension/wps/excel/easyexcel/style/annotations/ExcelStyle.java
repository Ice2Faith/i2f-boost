package i2f.extension.wps.excel.easyexcel.style.annotations;


import i2f.extension.wps.excel.easyexcel.style.defined.ExcelStyleCallback;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/10/12 11:52
 * @desc
 */
@Target({
        ElementType.FIELD,
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelStyle {
    // 指向一个方法，函数原型见： ExcelStyleCallback.class 中的定义
    // 但是支持匹配前N个参数的重载类型
    // 也就是说，如下参数列表重载也是支持的
    // WriteCellStyle style(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet, Workbook wb);
    // WriteCellStyle style(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet);
    // WriteCellStyle style(ExcelStyleCallbackMeta meta, Cell cell);
    // WriteCellStyle style(ExcelStyleCallbackMeta meta);
    // WriteCellStyle provide();
    // 同时支持返回值为void类型
    // 当返回值为WriteCellStyle时，返回值不为null时，样式将会进行覆盖
    // void style(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet, Workbook wb);
    // void style(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet);
    // void style(ExcelStyleCallbackMeta meta, Cell cell);
    // void style(ExcelStyleCallbackMeta meta);
    // void provide();
    // 因此，一般使用有两种方式，带返回值的，用来直接返回样式设置
    // 不带返回值的，用来自定义原始的样式

    // 在 PresetExcelStyles 中定义了一些常用的样式
    // 实际情况中，你可以根据自己的需求，按照预设的定义方式，编写自己的样式

    // 对单元格使用
    String cell() default "";

    Class<? extends ExcelStyleCallback> cellClass() default ExcelStyleCallback.class;

    // 对行使用
    String row() default "";

    Class<? extends ExcelStyleCallback> rowClass() default ExcelStyleCallback.class;

    // 对列使用
    String col() default "";

    Class<? extends ExcelStyleCallback> colClass() default ExcelStyleCallback.class;

}
