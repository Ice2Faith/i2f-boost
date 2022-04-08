package i2f.core.str;

import i2f.core.annotations.notice.CloudBe;
import i2f.core.collection.adapter.ArrayIteratorAdapter;
import i2f.core.collection.adapter.IterableIteratorAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/1 14:31
 * @desc 通配符匹配类，处理*，?的通配符
 * *匹配0-多个符号
 * ?匹配一个符号
 * 允许patten中对*和?进行转义
 * 转义规则：
 * * --> \*
 * ? --> \?
 * 当\之后不是关键的*和?时，\的含义保持，不需要转义
 * 因此\\就是\\，而不是\
 */
public class StringMatcher {

    private static class MatchWeightItem{
        public String str;
        public double weight;
        public MatchWeightItem(){}
        public MatchWeightItem(String str,double weight){
            this.str=str;
            this.weight=weight;
        }
    }

    public static List<String> priorMatches(String str,String ... pattens){
        return priorMatches(str,new ArrayIteratorAdapter<>(pattens),-1);
    }

    public static List<String> priorMatches(String str,Iterable<String> pattens){
        return priorMatches(str,new IterableIteratorAdapter<>(pattens),-1);
    }

    public static List<String> priorMatches(String str, Iterator<String> iterator,@CloudBe("-1") int maxReturnCount){
        List<MatchWeightItem> list=new ArrayList<>();
        int cnt=0;
        while(iterator.hasNext()){
            String item= iterator.next();
            double rate=matchWithRate(item,str);
            if(matched(rate)){
                list.add(new MatchWeightItem(item,rate));
                cnt++;
                if(maxReturnCount>0 && maxReturnCount==cnt){
                    break;
                }
            }
        }
        list.sort(new Comparator<MatchWeightItem>() {
            @Override
            public int compare(MatchWeightItem o1, MatchWeightItem o2) {
                return (o1.weight>o2.weight)?-1:1;
            }
        });
        List<String> ret=new ArrayList<>();
        for(MatchWeightItem item : list){
            ret.add(item.str);
        }
        return ret;
    }


    public static boolean match(String patten,String str){
        return matched(matchWithRate(patten,str));
    }

    // 由于浮点数存在精度问题，如果直接与0.0比较判定匹配，则可能会误判，直接与-1比较也可能会发生误判，因此取中间值-0.5，保证不会误判
    public static boolean matched(double rate){
        return rate>-0.5;
    }

    /**
     * 通配符匹配
     * 返回值含义，为负数，表示不匹配
     * 为正数，表示（末尾匹配程度+总体匹配重叠度）的均值
     * 此时可以用于表示哪一个patten更能够更加精准来匹配字符串
     * 这个返回值在某些情况下很有用，比如多个匹配规则同时适用一个目标时，优选某个规则的情况
     * @param patten
     * @param str
     * @return
     */
    public static double matchWithRate(String patten,String str){
        int sidx=0;
        int pidx=0;
        int plen=patten.length();
        int slen=str.length();
        int mlen=0;
        while(pidx<plen && sidx<slen){
            char pch=patten.charAt(pidx);
            if(pch=='\\'){
                if((pidx+1)<plen){
                    char npch=patten.charAt(pidx+1);
                    if(npch=='*' || npch=='?'){
                        if(npch!=str.charAt(sidx)){
                            return -1;
                        }else{
                            sidx++;
                            pidx+=2;
                        }
                    }else{
                        if(npch!=str.charAt(sidx)){
                            return -1;
                        }else{
                            sidx++;
                            pidx++;
                        }
                    }
                }else{
                    return ((((sidx+pidx)*1.0/(slen+plen))*0.5)+((mlen*1.0/slen)*0.5));
                }
            } else if(pch=='*'){
                if((pidx+1)<plen){
                    int edx=pidx+1;
                    while(edx<plen && patten.charAt(edx)!='*' && patten.charAt(edx)!='?'){
                        edx++;
                    }
                    String wstr=patten.substring(pidx+1,edx);
                    char swfch=wstr.charAt(0);
                    while(sidx<slen){
                        char sch=str.charAt(sidx);
                        if(sch==swfch){
                            String nxtstr=str.substring(sidx);
                            if(nxtstr.startsWith(wstr)){
                                break;
                            }
                        }
                        sidx++;
                        if(sidx==slen){
                            return -1;
                        }
                    }
                    pidx++;
                }else{
                    return ((((sidx+pidx)*1.0/(slen+plen))*0.5)+((mlen*1.0/slen)*0.5));
                }
            }else if(pch=='?'){
                pidx++;
                sidx++;
            }else{
                if(pch!=str.charAt(sidx)){
                    return -1;
                }
                pidx++;
                sidx++;
                mlen++;
            }
        }
        return ((((sidx+pidx)*1.0/(slen+plen))*0.5)+((mlen*1.0/slen)*0.5));
    }
}
