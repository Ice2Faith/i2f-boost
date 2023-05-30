package i2f.extension.table.impl.poi.base;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Ice2Faith
 * @date 2023/5/30 9:53
 * @desc
 */
public class DefaultExcelWriterListener implements ExcelWriterListener {
    @Override
    public void onCreateWorkbook(Workbook workbook) {

    }

    @Override
    public void onCreateSheet(Sheet sheet, Workbook workbook) {
//            sheet.setDefaultRowHeightInPoints((short)12);
//            sheet.setDefaultColumnWidth(300);
    }

    @Override
    public void onCreateRow(Row row, Sheet sheet, Workbook workbook) {
        int rowIdx = row.getRowNum();

    }

    @Override
    public void onCreateCell(Cell cell, Row row, Sheet sheet, Workbook workbook) {
        int rowIdx = row.getRowNum();
        int colIdx = cell.getColumnIndex();

    }

    @Override
    public void writeCellValue(Object val, Cell cell, Row row, Sheet sheet, Workbook workbook) {
        String str = ExcelUtils.stringify(val);
        cell.setCellValue(str);
    }

    @Override
    public void onFinishSheet(Sheet sheet, Workbook workbook) {

    }
}