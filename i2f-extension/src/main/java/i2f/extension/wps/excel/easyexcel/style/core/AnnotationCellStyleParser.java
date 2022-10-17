package i2f.extension.wps.excel.easyexcel.style.core;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import i2f.extension.wps.excel.easyexcel.style.annotations.ExcelCellStyle;
import i2f.extension.wps.excel.easyexcel.style.annotations.ExcelStyle;
import i2f.extension.wps.excel.easyexcel.style.data.ExcelStyleCallbackMeta;
import i2f.extension.wps.excel.easyexcel.style.defined.ExcelStyleCallback;
import i2f.extension.wps.excel.easyexcel.style.style.PresetExcelStyles;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author ltb
 * @date 2022/10/12 11:40
 * @desc 工具主入口 parse
 */
@Slf4j
public class AnnotationCellStyleParser {

    public static <T> AnnotatedCellWriteHandler parse(Collection<T> list, Class<T> clazz, int offRow, int offCol) {
        AnnotatedCellWriteHandler handler = new AnnotatedCellWriteHandler();

        try {
            parseIntoAnnotatedCellWriteHandler(list, clazz, offRow, offCol, handler);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return handler;
    }

    public static Map<Field, Class> getAllFields(Class clazz) {
        Map<Field, Class> ret = new LinkedHashMap<>();
        if (clazz == null || Object.class.equals(clazz)) {
            return ret;
        }
        Set<Field> fields = getFields(clazz);
        for (Field item : fields) {
            ret.put(item, clazz);
        }
        Class superClass = clazz.getSuperclass();
        if (superClass != null && !Object.class.equals(superClass)) {
            Map<Field, Class> next = getAllFields(superClass);
            ret.putAll(next);
        }
        return ret;
    }

    public static Set<Field> getFields(Class clazz) {
        Set<Field> ret = new LinkedHashSet<>();
        if (clazz == null || Object.class.equals(clazz)) {
            return ret;
        }
        for (Field item : clazz.getDeclaredFields()) {
            ret.add(item);
        }
        for (Field item : clazz.getFields()) {
            ret.add(item);
        }
        return ret;
    }

    public static <T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> clazz) {
        T ret = null;
        ret = elem.getDeclaredAnnotation(clazz);
        if (ret == null) {
            ret = elem.getAnnotation(clazz);
        }
        return ret;
    }

    public static class ExcelFieldBindData {
        public Field field;
        public ExcelProperty prop;
        public int sort;
        public int index;
        public ExcelCellStyle cellStyle;

