package test.escape;

import i2f.core.data.Pair;
import i2f.core.escape.Escapes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/5/20 16:40
 * @desc
 */
public class TestEscape {
    public static void main(String[] args) {
        testStr();

        testBytesStr();

        testJt808Bytes();

        System.out.println("done");

    }

    public static void testJt808Bytes(){
        List<Pair<byte[],byte[]>> rule = new ArrayList<>();

        rule.add(new Pair<>(new byte[]{0x7e},new byte[]{0x7d,0x02}));
        rule.add(new Pair<>(new byte[]{0x7d},new byte[]{0x7d,0x01}));

        byte[] str = new byte[]{0x30,0x7e,0x08,0x7d,0x55 };
        byte[] rs = Escapes.byteEscape(str, rule);
        for(byte item : rs){
            System.out.print(String.format("0x%02x,",(int)(item&0x0ff)));
        }
        System.out.println();

        List<Pair<byte[],byte[]>> rrule = new ArrayList<>();
        for(Pair<byte[],byte[]> item : rule){
            rrule.add(new Pair<>(item.val,item.key));
        }

        byte[] rrs=Escapes.byteEscape(rs,rrule);
        for(byte item : rrs){
            System.out.print(String.format("0x%02x,",(int)(item&0x0ff)));
        }
        System.out.println();
    }

    public static void testBytesStr(){
        List<Pair<byte[],byte[]>> rule = new ArrayList<>();

        rule.add(new Pair<>(new byte[]{'\n'},new byte[]{'\\','n'}));
        rule.add(new Pair<>(new byte[]{'\t'},new byte[]{'\\','t'}));
        rule.add(new Pair<>(new byte[]{'\\'},new byte[]{'\\','\\'}));

        byte[] str = "a\nb\tc".getBytes();
        byte[] rs = Escapes.byteEscape(str, rule);

        System.out.println(new String(rs));

        List<Pair<byte[],byte[]>> rrule = new ArrayList<>();
        for(Pair<byte[],byte[]> item : rule){
            rrule.add(new Pair<>(item.val,item.key));
        }

        byte[] rrs=Escapes.byteEscape(rs,rrule);
        System.out.println(new String(rrs));
    }

    public static void testStr(){
        Map<String, String> rule = new HashMap<>();

        rule.put("\n", "\\n");
        rule.put("\t", "\\t");
        rule.put("\\", "\\\\");

        String str = "a\nb\tc";
        String rs = Escapes.charEscape(str, rule);

        System.out.println(rs);

        Map<String,String> rrule=new HashMap<>();
        for(Map.Entry<String,String> item : rule.entrySet()){
            rrule.put(item.getValue(),item.getKey());
        }

        String rrs=Escapes.charEscape(rs,rrule);
        System.out.println(rrs);
    }

}
