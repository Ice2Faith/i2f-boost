package i2f.extension.table.impl.poi.base;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Ice2Faith
 * @date 2023/5/30 11:09
 * @desc
 */
public interface ExcelReaderListener {
    Object readCellValue(Cell cell, Row row, Sheet sheet, Workbook workbook);

}
