package i2f.core.escape;

import i2f.core.data.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/5/20 14:11
 * @desc 处理转义的类，适用泛类型
 * 转义，实质上是一些列的泛型替换
 */
public class Escapes {

    private static final String[][] clangMapping=new String[][]{
            {"\\\\","\\"},
            {"\\n","\n"},
            {"\\t","\t"},
            {"\\\"","\""},
            {"\\'","'"},
            {"\\r","\r"},
            {"\\b","\b"},
            {"\\f","\f"}
    };
    public static Map<String,String> getClangEscapeMap(){
        Map<String,String> ret=new HashMap<>();
        for(String[] item : clangMapping){
            ret.put(item[1],item[0]);
        }
        return ret;
    }
    public static Map<String,String> getClangDescapeMap(){
        Map<String,String> ret=new HashMap<>();
        for(String[] item : clangMapping){
            ret.put(item[0],item[1]);
        }
        return ret;
    }

    public static<K,V> Map<V,K> reverseKeyVal(Map<K,V> map){
        Map<V, K> ret=new HashMap<>();
        for(Map.Entry<K,V> item : map.entrySet()){
            ret.put(item.getValue(),item.getKey());
        }
        return ret;
    }

    public static String charEscape(String str, Map<String, String> map) {
        List<Character> list = new ArrayList<>();
        char[] arr = str.toCharArray();
        for (char item : arr) {
            list.add(item);
        }
        List<Pair<List<Character>, List<Character>>> rule = new ArrayList<>();
        for (Map.Entry<String, String> item : map.entrySet()) {
            String key = item.getKey();
            String val = item.getValue();
            List<Character> klist = new ArrayList<>();
            List<Character> vlist = new ArrayList<>();

            for (char ch : key.toCharArray()) {
                klist.add(ch);
            }
            for (char ch : val.toCharArray()) {
                vlist.add(ch);
            }

            rule.add(new Pair<>(klist, vlist));
        }

        List<Character> ret = escape(list, rule);
        int size = ret.size();
        char[] rarr = new char[size];
        int i = 0;
        for (Character item : ret) {
            rarr[i++] = item;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(rarr);

        return builder.toString();
    }

    public static byte[] byteEscape(byte[] data, List<Pair<byte[],byte[]>> map) {
        List<Byte> list = new ArrayList<>();
        for (byte item : data) {
            list.add(item);
        }
        List<Pair<List<Byte>, List<Byte>>> rule = new ArrayList<>();
        for (Pair<byte[],byte[]> item : map) {
            byte[] key = item.getKey();
            byte[] val = item.getVal();
            List<Byte> klist = new ArrayList<>();
            List<Byte> vlist = new ArrayList<>();

            for (byte ch : key) {
                klist.add(ch);
            }
            for (byte ch : val) {
                vlist.add(ch);
            }

            rule.add(new Pair<>(klist, vlist));
        }

        List<Byte> ret = escape(list, rule);
        int size = ret.size();
        byte[] rarr = new byte[size];
        int i = 0;
        for (Byte item : ret) {
            rarr[i++] = item;
        }

        return rarr;
    }


    /**
     * 根据转义规则rule对list进行转义
     * 实际上就是一种广泛意义的替换
     * rule是一个pair配对列表
     * 每一个pair中对应的key表示原来的序列，val表示新的替换序列
     * @param list 原来的序列
     * @param rule 替换规则列表
     * @param <T> 元素
     * @return
     */
    public static <T> List<T> escape(List<T> list, List<Pair<List<T>, List<T>>> rule) {
        Map<T,List<Integer>> active = new HashMap<>();
        int pi=0;
        for (Pair<List<T>, List<T>> item : rule) {
            List<T> ac = item.key;
            if(!active.containsKey(ac.get(0))){
                active.put(ac.get(0),new ArrayList<>());
            }
            active.get(ac.get(0)).add(pi);
            pi++;
        }
        List<T> ret = new ArrayList<>();
        int len = list.size();
        int i = 0;
        while (i < len) {
            T cur = list.get(i);
            if (active.containsKey(cur)) {
                int flen = -1;
                List<T> replace = new ArrayList<>();
                List<Integer> idxes=active.get(cur);
                for (int idx : idxes) {
                    Pair<List<T>, List<T>> item =rule.get(idx);
                    if (!item.key.get(0).equals(cur)) {
                        continue;
                    }
                    int plen = item.key.size();
                    if (i + plen >= len) {
                        continue;
                    }
                    boolean isMatch = true;
                    for (int p = 0; p < plen; p++) {
                        if (!item.key.get(p).equals(list.get(i + p))) {
                            isMatch = false;
                            break;
                        }
                    }
                    if (isMatch) {
                        replace = item.getVal();
                        flen = plen;
                        break;
                    }
                }
                if (flen > 0) {
                    ret.addAll(replace);
                    i += flen;
                } else {
                    ret.add(cur);
                    i++;
                }
            } else {
                ret.add(cur);
                i++;
            }
        }

        return ret;
    }

}
