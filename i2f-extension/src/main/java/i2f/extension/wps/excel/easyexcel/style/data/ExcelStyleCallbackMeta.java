package i2f.extension.wps.excel.easyexcel.style.data;

import i2f.extension.wps.excel.easyexcel.style.annotations.ExcelCellStyle;
import i2f.extension.wps.excel.easyexcel.style.core.SpelEnhancer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/10/13 10:07
 * @desc
 */
public class ExcelStyleCallbackMeta {
    public Field field;
    public int index;
    public Method method;
    public ExcelCellStyle style;
    public boolean styleEnable = false;

    // for spel
    public SpelEnhancer tool = new SpelEnhancer();

    // args
    public Object record;
    public Object val;
    public Integer row;
    public Integer col;
    public Class clazz;
    public Object ivkObj;

}
