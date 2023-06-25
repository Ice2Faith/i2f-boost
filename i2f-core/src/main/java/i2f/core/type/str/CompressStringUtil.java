package i2f.core.type.str;

/**
 * @author ltb
 * @date 2022/3/31 13:44
 * @desc 仅适用于大量出现重复连续字符的场景，否则使用后不但不会起到压缩的效果，甚至还会使得数据量翻倍
 */
public class CompressStringUtil {
    /**
     * 字符串压缩，按照邻近相同进行合并
     * 例如：
     * aaabbccccdd
     * 压缩为
     * a3b2c4d2
     * @param str
     * @return
     */
    public static String compress(String str){
        StringBuilder builder=new StringBuilder();
        int len=str.length();
        int i=0;
        while(i<len){
         char chi=str.charAt(i);
         int j=0;
         while((i+j)<len && str.charAt(i+j)==chi){
             j++;
             if(j==Integer.MAX_VALUE){
                 break;
             }
         }

         builder.append(chi);
         char clen='0';
         if(j<10){
             clen=(char)(j+'0');
             builder.append(clen);
         }else if(j<15){
             clen=(char)(j-10+'A');
             builder.append(clen);
         }else{
             builder.append("[");
             builder.append(Integer.toHexString(j));
             builder.append("]");
         }
         i+=j;
        }
        return builder.toString();
    }

    public static String deCompress(String str){
        StringBuilder builder=new StringBuilder();
        int i=0;
        int len=str.length();
        while(i<len){
            char ch=str.charAt(i);
            if(i+1>=len){
                break;
            }
            char nch=str.charAt(i+1);
            if(nch=='['){
                int si=i+1;
                int j=0;
                while((si+j)<len && str.charAt(si+j)!=']'){
                    j++;
                }
                String slen=str.substring(si+1,si+j);
                int plen=Integer.parseInt(slen,16);
                for(int m=0;m<plen;m++){
                    builder.append(ch);
                }
                i=si+j+1;
            }else{
                int plen=0;
                if(nch>='0' && nch<='9'){
                    plen=nch-'0';
                }else{
                    plen=nch-'A'+10;
                }
                for(int j=0;j<plen;j++){
                    builder.append(ch);
                }
                i+=2;
            }
        }

        return builder.toString();
    }
}
