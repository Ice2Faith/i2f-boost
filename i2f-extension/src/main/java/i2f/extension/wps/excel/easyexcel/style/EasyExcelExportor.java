package i2f.extension.wps.excel.easyexcel.style;

import com.alibaba.excel.EasyExcel;
import i2f.extension.wps.excel.easyexcel.style.core.AnnotatedCellWriteHandler;

import java.io.OutputStream;
import java.util.List;

/**
 * @author ltb
 * @date 2022/10/13 16:53
 * @desc
 */
public class EasyExcelExportor {
    public static <T> void export(OutputStream os, String sheetName, List<T> data, Class<T> clazz) {
        export(os, sheetName, data, clazz, 1, 0);
    }

    public static <T> void export(OutputStream os, String sheetName, List<T> data, Class<T> clazz, int offRow) {
        export(os, sheetName, data, clazz, offRow, 0);
    }

    public static <T> void export(OutputStream os, String sheetName, List<T> data, Class<T> clazz, int offRow, int offCol) {
        EasyExcel.write(os)
                .registerWriteHandler(AnnotatedCellWriteHandler.getInstance(data, clazz, offRow, offCol))
                .sheet(0, sheetName)
                .head(clazz)
                .doWrite(data);
    }
}
