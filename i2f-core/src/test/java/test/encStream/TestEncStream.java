package test.encStream;

import i2f.core.io.stream.encrypt.EncryptStreamUtil;
import i2f.core.io.stream.encrypt.IEncryptor;
import i2f.core.io.stream.encrypt.XorEncryptor;

import java.io.IOException;

/**
 * @author ltb
 * @date 2022/5/20 10:37
 * @desc
 */
public class TestEncStream {
    public static void main(String[] args) throws IOException {
        IEncryptor enc=new XorEncryptor();
        byte[] data= EncryptStreamUtil.stringEncrypt("hello,你好呀，ok?",enc);

        String str=EncryptStreamUtil.stringDecrypt(data,enc);

        System.out.println(str);

    }
}
