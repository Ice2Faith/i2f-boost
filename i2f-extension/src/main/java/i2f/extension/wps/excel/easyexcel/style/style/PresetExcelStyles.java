package i2f.extension.wps.excel.easyexcel.style.style;

import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import i2f.extension.wps.excel.easyexcel.style.annotations.ExcelCellStyle;
import i2f.extension.wps.excel.easyexcel.style.data.ExcelStyleCallbackMeta;
import org.springframework.util.StringUtils;

/**
 * @author ltb
 * @date 2022/10/13 9:19
 * @desc
 */
public class PresetExcelStyles {
    public static WriteCellStyle createCellStyleByAnnotation(ExcelCellStyle ann) {
        WriteCellStyle ret = new WriteCellStyle();
        WriteFont cellWriteFont = new WriteFont();
        if (!StringUtils.isEmpty(ann.fontName())) {
            cellWriteFont.setFontName(ann.fontName());
        }
        cellWriteFont.setBold(ann.fontBold());
        cellWriteFont.setItalic(ann.fontItalic());
        if (ann.fontUnderline()) {
            cellWriteFont.setUnderline((byte) 1);
        }
        if (ann.fontColor() != null) {
            cellWriteFont.setColor(ann.fontColor().getIndex());
        } else {
            cellWriteFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if (ann.fontHeight() > 0) {
            cellWriteFont.setFontHeightInPoints(ann.fontHeight());
        }
        ret.setWriteFont(cellWriteFont);
        if (ann.backgroundColor() != null) {
            ret.setFillForegroundColor(ann.backgroundColor().getIndex());
        } else {
            ret.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        }
        ret.setBorderBottom(ann.borderBottom());
        ret.setBorderLeft(ann.borderLeft());
        ret.setBorderRight(ann.borderRight());
        ret.setBorderTop(ann.borderTop());
        ret.setHorizontalAlignment(ann.alignHorizontal());
        ret.setVerticalAlignment(ann.alignVertical());
        ret.setWrapped(ann.textWrapped());
        return ret;
    }

    public static WriteCellStyle cellColorFont(IndexedColors fontColor,
                                               String fontName,
                                               short high,
                                               boolean bold,
                                               boolean italic,
                                               boolean underline,
                                               IndexedColors bgColor) {
        WriteCellStyle ret = new WriteCellStyle();
        WriteFont cellWriteFont = new WriteFont();
        if (fontName != null) {
            cellWriteFont.setFontName(fontName);
        }
        cellWriteFont.setBold(bold);
        cellWriteFont.setItalic(italic);
        if (underline) {
            cellWriteFont.setUnderline((byte) 1);
        }
        if (fontColor != null) {
            cellWriteFont.setColor(fontColor.getIndex());
        } else {
            cellWriteFont.setColor(IndexedColors.BLACK.getIndex());
        }
        if (high > 0) {
            cellWriteFont.setFontHeightInPoints(high);
        }
        ret.setWriteFont(cellWriteFont);
        if (bgColor != null) {
            ret.setFillForegroundColor(bgColor.getIndex());
        } else {
            ret.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        }
        ret.setBorderBottom(BorderStyle.HAIR);
        ret.setBorderLeft(BorderStyle.HAIR);
        ret.setBorderRight(BorderStyle.HAIR);
        ret.setBorderTop(BorderStyle.HAIR);
        ret.setHorizontalAlignment(HorizontalAlignment.LEFT);
        ret.setVerticalAlignment(VerticalAlignment.CENTER);
        return ret;
    }

    public static WriteCellStyle redFontStyle() {
        return cellColorFont(IndexedColors.RED, null, (short) 11, false, false, false, IndexedColors.WHITE);
    }

    public static WriteCellStyle greenFontStyle() {
        return cellColorFont(IndexedColors.GREEN, null, (short) 11, false, false, false, IndexedColors.WHITE);
    }

    public static WriteCellStyle blueFontStyle() {
        return cellColorFont(IndexedColors.BLUE, null, (short) 11, false, false, false, IndexedColors.WHITE);
    }

    public static WriteCellStyle blackFontStyle() {
        return cellColorFont(IndexedColors.BLACK, null, (short) 11, false, false, false, IndexedColors.WHITE);
    }

    public static WriteCellStyle grayFontStyle() {
        return cellColorFont(IndexedColors.GREY_50_PERCENT, null, (short) 11, false, false, false, IndexedColors.WHITE);
    }

    public static WriteCellStyle stripeRowStyle(ExcelStyleCallbackMeta meta) {
        WriteCellStyle ret = new WriteCellStyle();
        if (meta.row != null && meta.row % 2 == 0) {
            ret.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        } else {
            ret.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        }
        return ret;
    }

    public static void urlStyle(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet, Workbook workbook) {
        CreationHelper creationHelper = workbook.getCreationHelper();
        Hyperlink link = creationHelper.createHyperlink(HyperlinkType.URL);
        link.setAddress(meta.val + "");
        cell.setHyperlink(link);

        WriteCellStyle fontColor = cellColorFont(IndexedColors.BLUE, null, (short) 11, false, true, true, null);

        CellStyle cellStyle = StyleUtil.buildHeadCellStyle(workbook, fontColor);
        cell.setCellStyle(cellStyle);
    }


    public static WriteCellStyle exampleStyle(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet, Workbook workbook) {
        // 单元格策略
        WriteCellStyle ret = new WriteCellStyle();
        // 设置背景颜色白色
        ret.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        // 设置垂直居中为居中对齐
        ret.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置左右对齐为靠左对齐
        ret.setHorizontalAlignment(HorizontalAlignment.LEFT);
        // 设置单元格上下左右边框为细边框
        ret.setBorderBottom(BorderStyle.MEDIUM);
        ret.setBorderLeft(BorderStyle.MEDIUM);
        ret.setBorderRight(BorderStyle.MEDIUM);
        ret.setBorderTop(BorderStyle.MEDIUM);
        // 创建字体实例
        WriteFont cellWriteFont = new WriteFont();
        // 设置字体大小
        cellWriteFont.setFontName("宋体");
        cellWriteFont.setFontHeightInPoints((short) 14);
        //设置字体颜色
        cellWriteFont.setColor(IndexedColors.RED.getIndex());
        //单元格颜色
        ret.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        ret.setWriteFont(cellWriteFont);

        return ret;
    }
}
