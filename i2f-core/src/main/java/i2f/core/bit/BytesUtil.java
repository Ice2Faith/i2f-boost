package i2f.core.bit;

import i2f.core.check.CheckUtil;
import i2f.core.container.array.Arrays;
import i2f.core.container.collection.Collections;
import i2f.core.type.str.Appender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/20 10:44
 * @desc
 */
public class BytesUtil {
    /**
     * 从字符串读取字节数据
     * 例如：
     * str=0x56,0x24
     * 则
     * sep=,
     * prefix=0x
     * suffix=null
     * --------------
     * 也可以是
     * sep=0x
     * prefix=null
     * suffix=,
     * --------------
     * 当不指定分隔符时，每个字节表示必须为2位
     * 然后自动根据：前缀+后缀+2的长度进行分隔字符串，开头不是前缀或结尾不是后缀时，自动补充前后缀
     * @param str 字符串
     * @param sep 每个字节的分隔符
     * @param prefix 每个字节分割后的前缀
     * @param suffix 每个字节分隔后的后缀
     * @return
     */
    public static byte[] readHexBytes(String str,String sep,String prefix,String suffix){
        List<Byte> ret=new ArrayList<>();
        List<String> arr=new ArrayList<>();
        boolean emptyPrefix=CheckUtil.isEmptyStr(prefix);
        boolean emptySuffix=CheckUtil.isEmptyStr(suffix);
        if(CheckUtil.isEmptyStr(sep)){
            int len=2;
            if(!emptyPrefix){
                len+=prefix.length();
                if(!str.startsWith(prefix)){
                    str=prefix+str;
                }
            }
            if(!emptySuffix){
                len+=suffix.length();
                if(!str.endsWith(suffix)){
                    str=str+suffix;
                }
            }
            int pi=0;
            while((pi+len)<str.length()){
                arr.add(str.substring(pi,pi+len));
                pi+=len;
            }
        }else{
            arr.addAll(Collections.arrayList(str.split(sep)));
        }

        for(String item : arr){
            if(!emptyPrefix){
                item=item.substring(prefix.length());
            }
            if(!emptySuffix){
                item=item.substring(item.length()-suffix.length());
            }
            item=item.toLowerCase().trim();
            if(!item.matches("^[a-z0-9]+$")){
                continue;
            }
            int val = Integer.parseInt(item, 16);
            byte b=(byte)(val&0x0ff);
            ret.add(b);
        }

        Byte[] bts = Arrays.collect(ret, Byte[].class);
        byte[] bytes = new byte[bts.length];
        for (int i = 0; i < bts.length; i++) {
            bytes[i]=bts[i];
        }
        return bytes;
    }

    /**
     * 将字节数据写出为字符串
     * @param bytes 字节数据
     * @param sep 分隔符
     * @param prefix 前缀
     * @param suffix 后缀
     * @return
     */
    public static String writeHexBytes(byte[] bytes,String sep,String prefix,String suffix){
        Appender<StringBuilder> builder = Appender.builder();
        boolean emptySep=CheckUtil.isEmptyStr(sep);
        boolean emptyPrefix=CheckUtil.isEmptyStr(prefix);
        boolean emptySuffix=CheckUtil.isEmptyStr(suffix);

        boolean isFirst=true;
        for(byte item : bytes){
            if(!isFirst){
                builder.addWhen(!emptySep,sep);
            }
            builder.addWhen(!emptyPrefix,prefix)
                    .addFormat("%02x",(int)(item&0x0ff))
                    .addWhen(!emptySuffix,suffix);
            isFirst=false;
        }
        return builder.get();
    }


}
