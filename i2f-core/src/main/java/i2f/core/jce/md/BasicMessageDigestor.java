package i2f.core.jce.md;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @author ltb
 * @date 2022/6/9 17:54
 * @desc
 */
public class BasicMessageDigestor implements IMessageDigestor{
    protected IMdType type;
    public BasicMessageDigestor(IMdType type){
        this.type=type;
    }
    @Override
    public byte[] mds(byte[] data) throws Exception {
        return mds(new ByteArrayInputStream(data));
    }

    @Override
    public byte[] mds(InputStream is) throws Exception {
        MessageDigest md=MessageDigest.getInstance(type.type());
        return getMds(is,md);
    }

    public static byte[] getMds(InputStream is, MessageDigest md) throws IOException {
        byte[] buf=new byte[16];
        int len=0;
        md.reset();
        while((len=is.read(buf))>0){
            md.update(buf,0,len);
        }
        return md.digest();
    }
}
