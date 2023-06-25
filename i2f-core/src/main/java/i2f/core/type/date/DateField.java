package i2f.core.type.date;

import java.util.Calendar;

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
