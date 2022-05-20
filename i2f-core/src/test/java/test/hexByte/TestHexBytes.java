package test.hexByte;

import i2f.core.bit.BytesUtil;

/**
 * @author ltb
 * @date 2022/5/20 11:27
 * @desc
 */
public class TestHexBytes {
    public static void main(String[] args){
        String hex= BytesUtil.writeHexBytes("hello，你好呀！,ok".getBytes(),",","\\x",null);
        byte[] data=BytesUtil.readHexBytes(hex,",","\\x",null);
        String str=new String(data);

        System.out.println(str);

        hex="\\xe7\\xa6\\x8f\\xe5\\xb7\\x9e\\xe5\\xb8\\x82\\xe4\\xbb\\x93\\xe5\\xb1\\xb1\\xe5\\x8c\\xba\\xe5\\x86\\xa0\\xe6\\x97\\xad\\xe7\\x94\\xb5\\xe5\\xad\\x90\\xe4\\xba\\xa7\\xe5\\x93\\x81\\xe5\\xba\\x97";
        data=BytesUtil.readHexBytes(hex,null,"\\x",null);
        str=new String(data);

        System.out.println(str);

        hex="%e7%a6%8f%e5%b7%9e%e5%b8%82%e4%bb%93%e5%b1%b1%e5%8c%ba%e5%86%a0%e6%97%ad%e7%94%b5%e5%ad%90%e4%ba%a7%e5%93%81%e5%ba%97";
        data=BytesUtil.readHexBytes(hex,null,"%",null);
        str=new String(data);

        System.out.println(str);
    }
}
