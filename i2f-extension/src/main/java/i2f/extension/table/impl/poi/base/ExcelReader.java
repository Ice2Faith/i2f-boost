package i2f.extension.table.impl.poi.base;

import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/5/30 10:06
 * @desc
 */
public class ExcelReader {
    public static List<List<Object>> read(InputStream is,
                                          ExcelReaderListener listener) throws IOException {
        return read(is, 0, null);
    }

    public static List<List<Object>> read(InputStream is,
                                          int sheetIdx,
                                          ExcelReaderListener listener) throws IOException {
        List<List<Object>> ret = new LinkedList<>();
        if (sheetIdx < 0) {
            sheetIdx = 0;
        }
        if (listener == null) {
            listener = new DefaultExcelReaderListener();
        }

        Workbook workbook = WorkbookFactory.create(is);

        int sheetCount = workbook.getNumberOfSheets();

        if (sheetIdx >= sheetCount) {
            sheetIdx = sheetCount - 1;
        }

        Sheet sheet = workbook.getSheetAt(sheetIdx);

        int rowCount = sheet.getPhysicalNumberOfRows();
        for (int rowIdx = 0; rowIdx < rowCount; rowIdx++) {
            Row row = sheet.getRow(rowIdx);
            List<Object> line = new LinkedList<>();
            int colCount = row.getPhysicalNumberOfCells();
            for (int colIdx = 0; colIdx < colCount; colIdx++) {
                Cell cell = row.getCell(colIdx);

                Object val = listener.readCellValue(cell, row, sheet, workbook);
                line.add(val);
            }
            ret.add(line);
        }

        is.close();

        return ret;
    }
}
