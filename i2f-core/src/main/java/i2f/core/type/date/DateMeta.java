package i2f.core.type.date;

import i2f.core.annotations.remark.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2021/11/4
 */
@Author("i2f")
@Data
@NoArgsConstructor
public class DateMeta {
    public int year = -1; // 实际的年份0-2020
    public int month = -1; // 实际的月份，1-12
    public int day = -1; // 实际的日期，1-30
    public int week = -1; // 实际的星期，1-7
    public int cnShu = -1; // 年属相，1-12，鼠牛虎兔。。。
    public int cnYear = -1; // 中国黄历年，1-60，甲子。。。
    public int cnMonth = -1; // 中国黄历月，1-60，甲子。。。
    public int cnDay = -1; // 中国黄历日，1-60，甲子。。。

    public String weekDesc; // 星期
    public String cnShuDesc; // 年属相，1-12，鼠牛虎兔。。。
    public String cnYearDesc; // 中国黄历年，1-60，甲子。。。


    public DateMeta(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateMeta(int year, int month, int day, int week) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
    }
}