        public ExcelStyle style;
        public Method rowMethod;
        public Method colMethod;
        public Method cellMethod;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?> returnType, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getMethod(methodName, parameterTypes);
            if (returnType != null) {
                if (!returnType.equals(method.getReturnType()) && !returnType.isAssignableFrom(method.getReturnType())) {
                    method = null;
                }
            }
            return method;
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static Method getExcelStyleMethod(Class clazz, String methodName, Class<? extends ExcelStyleCallback> implClass) {
        if (implClass != null && !implClass.equals(ExcelStyleCallback.class)) {
            try {
                Method mtd = implClass.getMethod("style", ExcelStyleCallbackMeta.class, Cell.class, Sheet.class, Workbook.class);
                if (mtd != null) {
                    return mtd;
                }
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
        if (methodName == null) {
            return null;
        }
        methodName = methodName.trim();
        if ("".equals(methodName)) {
            return null;
        }
        int idx = methodName.lastIndexOf(".");
        if (idx >= 0) {
            String className = methodName.substring(0, idx);
            methodName = methodName.substring(idx + 1);
            try {
                clazz = Class.forName(className);
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
        Method method = findStyleProvideMethod(clazz, methodName);
        if (method != null) {
            return method;
        }

        method = findStyleProvideMethod(PresetExcelStyles.class, methodName);
        if (method != null) {
            return method;
        }
        return null;
    }

    protected static Method findStyleProvideMethod(Class clazz, String methodName) {
        Method method = getMethod(clazz, methodName, WriteCellStyle.class, ExcelStyleCallbackMeta.class, Cell.class, Sheet.class, Workbook.class);
        if (method != null) {
            return method;
        }
        method = getMethod(clazz, methodName, WriteCellStyle.class, ExcelStyleCallbackMeta.class, Cell.class, Sheet.class);
        if (method != null) {
            return method;
        }
        method = getMethod(clazz, methodName, WriteCellStyle.class, ExcelStyleCallbackMeta.class, Cell.class);
        if (method != null) {
            return method;
        }
        method = getMethod(clazz, methodName, WriteCellStyle.class, ExcelStyleCallbackMeta.class);
        if (method != null) {
            return method;
        }
        method = getMethod(clazz, methodName, WriteCellStyle.class);
        if (method != null) {
            return method;
        }
        method = getMethod(clazz, methodName, void.class, ExcelStyleCallbackMeta.class, Cell.class, Sheet.class, Workbook.class);
        if (method != null) {
            return method;
        }
        method = getMethod(clazz, methodName, void.class, ExcelStyleCallbackMeta.class, Cell.class, Sheet.class);
        if (method != null) {
            return method;
        }
        method = getMethod(clazz, methodName, void.class, ExcelStyleCallbackMeta.class, Cell.class);
        if (method != null) {
            return method;
        }
        method = getMethod(clazz, methodName, void.class, ExcelStyleCallbackMeta.class);
        if (method != null) {
            return method;
        }
        method = getMethod(clazz, methodName, void.class);
        if (method != null) {
            return method;
        }
        return null;
    }

    public static ExcelFieldBindData[] getExcelFields(Class clazz) {
        Map<Field, Class> allFields = getAllFields(clazz);
        List<ExcelFieldBindData> fieldsList = new ArrayList<>();
        // 检查是否需要排除字段
        ExcelIgnoreUnannotated ignoreAnn = getAnnotation(clazz, ExcelIgnoreUnannotated.class);
        for (Field item : allFields.keySet()) {
            ExcelProperty propAnn = getAnnotation(item, ExcelProperty.class);
            if (ignoreAnn != null && propAnn == null) {
                continue;
            }
            int sort = Integer.MAX_VALUE;
            int index = -1;
            if (propAnn != null) {
                sort = propAnn.order();
                index = propAnn.index();
            }

            ExcelStyle styleAnn = getAnnotation(item, ExcelStyle.class);

            ExcelCellStyle cellStyleAnn = getAnnotation(item, ExcelCellStyle.class);

            ExcelFieldBindData bind = new ExcelFieldBindData();
            bind.field = item;
            bind.prop = propAnn;
            bind.sort = sort;
            bind.index = index;
            bind.style = styleAnn;
            bind.cellStyle = cellStyleAnn;
            fieldsList.add(bind);
        }

        ExcelFieldBindData[] arr = new ExcelFieldBindData[fieldsList.size()];
        LinkedList<ExcelFieldBindData> sortList = new LinkedList<>();

        for (ExcelFieldBindData item : fieldsList) {
            item.index = Math.min(item.index, arr.length - 1);
            if (item.index >= 0) {
                arr[item.index] = item;
            } else {
                sortList.add(item);
            }
        }

        // 字段排序
        sortList.sort((v1, v2) -> {
//            if(v1.sort==v2.sort){
//                return v1.field.getName().compareTo(v2.field.getName());
//            }
            return Integer.compare(v1.sort, v2.sort);
        });

        // 填充顺序数组
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                ExcelFieldBindData val = sortList.removeFirst();
                arr[i] = val;
            }
            arr[i].index = i;
            if (arr[i].style != null) {
                arr[i].rowMethod = getExcelStyleMethod(clazz, arr[i].style.row(), arr[i].style.rowClass());
                arr[i].colMethod = getExcelStyleMethod(clazz, arr[i].style.col(), arr[i].style.colClass());
                arr[i].cellMethod = getExcelStyleMethod(clazz, arr[i].style.cell(), arr[i].style.cellClass());
            }
        }

        return arr;
    }


    public static <T> AnnotatedCellWriteHandler parseIntoAnnotatedCellWriteHandler(Collection<T> list, Class<T> clazz, int offRow, int offCol, AnnotatedCellWriteHandler handler) {
        ExcelFieldBindData[] fields = getExcelFields(clazz);
        // 样式分类
        List<ExcelFieldBindData> rowList = new ArrayList<>();
        List<ExcelFieldBindData> colList = new ArrayList<>();
        List<ExcelFieldBindData> cellList = new ArrayList<>();
        for (ExcelFieldBindData item : fields) {
            if (item.rowMethod != null) {
                rowList.add(item);
            }
            if (item.colMethod != null) {
                colList.add(item);
            }
            if (item.cellMethod != null || item.cellStyle != null) {
                cellList.add(item);
            }
        }

        // 列样式，和数据无关
        if (!colList.isEmpty()) {
            for (ExcelFieldBindData bind : colList) {
                int colNum = bind.index + offCol;
                Method method = bind.colMethod;
                ExcelStyleCallbackMeta meta = new ExcelStyleCallbackMeta();
                meta.field = bind.field;
                meta.index = bind.index;
                meta.method = method;
                meta.val = null;
                meta.row = null;
                meta.col = colNum;
                meta.clazz = clazz;
                meta.ivkObj = null;
                meta.style = bind.cellStyle;
                Map<Integer, ExcelStyleCallbackMeta> colStyle = handler.getColStyle();
                colStyle.put(colNum, meta);

            }
        }

        if (list == null) {
            return handler;
        }

        // 行样式和单元格样式，和数据相关
        if (!rowList.isEmpty() || !cellList.isEmpty()) {
            int i = 0;
            for (T item : list) {
                int rowNum = i + offRow;
                if (!rowList.isEmpty()) {
                    for (ExcelFieldBindData bind : rowList) {
                        Method method = bind.rowMethod;
                        ExcelStyleCallbackMeta meta = new ExcelStyleCallbackMeta();
                        meta.field = bind.field;
                        meta.index = bind.index;
                        meta.method = method;
                        meta.val = item;
                        meta.row = rowNum;
                        meta.col = null;
                        meta.clazz = clazz;
                        meta.ivkObj = item;
                        meta.style = bind.cellStyle;
                        meta.record = item;
                        Map<Integer, ExcelStyleCallbackMeta> rowStyle = handler.getRowStyle();
                        rowStyle.put(rowNum, meta);
                    }
                }

                if (!cellList.isEmpty()) {
                    for (ExcelFieldBindData bind : cellList) {
                        int colNum = bind.index + offCol;
                        Method method = bind.cellMethod;
                        Object val = null;
                        try {
                            bind.field.setAccessible(true);
                            val = bind.field.get(item);
                        } catch (Exception e) {
                            log.debug(e.getMessage());
                        }
                        ExcelStyleCallbackMeta meta = new ExcelStyleCallbackMeta();
                        meta.field = bind.field;
                        meta.index = bind.index;
                        meta.method = method;
                        meta.val = val;
                        meta.row = rowNum;
                        meta.col = colNum;
                        meta.clazz = clazz;
                        meta.ivkObj = item;
                        meta.style = bind.cellStyle;
                        meta.record = item;
                        Map<Integer, Map<Integer, ExcelStyleCallbackMeta>> dataStyle = handler.getCellStyle();
                        if (!dataStyle.containsKey(rowNum)) {
                            dataStyle.put(rowNum, new HashMap<>());
                        }
                        Map<Integer, ExcelStyleCallbackMeta> rowStyle = dataStyle.get(rowNum);
                        rowStyle.put(colNum, meta);
                    }
                }

                i++;
            }
        }
        return null;
    }
}
