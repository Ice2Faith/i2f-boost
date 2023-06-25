package i2f.core.type.date;

import i2f.core.annotations.notice.NullableReturn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/8/5 21:17
 * @desc
 */
public class Dates {
    public static final String FMT_DATE_TIME_MILL = "yyyy-MM-dd HH:mm:ss SSS";
    public static ThreadLocal<SimpleDateFormat> formatter = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat(FMT_DATE_TIME_MILL);
    });
    public static ThreadLocal<SimpleDateFormat> sfmtMill = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtSecond = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtMinus = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtHour = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("yyyy-MM-dd HH");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtDay = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("yyyy-MM-dd");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtMonth = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("yyyy-MM");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtYear = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("yyyy");
    });
    public static final DateTimeFormatter fmtMill = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");
    public static final DateTimeFormatter fmtSecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter fmtMinus = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter fmtHour = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
    public static final DateTimeFormatter fmtDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter fmtMonth = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter fmtYear = DateTimeFormatter.ofPattern("yyyy");


    public static String[] SUPPORT_PARSE_DATE_FORMATS = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss SSS",
            "yyyy-MM",
            "yyyyMMdd",
            "yyyyMM",
            "yyyyMMddHH",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd",
            "yyyy年MM月dd日 HH时mm分ss秒",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH",
            "yyyy"
    };

    @NullableReturn
    public static Date from(String date) {
        for (String item : SUPPORT_PARSE_DATE_FORMATS) {
            SimpleDateFormat fmt = new SimpleDateFormat(item);
            try {
                return fmt.parse(date);
            } catch (Exception e) {

            }
        }
        return null;
    }

    @NullableReturn
    public static Date from(String date, String patten) {
        SimpleDateFormat fmt = new SimpleDateFormat(patten);
        Date ret = null;
        try {
            ret = fmt.parse(date);
        } catch (ParseException e) {

        }
        return ret;
    }

    public static Date parse(String date) throws ParseException {
        return parse(date, FMT_DATE_TIME_MILL);
    }

    public static String format(Date date) {
        return formatter.get().format(date);
    }

    @NullableReturn
    public static String convertFormat(String date, String srcFmt, String dstFmt) {
        Date sdate = from(date, srcFmt);
        if (sdate != null) {
            return format(sdate, dstFmt);
        }
        return null;
    }

    public static boolean isLeapYear(int year) {
        if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)) {
            return true;
        }
        return false;
    }

    public static boolean isLegalDate(int year, int month, int day) {
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > 31) {
            return false;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if (day > 31) {
                    return false;
                }
                break;
            case 2:
                if (isLeapYear(year)) {
                    if (day > 29) {
                        return false;
                    }
                } else {
                    if (day > 28) {
                        return false;
                    }
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (day > 30) {
                    return false;
                }
                break;
        }
        return true;
    }

    /**
     * 获取年有多少天
     *
     * @param year
     * @return
     */
    public static long getDaysOnYear(int year) {
        if (isLeapYear(year)) {
            return 366;
        }
        return 365;
    }

    /**
     * 获取月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public static long getDaysOnMonth(int year, int month) {
        long ret = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                ret = 31;
                break;
            case 2:
                if (isLeapYear(year)) {
                    ret = 29;
                } else {
                    ret = 28;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                ret = 30;
                break;
        }
        return ret;
    }


    private static final Class<Calendar> calendarLock = Calendar.class;

    public static Date parse(String date, String patten) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat(patten);
        return fmt.parse(date);
    }

    public static String format(Date date, String patten) {
        SimpleDateFormat fmt = new SimpleDateFormat(patten);
        return fmt.format(date);
    }

    public static Calendar calendar() {
        return Calendar.getInstance();
    }

    public static Date add(Date date, DateField field, int offset) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.add(field.code(), offset);
            return calendar.getTime();
        }
    }

    public static Date set(Date date, DateField field, int value) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(field.code(), value);
            return calendar.getTime();
        }
    }

    public static int get(Date date, DateField field) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            return calendar.get(field.code());
        }
    }

    public static Date min(Date date, DateField field) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(field.code(), field.min());
            return calendar.getTime();
        }
    }

    /**
     * 根据程序员普遍认知，从0开始的计算方式
     * 0代表1月，代表星期天，代表1号
     *
     * @param date
     * @param field
     * @param value
     * @return
     */
    public static Date setLogical(Date date, DateField field, int value) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(field.code(), field.logical2calendar(value));
            return calendar.getTime();
        }
    }

    /**
     * 根据程序员普遍认知，从0开始的计算方式
     * 0代表1月，代表星期天，代表1号
     *
     * @param date
     * @param field
     * @return
     */
    public static int getLogical(Date date, DateField field) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            return field.calendar2logical(calendar.get(field.code()));
        }
    }


    public static Date addMillisecond(Date date, long millSeconds) {
        long time = date.getTime();
        return new Date(time + millSeconds);
    }

    public static Date addSecond(Date date, long seconds) {
        long time = date.getTime();
        return new Date(time + seconds2(seconds));
    }

    public static Date addMinute(Date date, long minus) {
        long time = date.getTime();
        return new Date(time + minus2(minus));
    }

    public static Date addHour(Date date, long hours) {
        long time = date.getTime();
        return new Date(time + hours2(hours));
    }

    public static Date addDay(Date date, long days) {
        long time = date.getTime();
        return new Date(time + days2(days));
    }

    public static Date addWeek(Date date, long weeks) {
        long time = date.getTime();
        return new Date(time + week2(weeks));
    }

    public static long diff(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime());
    }

    public static long toSeconds(long millSeconds) {
        return millSeconds / 1000;
    }

    public static long toMinus(long millSeconds) {
        return millSeconds / 1000 / 60;
    }

    public static long toHours(long millSeconds) {
        return millSeconds / 1000 / 60 / 60;
    }

    public static long toDays(long millSeconds) {
        return millSeconds / 1000 / 60 / 60 / 24;
    }

    public static long seconds2(long seconds) {
        return seconds * 1000;
    }

    public static long minus2(long minus) {
        return minus * 1000 * 60;
    }

    public static long hours2(long hours) {
        return hours * 1000 * 60 * 60;
    }

    public static long days2(long days) {
        return days * 1000 * 60 * 60 * 24;
    }

    public static long week2(long weeks) {
        return weeks * 1000 * 60 * 60 * 24 * 7;
    }

    public static Date addMonth(Date date, int offset) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.add(DateField.MONTH.code(), offset);
            return calendar.getTime();
        }
    }

    public static Date addYear(Date date, int offset) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.add(DateField.YEAR.code(), offset);
            return calendar.getTime();
        }
    }

    public static Date add(Date date, long times, TimeUnit unit) {
        long time = date.getTime();
        long fac = unit.toMillis(times);
        return new Date(time + fac);
    }

    public static Date lastDayOfPreviousMonth(Date date) {
        Date ndate = firstDayOfMonth(date);
        return addDay(ndate, -1);
    }

    public static Date firstDayOfNextMonth(Date date) {
        Date ndate = lastDayOfMonth(date);
        return addDay(ndate, 1);
    }

    public static int season(Date date) {
        synchronized (calendarLock) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int mon = calendar.get(Calendar.MONTH);
            return mon / 3;
        }
    }

    public static Date firstDayOfSeason(Date date) {
        synchronized (calendarLock) {
            int season = season(date);
            int mon = season * 3;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, mon);
            return calendar.getTime();
        }
    }

    public static Date lastDayOfSeason(Date date) {
        synchronized (calendarLock) {
            int season = season(date);
            int mon = season * 3 + 2;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, mon);
            return calendar.getTime();
        }
    }

    public static Date lastDayOfPreviousSeason(Date date) {
        Date sdate = firstDayOfSeason(date);
        return addDay(sdate, -1);
    }

    public static Date firstDayOfNextSeason(Date date) {
        Date sdate = lastDayOfSeason(date);
        return addDay(sdate, 1);
    }


    public static Date firstSecondOfDay(Date date) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(DateField.MILLISECOND.code(), DateField.MILLISECOND.min());
            calendar.set(DateField.SECOND.code(), DateField.SECOND.min());
            calendar.set(DateField.MINUTE.code(), DateField.MINUTE.min());
            calendar.set(DateField.HOUR.code(), DateField.HOUR.min());
            return calendar.getTime();
        }
    }

    public static Date lastSecondOfDay(Date date) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(DateField.MILLISECOND.code(), DateField.MILLISECOND.max());
            calendar.set(DateField.SECOND.code(), DateField.SECOND.max());
            calendar.set(DateField.MINUTE.code(), DateField.MINUTE.max());
            calendar.set(DateField.HOUR.code(), DateField.HOUR.max());
            return calendar.getTime();
        }
    }

    public static Date firstDayOfMonth(Date date) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(DateField.DAY.code(), DateField.DAY.min());
            return calendar.getTime();
        }
    }

    public static Date lastDayOfMonth(Date date) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(DateField.DAY.code(), DateField.DAY.min());
            calendar.add(DateField.MONTH.code(), 1);
            calendar.add(DateField.DAY.code(), -1);
            return calendar.getTime();
        }
    }

    public static Date firstDayOfYear(Date date) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(DateField.DAY.code(), DateField.DAY.min());
            calendar.set(DateField.MONTH.code(), DateField.MONTH.min());
            return calendar.getTime();
        }
    }

    public static Date lastDayOfYear(Date date) {
        synchronized (calendarLock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(DateField.MONTH.code(), DateField.MONTH.max());
            calendar.set(DateField.DAY.code(), DateField.DAY.max());
            return calendar.getTime();
        }
    }

    public static long date2timestamp(Date date) {
        return date.getTime() / 1000;
    }

    public static Date timestamp2date(long timestamp) {
        return new Date(timestamp * 1000);
    }

    public static Date localDate2Date(LocalDate val) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = ((LocalDate) val).atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date localDateTime2Date(LocalDateTime val) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = ((LocalDateTime) val).atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date calendar2Date(Calendar val) {
        return val.getTime();
    }

    public static java.sql.Timestamp date2Timestamp(Date val) {
        return new java.sql.Timestamp(val.getTime());
    }

    public static java.sql.Date date2sqlDate(Date val) {
        return new java.sql.Date(val.getTime());
    }

    public static LocalDate date2LocalDate(Date val) {
        Instant instant = new Date(val.getTime()).toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone).toLocalDate();
    }

    public static LocalDateTime date2LocalDateTime(Date val) {
        Instant instant = new Date(val.getTime()).toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static Calendar date2Calendar(Date val) {
        Calendar ret = Calendar.getInstance();
        ret.setTime(val);
        return ret;
    }

    private Date date = new Date();

    public Dates() {

    }

    public Dates(Date date) {
        this.date = date;
    }

    public static Dates now() {
        return new Dates(new Date());
    }

    public static Dates of(Date date) {
        return new Dates(date);
    }

    public static Dates of(long timestamp) {
        return of(timestamp2date(timestamp));
    }

    public long timestamp() {
        return date2timestamp(date);
    }

    public static Dates of(String date, String patten) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat(patten);
        Date time = fmt.parse(date);
        return of(time);
    }

    public String format(String patten) {
        SimpleDateFormat fmt = new SimpleDateFormat(patten);
        return fmt.format(date);
    }

    public Dates set(Date date) {
        this.date = date;
        return this;
    }

    public Date get() {
        return this.date;
    }

    public Dates current() {
        this.date = new Date();
        return this;
    }

    public Dates add(DateField field, int offset) {
        date = add(date, field, offset);
        return this;
    }

    public int get(DateField field) {
        return get(date, field);
    }

    public Dates set(DateField field, int value) {
        date = set(date, field, value);
        return this;
    }

    public Dates min(DateField field) {
        date = min(date, field);
        return this;
    }

    public int getLogical(DateField field) {
        return getLogical(date, field);
    }

    public Dates setLogical(DateField field, int value) {
        date = setLogical(date, field, value);
        return this;
    }

    public Dates addMillisecond(int offset) {
        date = addMillisecond(date, offset);
        return this;
    }

    public Dates addSecond(int offset) {
        date = addSecond(date, offset);
        return this;
    }

    public Dates addMinute(int offset) {
        date = addMinute(date, offset);
        return this;
    }

    public Dates addHour(int offset) {
        date = addHour(date, offset);
        return this;
    }

    public Dates addDay(int offset) {
        date = addDay(date, offset);
        return this;
    }

    public Dates addMonth(int offset) {
        date = addMonth(date, offset);
        return this;
    }

    public Dates addYear(int offset) {
        date = addYear(date, offset);
        return this;
    }

    public Dates firstSecondOfDay() {
        date = firstSecondOfDay(date);
        return this;
    }

    public Dates lastSecondOfDay() {
        date = lastSecondOfDay(date);
        return this;
    }

    public Dates firstDayOfMonth() {
        date = firstDayOfMonth(date);
        return this;
    }

    public Dates lastDayOfMonth() {
        date = lastDayOfMonth(date);
        return this;
    }

    public Dates firstDayOfYear() {
        date = firstDayOfYear(date);
        return this;
    }

    public Dates lastDayOfYear() {
        date = lastDayOfYear(date);
        return this;
    }

}
