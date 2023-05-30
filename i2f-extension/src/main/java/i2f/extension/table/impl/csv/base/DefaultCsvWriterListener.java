package i2f.extension.table.impl.csv.base;

/**
 * @author Ice2Faith
 * @date 2023/5/30 15:54
 * @desc
 */
public class DefaultCsvWriterListener implements CsvWriterListener {

    @Override
    public String writeValue(Object obj, int rowIdx, int colIdx) {
        return CsvUtils.stringify(obj);
    }
}
