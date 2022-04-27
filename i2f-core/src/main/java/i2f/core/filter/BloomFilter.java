package i2f.core.filter;

import i2f.core.hash.IHashProvider;
import i2f.core.hash.ObjectHashcodeHashProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/27 8:47
 * @desc
 * bloom filter
 * which only mark and check
 * mark an element is exist
 * check an element whether exist
 * for bloom filter, only sure element not exist
 * not sure element must exist
 * exist true is possible
 */
public class BloomFilter<T> implements ICheckFilter<T>{
    protected long bitLen=4096;
    protected byte[] bits;
    protected List<IHashProvider<T>> hashs=new ArrayList<>();

    public BloomFilter(){
        this.prepare(4096,null);
    }

    public BloomFilter(long bitLen,IHashProvider<T> ... providers){
        List<IHashProvider<T>> list=new ArrayList<>();
        for(IHashProvider<T> item : providers){
            list.add(item);
        }
        this.prepare(bitLen,list);
    }

    public void prepare(long bitLen,List<IHashProvider<T>> providers){
        this.hashs.clear();
        this.bits=new byte[0];

        this.bitLen=bitLen;
        if(providers!=null && providers.size()>0){
            this.hashs.addAll(providers);
        }else{
            this.hashs.add(new ObjectHashcodeHashProvider<T>());
        }
        if(this.bitLen<=0){
            this.bitLen=32;
        }
        while(this.bitLen%8!=0){
            this.bitLen++;
        }
        int bitCount=(int)(this.bitLen/8);
        this.bits=new byte[bitCount];
        cleanBits();
    }

    protected void cleanBits(){
        for(int i=0;i<bits.length;i++){
            bits[i]=0;
        }
    }

    protected void setBit(long idx,boolean val){
        int bidx=(int)(idx/8);
        int boff=(int)(idx%8);
        byte bt=bits[bidx];
        int ibt=bt;

        int mask=1<<boff;
        ibt=ibt|mask;

        bt=(byte)(ibt & 0x0ff);
        bits[bidx]=bt;
    }

    protected boolean getBit(long idx){
        int bidx=(int)(idx/8);
        int boff=(int)(idx%8);
        byte bt=bits[bidx];
        int ibt=bt;
        ibt=ibt >>> boff;
        return (ibt&0x01) != 0;
    }

    protected long getHashIdx(IHashProvider<T> provider,T obj){
        long hcode=provider.hash(obj);
        if(hcode<0){
            hcode=0-hcode;
        }
        long idx=hcode%bitLen;
        return idx;
    }

    @Override
    public void mark(T obj) {
        for(IHashProvider<T> item : hashs){
            long idx=getHashIdx(item,obj);
            setBit(idx,true);
        }
    }

    @Override
    public boolean exists(T obj) {
        for(IHashProvider<T> item : hashs){
            long idx=getHashIdx(item,obj);
            boolean ex=getBit(idx);
            if(!ex){
                return false;
            }
        }
        return true;
    }

    public static long calcBestBitLen(double wrongRate,long allowCount){
        return (long)(0-((allowCount*Math.log(wrongRate))/(Math.pow(Math.log(2),2.0))));
    }
    public static int calcBestHashProviderCount(double wrongRate,long allowCount){
        long bitLen=calcBestBitLen(wrongRate, allowCount);
        return (int)(Math.log(2)*(bitLen/allowCount));
    }
}
