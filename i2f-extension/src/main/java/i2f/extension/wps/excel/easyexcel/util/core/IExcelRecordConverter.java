package i2f.extension.wps.excel.easyexcel.util.core;

public interface IExcelRecordConverter<T> {
    T convert(T bean);
}
