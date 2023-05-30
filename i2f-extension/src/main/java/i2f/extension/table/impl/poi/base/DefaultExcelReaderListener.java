package i2f.extension.table.impl.poi.base;

import org.apache.poi.ss.usermodel.*;

/**
 * @author Ice2Faith
 * @date 2023/5/30 11:12
 * @desc
 */
public class DefaultExcelReaderListener implements ExcelReaderListener {
    @Override
    public Object readCellValue(Cell cell, Row row, Sheet sheet, Workbook workbook) {
        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            }
            return cell.getNumericCellValue();
        } else if (cellType == CellType.FORMULA) {
            return cell.getCellFormula();
        } else if (cellType == CellType.BLANK) {
            return "";
        } else if (cellType == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        } else if (cellType == CellType.ERROR) {
            byte err = cell.getErrorCellValue();
            return err;
        }
        return cell.getStringCellValue();
    }
}
