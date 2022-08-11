package i2f.core.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ltb
 * @date 2022/8/5 21:17
 * @desc
 */
public class Dates {
    public interface DatePatten {
        String DATE_ONLY = "yyyy-MM-dd";
        String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
        String DATE_TIME_MILLI = "yyyy-MM-dd HH:mm:ss SSS";
        String MONTH = "yyyy-MM";
        String YEAR = "yyyy";
        String HOUR_MIN = "HH:mm";
        String TIME_ONLY = "HH:mm:ss";
    }

    public enum DateField {
        YEAR(Calendar.YEAR, 0, 9999, 0),
        MONTH(Calendar.MONTH, 0, 11, 0),// 0 is first month of year
        DAY(Calendar.DAY_OF_MONTH, 1, 31, -1), // 1 is first day of month
        HOUR(Calendar.HOUR_OF_DAY, 0, 23, 0),
        MINUTE(Calendar.MINUTE, 0, 59, 0),
        SECOND(Calendar.SECOND, 0, 59, 0),
        MILLISECOND(Calendar.MILLISECOND, 0, 999, 0),
        WEEK(Calendar.DAY_OF_WEEK, 1, 7, -1); // 0 is sunday,1 is monday

        private int code;
        private int min;
        private int max;
        private int off;

        private DateField(int code, int min, int max, int off) {
            this.code = code;
            this.min = min;
            this.max = max;
            this.off = off;
        }

        public int code() {
            return code;
        }

        public int min() {
            return min;
        }

        public int max() {
            return max;
        }

        public int calendar2logical(int val) {
            return val + off;
        }

        public int logical2calendar(int val) {
            return val - off;
        }
    }

    public static Date parse(String date, String patten) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat(patten);
        return fmt.parse(date);
    }

    public static String format(Date date, String patten) {
        SimpleDateFormat fmt = new SimpleDateFormat(patten);
        return fmt.format(date);
    }

    private static final Class<?> lock = Calendar.class;

    public static Calendar calendar() {
        return Calendar.getInstance();
    }

    public static Date add(Date date, DateField field, int offset) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.add(field.code(), offset);
            return calendar.getTime();
        }
    }

    public static Date set(Date date, DateField field, int value) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(field.code(), value);
            return calendar.getTime();
        }
    }

    public static int get(Date date, DateField field) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            return calendar.get(field.code());
        }
    }

    public static Date min(Date date, DateField field) {
        synchronized (lock) {
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
        synchronized (lock) {
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
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            return field.calendar2logical(calendar.get(field.code()));
        }
    }

    public static Date addMillisecond(Date date, int offset) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.add(DateField.MILLISECOND.code(), offset);
            return calendar.getTime();
        }
    }

    public static Date addSecond(Date date, int offset) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.add(DateField.SECOND.code(), offset);
            return calendar.getTime();
        }
    }

    public static Date addMinute(Date date, int offset) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.add(DateField.MINUTE.code(), offset);
            return calendar.getTime();
        }
    }

    public static Date addHour(Date date, int offset) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.add(DateField.HOUR.code(), offset);
            return calendar.getTime();
        }
    }

    public static Date addDay(Date date, int offset) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.add(DateField.DAY.code(), offset);
            return calendar.getTime();
        }
    }

    public static Date addMonth(Date date, int offset) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.add(DateField.MONTH.code(), offset);
            return calendar.getTime();
        }
    }

    public static Date addYear(Date date, int offset) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.add(DateField.YEAR.code(), offset);
            return calendar.getTime();
        }
    }

    public static Date firstSecondOfDay(Date date) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(DateField.HOUR.code(), DateField.HOUR.min());
            calendar.set(DateField.MINUTE.code(), DateField.MINUTE.min());
            calendar.set(DateField.SECOND.code(), DateField.SECOND.min());
            return calendar.getTime();
        }
    }

    public static Date lastSecondOfDay(Date date) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(DateField.HOUR.code(), DateField.HOUR.max());
            calendar.set(DateField.MINUTE.code(), DateField.MINUTE.max());
            calendar.set(DateField.SECOND.code(), DateField.SECOND.max());
            return calendar.getTime();
        }
    }

    public static Date firstDayOfMonth(Date date) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(DateField.DAY.code(), DateField.DAY.min());
            return calendar.getTime();
        }
    }

    public static Date lastDayOfMonth(Date date) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(DateField.DAY.code(), DateField.DAY.min());
            calendar.add(DateField.MONTH.code(), 1);
            calendar.add(DateField.DAY.code(), -1);
            return calendar.getTime();
        }
    }

    public static Date firstDayOfYear(Date date) {
        synchronized (lock) {
            Calendar calendar = calendar();
            calendar.setTime(date);
            calendar.set(DateField.DAY.code(), DateField.DAY.min());
            calendar.set(DateField.MONTH.code(), DateField.MONTH.min());
            return calendar.getTime();
        }
    }

    public static Date lastDayOfYear(Date date) {
        synchronized (lock) {
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
