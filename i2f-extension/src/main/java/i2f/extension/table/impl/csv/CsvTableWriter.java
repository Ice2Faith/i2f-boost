package i2f.extension.table.impl.csv;


import i2f.core.type.tuple.impl.Tuple2;
import i2f.extension.table.TableWriter;
import i2f.extension.table.impl.csv.base.CsvWriter;
import i2f.extension.table.impl.csv.base.DefaultCsvWriterListener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/5/30 15:49
 * @desc
 */
public class CsvTableWriter implements TableWriter {
    private String charset = "GBK";

    public CsvTableWriter() {
    }

    public CsvTableWriter(String charset) {
        this.charset = charset;
    }

    @Override
    public void write(List<Tuple2<String, String>> heads, List<?> data, File file) throws IOException {
        List<List<Object>> lines = new LinkedList<>();
        List<Object> headLine = new LinkedList<>();
        for (Tuple2<String, String> head : heads) {
            headLine.add(head.t2);
        }
        lines.add(headLine);

        for (Object obj : data) {
            List<Object> dataLine = new LinkedList<>();
            for (Tuple2<String, String> head : heads) {
                String prop = head.t1;
                if (obj == null) {
                    dataLine.add("");
                } else if (obj instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) obj;
                    Object val = map.get(prop);
                    dataLine.add(val);
                } else {
                    Class<?> clazz = obj.getClass();
                    String camelName = prop.substring(0, 1).toUpperCase() + prop.substring(1);
                    Object val = "";
                    Method method = null;
                    try {
                        method = clazz.getMethod("get" + camelName);
                    } catch (Exception e) {
                    }
                    try {
                        if (method == null) {
                            method = clazz.getMethod("is" + camelName);
                        }
                    } catch (Exception e) {

                    }
                    if (method != null) {
                        try {
                            method.setAccessible(true);
                            val = method.invoke(obj);
                        } catch (Exception e) {

                        }
                    }
                    dataLine.add(val);
                }
            }
            lines.add(dataLine);
        }

        CsvWriter.write(lines, file, charset, new DefaultCsvWriterListener());
    }
}
