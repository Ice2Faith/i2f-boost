package i2f.core.io.stream.encrypt;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author ltb
 * @date 2022/5/20 10:01
 * @desc 加解密包装输出流
 */
public class EncryptOutputStream extends FilterOutputStream {
    protected IEncryptor encryptor;
    protected EncryptOutputStream(OutputStream os,IEncryptor encryptor) {
        super(os);
        this.encryptor=encryptor;
        this.encryptor.reset();
    }

    @Override
    public void write(int b) throws IOException {
        b= encryptor.encrypt(b);
        super.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        encryptor.encrypt(b,0,b.length);
        super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        encryptor.encrypt(b,off,len);
        super.write(b, off, len);
    }
}
