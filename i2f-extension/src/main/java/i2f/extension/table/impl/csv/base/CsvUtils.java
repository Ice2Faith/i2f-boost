package i2f.extension.table.impl.csv.base;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/5/30 15:57
 * @desc
 */
public class CsvUtils {
    public static ObjectMapper mapper = new ObjectMapper();

    public static ThreadLocal<SimpleDateFormat> sfmt = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    public static DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter fmtTime = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String stringify(Object obj) {
        if (obj == null) {
            obj = "";
        } else if (obj instanceof Date) {
            obj = sfmt.get().format((Date) obj);
        } else if (obj instanceof LocalDateTime) {
            obj = fmtDateTime.format((LocalDateTime) obj);
        } else if (obj instanceof LocalDate) {
            obj = fmtDate.format((LocalDate) obj);
        } else if (obj instanceof LocalTime) {
            obj = fmtTime.format((LocalTime) obj);
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {

        }
        return "";
    }
}
