package i2f.extension.table.impl.poi.base;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Ice2Faith
 * @date 2023/5/30 9:52
 * @desc
 */
public interface ExcelWriterListener {
    void onCreateWorkbook(Workbook workbook);

    void onCreateSheet(Sheet sheet, Workbook workbook);

    void onCreateRow(Row row, Sheet sheet, Workbook workbook);

    void onCreateCell(Cell cell, Row row, Sheet sheet, Workbook workbook);

    void writeCellValue(Object val, Cell cell, Row row, Sheet sheet, Workbook workbook);

    void onFinishSheet(Sheet sheet, Workbook workbook);
}
