package i2f.extension.table.impl.poi.base;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/5/30 9:52
 * @desc
 */
public class ExcelUtils {
    public static ThreadLocal<SimpleDateFormat> sfmt = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    public static DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter fmtTime = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String stringify(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof Date) {
            return sfmt.get().format((Date) obj);
        } else if (obj instanceof LocalDateTime) {
            return fmtDateTime.format((LocalDateTime) obj);
        } else if (obj instanceof LocalDate) {
            return fmtDate.format((LocalDate) obj);
        } else if (obj instanceof LocalTime) {
            return fmtTime.format((LocalTime) obj);
        }
        return String.valueOf(obj);
    }
}
