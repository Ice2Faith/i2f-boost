package i2f.extension.wps.excel.easyexcel.style.defined;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import i2f.extension.wps.excel.easyexcel.style.data.ExcelStyleCallbackMeta;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author ltb
 * @date 2022/10/13 10:02
 * @desc excel 表格样式回调函数原型定义
 */
@FunctionalInterface
public interface ExcelStyleCallback {
    /**
     * 根据给定的参数对表格样式进行设置
     *
     * @param meta     单元格数据信息
     * @param cell     单元格
     * @param sheet    表格
     * @param workbook 工作簿
     * @return 覆盖的样式，如果
     */
    WriteCellStyle style(ExcelStyleCallbackMeta meta, Cell cell, Sheet sheet, Workbook workbook);
}
