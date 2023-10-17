package i2f.core.j2ee.firewall.std.common;

import java.util.*;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/9/21 22:15
 * @desc
 */
public class FirewallAsserterUtils {
    public static List<Integer> arr2list(int[] arr){
        if(arr==null){
            return null;
        }
        List<Integer> ret=new ArrayList<>(Math.max(arr.length,32));
        for (int item : arr) {
            ret.add(item);
        }
        return ret;
    }
    public static List<Long> arr2list(long[] arr){
        if(arr==null){
            return null;
        }
        List<Long> ret=new ArrayList<>(Math.max(arr.length,32));
        for (long item : arr) {
            ret.add(item);
        }
        return ret;
    }
    public static List<Short> arr2list(short[] arr){
        if(arr==null){
            return null;
        }
        List<Short> ret=new ArrayList<>(Math.max(arr.length,32));
        for (short item : arr) {
            ret.add(item);
        }
        return ret;
    }
    public static List<Byte> arr2list(byte[] arr){
        if(arr==null){
            return null;
        }
        List<Byte> ret=new ArrayList<>(Math.max(arr.length,32));
        for (byte item : arr) {
            ret.add(item);
        }
        return ret;
    }
    public static List<Float> arr2list(float[] arr){
        if(arr==null){
            return null;
        }
        List<Float> ret=new ArrayList<>(Math.max(arr.length,32));
        for (float item : arr) {
            ret.add(item);
        }
        return ret;
    }
    public static List<Double> arr2list(double[] arr){
        if(arr==null){
            return null;
        }
        List<Double> ret=new ArrayList<>(Math.max(arr.length,32));
        for (double item : arr) {
            ret.add(item);
        }
        return ret;
    }
    public static List<Boolean> arr2list(boolean[] arr){
        if(arr==null){
            return null;
        }
        List<Boolean> ret=new ArrayList<>(Math.max(arr.length,32));
        for (boolean item : arr) {
            ret.add(item);
        }
        return ret;
    }
    public static List<Character> arr2list(char[] arr){
        if(arr==null){
            return null;
        }
        List<Character> ret=new ArrayList<>(Math.max(arr.length,32));
        for (char item : arr) {
            ret.add(item);
        }
        return ret;
    }
    public static<T> List<T> arr2list(T[] arr){
        if(arr==null){
            return null;
        }
        List<T> ret=new ArrayList<>(Math.max(arr.length,32));
        for (T item : arr) {
            ret.add(item);
        }
        return ret;
    }



    public static char[] merge(char[] basicArr,char[] includeArr,char[] excludeArr,char[] replaceArr){
        List<Character> list = merge(
                arr2list(basicArr),
                arr2list(includeArr),
                arr2list(excludeArr),
                arr2list(replaceArr)
        );
        if(list==null){
            return null;
        }
        List<Character> ret=new ArrayList<>(list.size());
        int retSize=0;
        for (Character ch : list) {
            if(ch!=null){
                ret.add(ch);
                retSize++;
            }
        }
        char[] arr=new char[retSize];
        for (int i = 0; i < arr.length; i++) {
            arr[i]=ret.get(i);
        }
        return arr;
    }

    public static<T> List<T> merge(Collection<T> basicArr,
                                   Collection<T> includeArr,
                                   Collection<T> excludeArr,
                                   Collection<T> replaceArr){
        Set<T> ret=new LinkedHashSet<>();
        if(replaceArr!=null){
            ret.addAll(replaceArr);
        }else if(basicArr!=null){
            ret.addAll(basicArr);
        }

        if(includeArr!=null){
            ret.addAll(includeArr);
        }

        if(excludeArr!=null){
            for (T item : excludeArr) {
                ret.remove(item);
            }
        }

        return new ArrayList<>(ret);
    }



    /**
     * 获取size个元素的全部组合
     * 指包含选一个，2个...size个元素的所有组合
     * 比如：size=3
     * 则，返回从3个中 ，选一个，选两个，选三个的所有组合
     * 结果为：[[0], [1], [2], [0, 1], [0, 2], [1, 2], [0, 1, 2]]
     *
     * @param size
     * @return
     */
    public static List<List<Integer>> getAllCombinations(int size) {
        List<List<Integer>> ret = new LinkedList<>();
        for (int i = 1; i <= size; i++) {
            // 从size中选区i个元素的组合
            LinkedList<Integer> current = new LinkedList<>();
            getNextCombinations(size, i, current, ret);
        }
        return ret;
    }

    /**
     * 获取从size个元素中取cnt个元素的所有组合
     * 比如：从3个中选择两个
     * 结果为：[[0, 1], [0, 2], [1, 2]]
     *
     * @param size
     * @param cnt
     * @return
     */
    public static List<List<Integer>> getCombinations(int size, int cnt) {
        List<List<Integer>> ret = new LinkedList<>();

        LinkedList<Integer> current = new LinkedList<>();
        getNextCombinations(size, cnt, current, ret);

        return ret;
    }

    /**
     * 进行dfs递归遍历的内部调用
     *
     * @param size    总共多少个元素
     * @param cnt     要选择多少个元素
     * @param current 已经选择了哪些元素
     * @param ret     结果集合
     */
    public static void getNextCombinations(int size, int cnt, LinkedList<Integer> current, List<List<Integer>> ret) {
        int curSize = current.size();
        if (curSize == cnt) {
            ret.add(new LinkedList<>(current));
            return;
        }
        int start = 0;
        if (curSize > 0) {
            start = current.getLast() + 1;
        }
        for (int i = start; i < size; i++) {
            current.add(i);
            getNextCombinations(size, cnt, current, ret);
            current.removeLast();
        }
    }

    public static final Function<String, String> ENCODE_0X_02X = (str) -> str2form(str, null, (ch) -> String.format("0x%02x", (int) ch));
    public static final Function<String, String> ENCODE_PER_02X = (str) -> str2form(str, null, (ch) -> String.format("%%%02x", (int) ch));
    public static final Function<String, String> ENCODE_XCODE_02X = (str) -> str2form(str, null, (ch) -> String.format("\\x%02x", (int) ch));
    public static final Function<String, String> ENCODE_UCODE_04X = (str) -> str2form(str, null, (ch) -> String.format("\\u%04x", (int) ch));


    public static String str2form(String str, String separator, Function<Character, String> chMapper) {
        if (str == null) {
            return str;
        }
        if ("".equals(str)) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        char[] chars = str.toCharArray();
        boolean isFirst = true;
        for (char ch : chars) {
            if (!isFirst) {
                if (separator != null) {
                    builder.append(separator);
                }
            }
            builder.append(chMapper.apply(ch));
            isFirst = false;
        }
        return builder.toString();
    }

    public static List<Function<String, String>> combinationsWrappers(List<Function<String, String>> singleWrappers, boolean useCombine) {
        int size = singleWrappers.size();

        List<Function<String, String>> wrappers = new LinkedList<>(singleWrappers);
        if (useCombine) {
            List<Function<String, String>> groupWrappers = new LinkedList<>();
            List<List<Integer>> groups = FirewallAsserterUtils.getAllCombinations(size);
            for (List<Integer> group : groups) {
                Function<String, String> groupWrapper = (str) -> {
                    String ret = str;
                    for (Integer idx : group) {
                        Function<String, String> func = singleWrappers.get(idx);
                        try {
                            ret = func.apply(ret);
                        } catch (Exception e) {

                        }
                    }
                    return ret;
                };
                groupWrappers.add(groupWrapper);
            }
            wrappers = groupWrappers;
        }
        return wrappers;
    }
}
