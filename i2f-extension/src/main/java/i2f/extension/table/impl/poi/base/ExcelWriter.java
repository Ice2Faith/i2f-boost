package i2f.extension.table.impl.poi.base;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/5/30 8:56
 * @desc
 */
public class ExcelWriter {
    public static void write(List<Object> data,
                             Function<Object, List<Object>> liner,
                             File file,
                             String sheetName,
                             ExcelWriterListener listener) throws IOException {
        List<List<Object>> lines = new LinkedList<>();
        for (Object val : data) {
            List<Object> line = liner.apply(val);
            lines.add(line);
        }
        write(lines, file, sheetName, listener);
    }

    public static void write(List<List<Object>> data,
                             File file,
                             String sheetName,
                             ExcelWriterListener listener) throws IOException {
        String sname = file.getName().toLowerCase();
        ExcelType type = null;
        if (sname.endsWith(".xls")) {
            type = ExcelType.XLS_HSSF;
        } else if (sname.endsWith(".xlsx")) {
            type = ExcelType.XLSX_SXSSF;
        }
        FileOutputStream fos = new FileOutputStream(file);
        write(data, fos, sheetName, type, listener);
    }

    public static void write(List<List<Object>> data,
                             OutputStream os,
                             String sheetName,
                             ExcelType type,
                             ExcelWriterListener listener) throws IOException {
        Workbook workbook = null;
        if (type == null) {
            type = ExcelType.XLSX_SXSSF;
        }
        if (sheetName == null || "".equals(sheetName)) {
            sheetName = "sheet1";
        }
        if (listener == null) {
            listener = new DefaultExcelWriterListener();
        }
        if (type == ExcelType.XLS_HSSF) {
            workbook = new HSSFWorkbook();
        } else if (type == ExcelType.XLSX_XSSF) {
            workbook = new XSSFWorkbook();
        } else if (type == ExcelType.XLSX_SXSSF) {
            workbook = new SXSSFWorkbook();
        }

        listener.onCreateWorkbook(workbook);

        Sheet sheet = workbook.createSheet(sheetName);

        listener.onCreateSheet(sheet, workbook);

        int rowIdx = 0;
        for (List<Object> line : data) {
            Row row = sheet.createRow(rowIdx);
            listener.onCreateRow(row, sheet, workbook);

            int colIdx = 0;
            for (Object val : line) {
                Cell cell = row.createCell(colIdx);
                listener.onCreateCell(cell, row, sheet, workbook);

                listener.writeCellValue(val, cell, row, sheet, workbook);

                colIdx++;
            }

            rowIdx++;
        }

        listener.onFinishSheet(sheet, workbook);

        workbook.write(os);

        os.close();
    }

}
