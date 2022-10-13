package i2f.extension.wps.excel.easyexcel.style.test;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import i2f.extension.wps.excel.easyexcel.style.annotations.ExcelCellStyle;
import i2f.extension.wps.excel.easyexcel.style.annotations.ExcelStyle;
import i2f.extension.wps.excel.easyexcel.style.data.ExcelStyleCallbackMeta;
import i2f.extension.wps.excel.easyexcel.style.style.PresetExcelStyles;
import lombok.Data;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * @author ltb
 * @date 2022/10/12 19:52
 * @desc
 */
@Data
@ExcelIgnoreUnannotated
public class TestExcelDo {

    @ExcelStyle(row = "com.test.style.PresetExcelStyles.stripeRowStyle")
    @ExcelProperty(value = "月份", order = 0)
    private String month;

    @ExcelStyle(col = "com.test.style.PresetExcelStyles.exampleStyle")
    @ExcelProperty(value = "姓名", order = 10)
    private String name;

    @ExcelStyle(cell = "com.test.style.PresetExcelStyles.urlStyle")
    @ExcelProperty(value = "照片", order = 20)
    private String photo;

    @ExcelStyle(cell = "errorLoginCountStyle")
    @ExcelProperty(value = "登录失败次数", order = 30)
    private String errorLoginCount;

    public static WriteCellStyle errorLoginCountStyle(ExcelStyleCallbackMeta meta) {
        try {
            Object val = meta.val;
            Integer cnt = Integer.parseInt(String.valueOf(val));
            if (cnt > 50) {
                return PresetExcelStyles.redFontStyle();
            }
        } catch (Exception e) {

        }
        return null;
    }

    @ExcelStyle(cell = "com.test.style.PresetExcelStyles.greenFontStyle")
    @ExcelProperty(value = {"用户统计", "登录"}, order = 40)
    private String loginCount;

    @ExcelStyle(cell = "com.test.style.PresetExcelStyles.blueFontStyle")
    @ExcelProperty(value = {"用户统计", "任务"}, order = 50)
    private String taskCount;

    @ExcelCellStyle(spel = "#{T(Integer).parseInt(val)>1}", fontColor = IndexedColors.PINK)
    @ExcelProperty(value = {"用户统计", "告警"}, order = 60)
    private String alarmCount;

    @ExcelStyle(cellClass = TestStyleCallback.class)
    @ExcelProperty(value = {"最后操作状态"}, order = 70)
    private String lastStatus;

}
