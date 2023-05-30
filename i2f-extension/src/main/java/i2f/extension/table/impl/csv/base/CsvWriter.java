package i2f.extension.table.impl.csv.base;

import java.io.*;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/5/30 15:50
 * @desc
 */
public class CsvWriter {
    public static void write(List<List<Object>> data,
                             File file,
                             String charset,
                             CsvWriterListener listener) throws IOException {
        OutputStream os = new FileOutputStream(file);
        write(data, os, charset, listener);
    }

    public static void write(List<List<Object>> data,
                             OutputStream os,
                             String charset,
                             CsvWriterListener listener) throws IOException {
        if (listener == null) {
            listener = new DefaultCsvWriterListener();
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, charset));
        int rowIdx = 0;
        for (List<Object> line : data) {
            int colIdx = 0;
            for (Object item : line) {
                if (colIdx != 0) {
                    writer.write(", ");
                }
                String str = listener.writeValue(item, rowIdx, colIdx);
                writer.write(str);
                colIdx++;
            }
            writer.write("\n");
            rowIdx++;
        }
        writer.flush();
        writer.close();
    }
}
