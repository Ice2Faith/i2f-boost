package i2f.core.date;

import i2f.core.annotations.notice.NullableReturn;
import i2f.core.annotations.remark.Author;

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

@Author("i2f")
public class DateUtil {
    public static final String FMT_DATE_TIME_MILL="yyyy-MM-dd HH:mm:ss SSS";
    public static ThreadLocal<SimpleDateFormat> formatter=ThreadLocal.withInitial(()->{
        return new SimpleDateFormat(FMT_DATE_TIME_MILL);
    });
    public static ThreadLocal<SimpleDateFormat> sfmtMill=ThreadLocal.withInitial(()->{
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtSecond=ThreadLocal.withInitial(()->{
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtMinus=ThreadLocal.withInitial(()->{
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtHour=ThreadLocal.withInitial(()->{
        return new SimpleDateFormat("yyyy-MM-dd HH");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtDay=ThreadLocal.withInitial(()->{
        return new SimpleDateFormat("yyyy-MM-dd");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtMonth=ThreadLocal.withInitial(()->{
        return new SimpleDateFormat("yyyy-MM");
    });
    public static ThreadLocal<SimpleDateFormat> sfmtYear=ThreadLocal.withInitial(()->{
        return new SimpleDateFormat("yyyy");
    });
    public static final DateTimeFormatter fmtMill = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");
    public static final DateTimeFormatter fmtSecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter fmtMinus = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter fmtHour = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
    public static final DateTimeFormatter fmtDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter fmtMonth = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter fmtYear = DateTimeFormatter.ofPattern("yyyy");

    private static final Class<Calendar> calendarLock = Calendar.class;

    public static long date2secondTimestamp(Date date) {
        return date.getTime() / 1000;
    }

    public static Date secondTimestamp2Date(long stamp) {
        return new Date(stamp * 1000);
    }

    public static Date localDate2Date(LocalDate val) {
        ZoneId zone=ZoneId.systemDefault();
        Instant instant = ((LocalDate)val).atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date localDateTime2Date(LocalDateTime val){
        ZoneId zone=ZoneId.systemDefault();
        Instant instant = ((LocalDateTime)val).atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date calendar2Date(Calendar val){
        return val.getTime();
    }

    public static java.sql.Timestamp date2Timestamp(Date val){
        return new java.sql.Timestamp(val.getTime());
    }

    public static java.sql.Date date2sqlDate(Date val){
        return new java.sql.Date(val.getTime());
    }

    public static LocalDate date2LocalDate(Date val){
        Instant instant = new java.util.Date(val.getTime()).toInstant();
        ZoneId zone=ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone).toLocalDate();
    }

    public static LocalDateTime date2LocalDateTime(Date val){
        Instant instant = new java.util.Date(val.getTime()).toInstant();
        ZoneId zone=ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static Calendar date2Calendar(Date val){
        Calendar ret=Calendar.getInstance();
        ret.setTime(val);
        return ret;
    }

    public static String[] SUPPORT_PARSE_DATE_FORMATS={
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
    public static Date from(String date){
        for(String item : SUPPORT_PARSE_DATE_FORMATS){
            SimpleDateFormat fmt=new SimpleDateFormat(item);
            try{
                return fmt.parse(date);
            }catch(Exception e){

            }
        }
        return null;
    }
    @NullableReturn
    public static Date from(String date,String patten){
        SimpleDateFormat fmt=new SimpleDateFormat(patten);
        Date ret=null;
        try{
            ret=fmt.parse(date);
        }catch(ParseException e){

        }
        return ret;
    }
    public static Date parse(String date) throws ParseException {
        return parse(date,FMT_DATE_TIME_MILL);
    }
    public static Date parse(String date,String patten) throws ParseException {
        SimpleDateFormat fmt=new SimpleDateFormat(patten);
        return fmt.parse(date);
    }
    public static String format(Date date){
        return formatter.get().format(date);
    }
    public static String format(Date date,String patten){
        SimpleDateFormat fmt=new SimpleDateFormat(patten);
        return fmt.format(date);
    }
    @NullableReturn
    public static String convertFormat(String date,String srcFmt,String dstFmt){
        Date sdate=from(date,srcFmt);
        if(sdate!=null){
            return format(sdate,dstFmt);
        }
        return null;
    }
    public static Date addMillSeconds(Date date,long millSeconds){
        long time=date.getTime();
        return new Date(time+millSeconds);
    }
    public static Date addSeconds(Date date,long seconds){
        long time=date.getTime();
        return new Date(time+seconds2(seconds));
    }
    public static Date addMinus(Date date,long minus){
        long time=date.getTime();
        return new Date(time+minus2(minus));
    }
    public static Date addHours(Date date,long hours){
        long time=date.getTime();
        return new Date(time+hours2(hours));
    }
    public static Date addDays(Date date,long days){
        long time=date.getTime();
        return new Date(time+days2(days));
    }
    public static Date addWeek(Date date,long weeks){
        long time=date.getTime();
        return new Date(time+week2(weeks));
    }
    public static Date addMonth(Date date,int months) {
        synchronized (calendarLock) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, months);
            return calendar.getTime();
        }
    }
    public static Date addYear(Date date,int years) {
        synchronized (calendarLock) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, years);
            return calendar.getTime();
        }
    }
    public static Date add(Date date, long times, TimeUnit unit){
        long time=date.getTime();
        long fac=unit.toMillis(times);
        return new Date(time+fac);
    }
    public static Date lastDayOfMonth(Date date) {
        synchronized (calendarLock) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, +1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            return calendar.getTime();
        }
    }
    public static Date firstDayOfMonth(Date date) {
        synchronized (calendarLock) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return calendar.getTime();
        }
    }
    public static Date lastDayOfPreviousMonth(Date date){
        Date ndate=firstDayOfMonth(date);
        return addDays(ndate,-1);
    }
    public static Date firstDayOfNextMonth(Date date){
        Date ndate=lastDayOfMonth(date);
        return addDays(ndate,1);
    }
    public static Date lastDayOfYear(Date date) {
        synchronized (calendarLock) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, 0);
            calendar.add(Calendar.YEAR, 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            return calendar.getTime();
        }
    }
    public static Date firstDayOfYear(Date date) {
        synchronized (calendarLock) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, 0);
            return calendar.getTime();
        }
    }
    public static Date add(Date date, int fieldOfCalendar,int amount) {
        synchronized (calendarLock) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(fieldOfCalendar, amount);
            return calendar.getTime();
        }
    }
    public static Date set(Date date,int fieldOfCalendar,int value) {
        synchronized (calendarLock) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(fieldOfCalendar, value);
            return calendar.getTime();
        }
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
    public static Date lastDayOfPreviousSeason(Date date){
        Date sdate=firstDayOfSeason(date);
        return addDays(sdate,-1);
    }
    public static Date firstDayOfNextSeason(Date date){
        Date sdate=lastDayOfSeason(date);
        return addDays(sdate,1);
    }
    public static long diff(Date date1,Date date2){
        return (date1.getTime()-date2.getTime());
    }
    public static long toSeconds(long millSeconds){
        return millSeconds/1000;
    }
    public static long toMinus(long millSeconds){
        return millSeconds/1000/60;
    }
    public static long toHours(long millSeconds){
        return millSeconds/1000/60/60;
    }
    public static long toDays(long millSeconds){
        return millSeconds/1000/60/60/24;
    }
    public static long seconds2(long seconds){
        return seconds*1000;
    }
    public static long minus2(long minus){
        return minus*1000*60;
    }
    public static long hours2(long hours){
        return hours*1000*60*60;
    }
    public static long days2(long days){
        return days*1000*60*60*24;
    }
    public static long week2(long weeks){
        return weeks*1000*60*60*24*7;
    }

    public static boolean isLeapYear(int year){
        if(year%400==0 || (year%100!=0 && year%4==0)){
            return true;
        }
        return false;
    }

    public static boolean isLegalDate(int year,int month,int day){
        if(month<1 || month>12){
            return false;
        }
        if(day<1||day>31){
            return false;
        }
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if(day>31){
                    return false;
                }
                break;
            case 2:
                if(isLeapYear(year)){
                    if(day>29){
                        return false;
                    }
                }else{
                    if(day>28){
                        return false;
                    }
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if(day>30){
                    return false;
                }
                break;
        }
        return true;
    }
}

