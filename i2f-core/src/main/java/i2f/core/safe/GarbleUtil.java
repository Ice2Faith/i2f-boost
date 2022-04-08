package i2f.core.safe;

import java.util.Random;

/**
 * @author ltb
 * @date 2022/3/31 10:10
 * @desc 混淆工具
 */
public class GarbleUtil {
    /**
     * 进行混淆，能够应用于前后端交互是，
     * 对公开算法rsa等的解密key一次混淆，
     * 可以达到干扰base64的目的
     * 源字符串
     * 123456789
     * 混淆结果示例
     * 6123456B01391038320789
     * 含义解析
     * 分组
     * 6 对源字符串的第6位开始插入一段随机字符
     * 123456 第6位之前的源字符串
     * B 源字符串混淆添加11位混淆数据
     * 01391038320 这就是11位混淆数据
     * 789 剩余的源数据
     * @param str
     * @return
     */
    public static String garble(String str){
        Random rand=new Random();
        int slen=str.length();
        StringBuilder builder=new StringBuilder();
        int insIdx=rand.nextInt(slen);
        if(insIdx>16){
            insIdx=insIdx%16;
        }
        int insLen=rand.nextInt(16);
        char insCh='0';
        if(insIdx<10){
            insCh=(char)(insIdx+'0');
        }else{
            insCh=(char)(insIdx-10+'A');
        }
        char lenCh='0';
        if(insLen<10){
            lenCh=(char)(insLen+'0');
        }else{
            lenCh=(char)(insLen-10+'A');
        }

        builder.append(insCh);
        builder.append(str.substring(0,insIdx));
        builder.append(lenCh);
        for(int i=0;i<insLen;i++){
            char pch='0';
            int num=rand.nextInt(16);
            if(num<10){
                pch=(char)(num+'0');
            }else{
                pch=(char)(num-10+'0');
            }
            builder.append(pch);
        }

        builder.append(str.substring(insIdx));

        return builder.toString();
    }

    public static String deGrable(String str){
        StringBuilder builder=new StringBuilder();
        char insCh=str.charAt(0);
        int insIdx=0;
        if(insCh>='0' && insCh<='9'){
            insIdx=insCh-'0';
        }else{
            insIdx=insCh-'A'+10;
        }
        builder.append(str.substring(1,insIdx+1));
        char lenCh=str.charAt(1+insIdx);
        int insLen=0;
        if(lenCh>='0' && lenCh<='9'){
            insLen=lenCh-'0';
        }else{
            insLen=lenCh-'A'+10;
        }
        builder.append(str.substring(1+insIdx+insLen+1));
        return builder.toString();
    }

}
