package i2f.core.stream.encrypt;

import i2f.core.annotations.notice.CloudBe;

/**
 * @author ltb
 * @date 2022/5/20 10:09
 * @desc 一个简单的加解密器实现，基于异或机制
 */
public class XorEncryptor implements IEncryptor{
    @CloudBe("-1")
    protected long maxLen=-1;
    protected long initFac=177;

    protected long idx=0;
    protected long fac=177;
    public XorEncryptor(){

    }
    public XorEncryptor(@CloudBe("-1") long maxLen){
        this.maxLen=maxLen;
    }
    public XorEncryptor(long fac,@CloudBe("-1") long maxLen){
        this.initFac=fac;
        this.fac=initFac;
        this.maxLen=maxLen;
    }
    @Override
    public int encrypt(int b) {
        byte ret=(byte)b;
        if(idx!=maxLen){
            ret=enc(ret);
        }
        return ret;
    }

    protected byte enc(byte b){
        b=(byte)((b^fac)&0x0ff);
        fac=(fac*31)+idx;
        idx++;
        return b;
    }

    @Override
    public void encrypt(byte[] bts, int offset, int len) {
        int i=0;
        while(idx!=maxLen && i<len){
            bts[offset+i]=enc(bts[offset+i]);
            i++;
        }
    }

    @Override
    public void reset() {
        idx=0;
        fac=initFac;
    }
}
