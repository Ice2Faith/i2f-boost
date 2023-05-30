package i2f.extension.table.impl.csv.base;

/**
 * @author Ice2Faith
 * @date 2023/5/30 15:53
 * @desc
 */
public interface CsvWriterListener {
    String writeValue(Object val, int rowIdx, int colIdx);
}
