package i2f.extension.wps.excel.easyexcel.style.test;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import i2f.extension.wps.excel.easyexcel.style.data.ExcelStyleCallbackMeta;
import i2f.extension.wps.excel.easyexcel.style.defined.ExcelStyleCallback;
import i2f.extension.wps.excel.easyexcel.style.style.PresetExcelStyles;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author ltb
 * @date 2022/10/13 11:26
 * @desc
 */
public class TestStyleCallback implements ExcelStyleCallback {
    @Override
    public WriteCellStyle style(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet, Workbook workbook) {
        return PresetExcelStyles.cellColorFont(IndexedColors.SKY_BLUE, null, (short) 15, true, true, false, IndexedColors.GOLD);
    }
}
