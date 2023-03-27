package i2f.core.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2023/3/13 15:36
 * @desc
 */
public class DateFormatter {
    private static ConcurrentHashMap<String, SimpleDateFormat> simpleFormatMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, DateTimeFormatter> formatterMap = new ConcurrentHashMap<>();
    private static String[] pattens = {
            "yyyy-MM-dd HH:mm:ss SSS",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd",
            "yyyy-MM",
            "HH:mm:ss",
            "HH:mm",
            "yyyy/MM/dd HH:mm:ss SSS",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd HH:mm",
            "yyyy/MM/dd",
            "yyyy/MM",
            "yyyyMMddHHmmssSSS",
            "yyyyMMddHHmmss",
            "yyyyMMddHHmm",
            "yyyyMMdd",
            "yyyyMM",
            "yyyy年MM月dd日 HH时mm分ss秒 SSS",
            "yyyy年MM月dd日 HH时mm分ss",
            "yyyy年MM月dd日 HH时mm",
            "yyyy年MM月dd日",
            "yyyy年MM月",
            "HH时mm分ss秒",
            "HH时mm分",
    };

    public static SimpleDateFormat getSimpleFormatter(String patten) {
        if (simpleFormatMap.containsKey(patten)) {
            return simpleFormatMap.get(patten);
        }
        SimpleDateFormat fmt = new SimpleDateFormat(patten);
        simpleFormatMap.put(patten, fmt);
        return simpleFormatMap.get(patten);
    }

    public static DateTimeFormatter getFormatter(String patten) {
        if (formatterMap.containsKey(patten)) {
            return formatterMap.get(patten);
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(patten);
        formatterMap.put(patten, fmt);
        return formatterMap.get(patten);
    }

    public synchronized static String format(String patten, Date date) {
        return getSimpleFormatter(patten).format(date);
    }

    public synchronized static Date parse(String patten, String date) throws ParseException {
        return getSimpleFormatter(patten).parse(date);
    }

    public static Date parse(String date) throws ParseException {
        ParseException ex = null;
        for (String patten : pattens) {
            try {
                return parse(patten, date);
            } catch (ParseException e) {
                ex = e;
            }
        }
        throw ex;
    }

    public static String format(String patten, TemporalAccessor date) {
        return getFormatter(patten).format(date);
    }

    public static String format(String patten, LocalDate date) {
        return date.format(getFormatter(patten));
    }

    public static String format(String patten, LocalTime date) {
        return date.format(getFormatter(patten));
    }

    public static String format(String patten, LocalDateTime date) {
        return date.format(getFormatter(patten));
    }

    public static LocalDate parseLocalDate(String patten, String date) {
        return LocalDate.parse(date, getFormatter(patten));
    }

    public static LocalTime parseLocalTime(String patten, String date) {
        return LocalTime.parse(date, getFormatter(patten));
    }

    public static LocalDateTime parseLocalDateTime(String patten, String date) {
        return LocalDateTime.parse(date, getFormatter(patten));
    }

    public static LocalDateTime parseLocalDateTime(String date) {
        Exception ex = null;
        for (String patten : pattens) {
            try {
                return parseLocalDateTime(patten, date);
            } catch (Exception e) {
                ex = e;
            }
        }
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        } else {
            throw new IllegalArgumentException(ex);
        }
    }
}