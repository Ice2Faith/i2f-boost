package i2f.extension.wps.excel.easyexcel.util.core;

public interface IExcelRecordFilter<T> {
    boolean pass(T bean);
}
