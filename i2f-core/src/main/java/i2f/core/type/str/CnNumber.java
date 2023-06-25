package i2f.core.type.str;


import i2f.core.type.tuple.impl.Tuple3;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2023/5/29 14:31
 * @desc
 */
public class CnNumber {

    public static final String simpleWords = "零一二三四五六七八九";
    public static final String complexWords = "零壹贰叁肆伍陆柒捌玖";

    public static final String simpleUnits = "|十|百|千|万|十万|百万|千万|亿|十亿|百亿|千亿|兆|十兆|百兆|千兆|京";
    public static final String complexUnits = "|拾|佰|仟|萬|拾萬|佰萬|仟萬|亿|拾亿|佰亿|仟亿|兆|拾兆|佰兆|仟兆|京";

    public static final String simpleSplitUnits = "|万|亿|兆|京|垓|秭|穰|沟|涧|正|载|极|恒河沙|阿僧祇|那由他|不可思议|无量大数";
    public static final String complexSplitUnits = "|萬|亿|兆|京|垓|秭|穰|沟|涧|正|载|极|恒河沙|阿僧祇|那由他|不可思议|无量大数";

    public static String floatUnits = "分|厘|毫|丝|忽|微|纳|皮";

    public static List<Tuple3<Long, String, Integer>> cnSplit(long num, boolean big) {
        num = Math.abs(num);
        String[] units = simpleSplitUnits.split("\\|");
        if (big) {
            units = complexSplitUnits.split("\\|");
        }
        Stack<Tuple3<Long, String, Integer>> stack = new Stack<>();
        if (num == 0) {
            return new ArrayList<>(Arrays.asList(new Tuple3<>(num, units[0], 4)));
        }
        int count = 0;
        long base = 10000;
        int ncnt = 4;
        while (num != 0) {
            long val = num % base;
            stack.push(new Tuple3<>(val, units[count], ncnt));
            num /= base;
            count++;
        }
        ;

        List<Tuple3<Long, String, Integer>> ret = new ArrayList<>();
        while (!stack.isEmpty()) {
            Tuple3<Long, String, Integer> val = stack.pop();
            ret.add(val);
        }
        return ret;
    }

    public static String num2cn(long num, boolean big) {
        long anum = Math.abs(num);
        if (anum < 10000) {
            return num2cnSimple(num, big);
        }

        String prefix = "";
        if (num < 0) {
            prefix = "负";
        }
        num = anum;

        String words = simpleWords;
        if (big) {
            words = complexWords;
        }
        List<Tuple3<Long, String, Integer>> pairs = cnSplit(num, big);
        String ret = "";
        boolean lastZero = false;
        int idx = 0;
        for (Tuple3<Long, String, Integer> pair : pairs) {
            if (pair.t1 != 0) {
                if (idx != 0) {
                    Integer ncnt = (Integer) pair.t3;
                    String lstr = String.format("%d", pair.t1);
                    if (lastZero || lstr.length() != ncnt) {
                        ret += words.charAt(0);
                    }
                }
                ret += num2cn(pair.t1, big, pair.t2);
            }

            if (pair.t1 % 10 != 0) {
                lastZero = false;
            } else {
                lastZero = true;
            }
            idx++;
        }
        if (ret.endsWith(String.valueOf(words.charAt(0)))) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return prefix + ret;
    }

    public static String num2cn(long num, boolean big, String base) {
        String str = num2cnSimple(num, big);
        return str + base;
    }

    public static String num2cnSimple(long num, boolean big) {
        long anum = Math.abs(num);
        if (anum > 9999) {
            return num2cn(anum, big);
        }

        String prefix = "";
        if (num < 0) {
            prefix = "负";
        }
        num = anum;

        String words = simpleWords;
        String[] units = simpleUnits.split("\\|");
        if (big) {
            words = complexWords;
            units = complexUnits.split("\\|");
        }

        if (num == 0) {
            return String.valueOf(words.charAt(0));
        }

        Stack<Integer> nums = new Stack<Integer>();
        while (num != 0) {
            int val = (int) (num % 10);
            nums.push(val);
            num /= 10;
        }

        String ret = "";
        int size = nums.size();
        boolean lastZero = false;
        while (size > 0) {
            Integer val = nums.pop();
            size--;

            if (val != 0) {
                ret += words.charAt(val);
            } else {
                if (!lastZero) {
                    ret += words.charAt(val);
                }
            }

            if (val != 0) {
                ret += units[size];
                lastZero = false;
            } else {
                lastZero = true;
            }
        }

        if (ret.endsWith(String.valueOf(words.charAt(0)))) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return prefix + ret;
    }


    public static String num2cn(double num, boolean big, int scale) {
        long inum = (long) num;
        String istr = CnNumber.num2cn(inum, big);
        String fstr = "";
        if (scale <= 0) {
            fstr = "";
        } else {
            double fnum = Math.abs(num - inum);
            fstr = num2cnFloat(fnum, big, scale);
            String words = CnNumber.simpleWords;
            if (big) {
                words = CnNumber.complexWords;
            }
            if (fstr.equals(String.valueOf(words.charAt(0)))) {
                fstr = "";
            } else {
                fstr = "又" + fstr;
            }
        }

        return istr + fstr;
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
