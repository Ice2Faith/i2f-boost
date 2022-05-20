package i2f.core.stream.encrypt;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ltb
 * @date 2022/5/20 10:01
 * @desc 加解密包装输入流
 */
public class EncryptInputStream extends FilterInputStream {
    protected IEncryptor encryptor;
    protected EncryptInputStream(InputStream in,IEncryptor encryptor) {
        super(in);
        this.encryptor=encryptor;
        this.encryptor.reset();
    }

    @Override
    public int read() throws IOException {
        int b= super.read();
        b=encryptor.encrypt(b);
        return b;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int len= super.read(b);
        encryptor.encrypt(b,0,len);
        return len;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int rlen= super.read(b, off, len);
        encryptor.encrypt(b,off,rlen);
        return rlen;
    }


}
