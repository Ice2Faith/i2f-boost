package i2f.core.str;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Ice2Faith
 * @date 2023/5/29 15:17
 * @desc
 */
public class CnYuanNumber {
    public static String floatUnits = "角|分|厘|毫|丝|忽|微|纳|皮";
    public static String simpleYuan = "元";
    public static String complexYuan = "圆";


    public static String num2cnYuan(double num, boolean big, int scale) {
        String yuan = simpleYuan;
        if (big) {
            yuan = complexYuan;
        }
        long inum = (long) num;
        String istr = CnNumber.num2cn(inum, big);
        String fstr = "";
        if (scale <= 0) {
            fstr = "整";
        } else {
            double fnum = Math.abs(num - inum);
            fstr = num2cnFloat(fnum, big, scale);
            String words = CnNumber.simpleWords;
            if (big) {
                words = CnNumber.complexWords;
            }
            if (fstr.equals(String.valueOf(words.charAt(0)))) {
                fstr = "整";
            }
        }

        return istr + yuan + fstr;
    }

    public static String num2cnFloat(double num, boolean big, int scale) {
        double anum = Math.abs(num);

        String prefix = "";
        if (num < 0) {
            prefix = "负";
        }
        num = anum;

        String words = CnNumber.simpleWords;
        String[] units = floatUnits.split("\\|");
        if (big) {
            words = CnNumber.complexWords;
            units = floatUnits.split("\\|");
        }

        if (num == 0) {
            return String.valueOf(words.charAt(0));
        }

        Queue<Integer> nums = new ArrayDeque<>();
        int i = 0;
        while (i < scale) {
            num *= 10;
            if (i == scale - 1) {
                num += 0.5;
            }
            int val = (int) ((long) num % 10);
            nums.add(val);
            i++;
        }

        String ret = "";
        int size = nums.size();
        int idx = 0;
        boolean lastZero = false;
        while (size > 0) {
            Integer val = nums.poll();

            if (val != 0) {
                ret += words.charAt(val);
            } else {
                if (!lastZero) {
                    ret += words.charAt(val);
                }
            }

            if (val != 0) {
                ret += units[idx];
                lastZero = false;
            } else {
                lastZero = true;
            }
            size--;
            idx++;
        }

        if (ret.endsWith(String.valueOf(words.charAt(0)))) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return prefix + ret;
    }
}
