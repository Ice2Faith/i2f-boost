package i2f.extension.wps.excel.easyexcel.style.core;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import i2f.extension.wps.excel.easyexcel.style.data.ExcelStyleCallbackMeta;
import i2f.extension.wps.excel.easyexcel.style.style.PresetExcelStyles;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.StringUtils;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author ltb
 * @date 2022/10/12 11:13
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
public class AnnotatedCellWriteHandler implements CellWriteHandler {

    public static <T> AnnotatedCellWriteHandler getInstance(Collection<T> data, Class<T> clazz, int offRow, int offCol) {
        return AnnotationCellStyleParser.parse(data, clazz, offRow, offCol);
    }

    public static <T> AnnotatedCellWriteHandler getInstance(Collection<T> data, Class<T> clazz, int offRow) {
        return AnnotationCellStyleParser.parse(data, clazz, offRow, 0);
    }

    public static <T> AnnotatedCellWriteHandler getInstance(Collection<T> data, Class<T> clazz) {
        return AnnotationCellStyleParser.parse(data, clazz, 1, 0);
    }

    // 数据区域样式表：<行号,<列号,样式>>
    private Map<Integer, Map<Integer, ExcelStyleCallbackMeta>> cellStyle = new HashMap<>();

    // 数据行样式
    private Map<Integer, ExcelStyleCallbackMeta> rowStyle = new HashMap<>();

    // 数据列样式
    private Map<Integer, ExcelStyleCallbackMeta> colStyle = new HashMap<>();

    // 已经设置过行样式
    private Set<Integer> rowStyleInited = new HashSet<>();

    // 已经设置过列样式
    private Set<Integer> colStyleInited = new HashSet<>();


    public AnnotatedCellWriteHandler(Map<Integer, Map<Integer, ExcelStyleCallbackMeta>> cellStyle, Map<Integer, ExcelStyleCallbackMeta> rowStyle, Map<Integer, ExcelStyleCallbackMeta> colStyle) {
        this.cellStyle = cellStyle;
        this.rowStyle = rowStyle;
        this.colStyle = colStyle;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, WriteCellData<?> cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {

        try {
            excelStyleApplyDelegate(writeSheetHolder, writeTableHolder, list, cell, head, integer, aBoolean);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    protected void excelStyleApplyDelegate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = sheet.getWorkbook();

        int rowIdx = cell.getRowIndex();
        int colIdx = cell.getColumnIndex();

        // 设置行样式
        ExcelStyleCallbackMeta sheetRowStyle = rowStyle.get(rowIdx);
        if (sheetRowStyle != null && !rowStyleInited.contains(rowIdx)) {
            WriteCellStyle style = invokeExcelStyleMethod(sheetRowStyle, cell, sheet, workbook);
            if (style != null) {
                CellStyle cellStyle = StyleUtil.buildCellStyle(workbook, null,style);
                sheet.getRow(rowIdx).setRowStyle(cellStyle);
                rowStyleInited.add(rowIdx);
            }
        }

        // 设置列样式
        ExcelStyleCallbackMeta sheetColStyle = colStyle.get(colIdx);
        if (sheetColStyle != null && !colStyleInited.contains(colIdx)) {
            WriteCellStyle style = invokeExcelStyleMethod(sheetColStyle, cell, sheet, workbook);
            if (style != null) {
                CellStyle cellStyle = StyleUtil.buildCellStyle(workbook,null, style);
                sheet.setDefaultColumnStyle(colIdx, cellStyle);
                colStyleInited.add(colIdx);
            }
        }


        // 设置单元格样式
        Map<Integer, ExcelStyleCallbackMeta> lineStyle = cellStyle.get(rowIdx);
        if (lineStyle != null) {
            ExcelStyleCallbackMeta writeCellStyle = lineStyle.get(colIdx);
            if (writeCellStyle != null) {
                resolveSpelExpression(writeCellStyle);
                WriteCellStyle style = invokeExcelStyleMethod(writeCellStyle, cell, sheet, workbook);
                if (style != null) {
                    CellStyle cellStyle = StyleUtil.buildCellStyle(workbook,null, style);
                    cell.setCellStyle(cellStyle);
                }
                annotationExcelStyleCellAfterProcess(writeCellStyle, cell, sheet, workbook);
            }
        }
    }

    public static void resolveSpelExpression(ExcelStyleCallbackMeta meta) {
        meta.styleEnable = false;
        if (meta.style == null) {
            return;
        }

        String express = meta.style.spel();
        if (!StringUtils.isEmpty(express)) {
            meta.styleEnable = StandaloneSpelExpressionResolver.getBool(express, meta);
        }
    }

    public static void annotationExcelStyleCellAfterProcess(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet, Workbook workbook) {
        if (!meta.styleEnable) {
            return;
        }
        if (meta.style.hyperLink()) {
            PresetExcelStyles.urlStyle(meta, cell, sheet, workbook);
        }
        if (meta.style.rowHeight() > 0) {
            cell.getRow().setHeightInPoints(meta.style.rowHeight());
        }
        if (meta.style.colWidth() > 0) {
            sheet.setColumnWidth(cell.getColumnIndex(), meta.style.colWidth());
        }
    }

    public static WriteCellStyle invokeExcelStyleMethod(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet, Workbook workbook) {
        if (meta == null) {
            return null;
        }
        if (meta.styleEnable) {
            return PresetExcelStyles.createCellStyleByAnnotation(meta.style);
        }
        if (meta.method == null) {
            return null;
        }
        Class<?>[] paramTypes = meta.method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        if (paramTypes.length >= 1) {
            args[0] = meta;
        }
        if (paramTypes.length >= 2) {
            args[1] = cell;
        }
        if (paramTypes.length >= 3) {
            args[2] = sheet;
        }
        if (paramTypes.length >= 4) {
            args[3] = workbook;
        }
        if (Modifier.isStatic(meta.method.getModifiers())) {
            try {
                Object retVal = meta.method.invoke(null, args);
                return (WriteCellStyle) retVal;
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        } else {
            if (meta.ivkObj == null && meta.clazz != null) {
//                meta.ivkObj = SpringUtil.getBean(meta.clazz);
                if (meta.ivkObj == null) {
                    try {
                        meta.ivkObj = meta.clazz.newInstance();
                    } catch (Exception e) {
                        log.debug(e.getMessage());
                    }
                }
            }
            if (meta.ivkObj == null) {
                return null;
            }
            try {
                Object retVal = meta.method.invoke(meta.ivkObj, args);
                return (WriteCellStyle) retVal;
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
        return null;
    }
}
