package i2f.core.match.impl;

import i2f.core.match.IMatcher;

/**
 * @author ltb
 * @date 2022/4/26 9:37
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
public class SimpleMatcher implements IMatcher {
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
    @Override
    public double match(String str,String patten){
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
                            return MATCH_FAILURE_VALUE;
                        }else{
                            sidx++;
                            pidx+=2;
                        }
                    }else{
                        if(npch!=str.charAt(sidx)){
                            return MATCH_FAILURE_VALUE;
                        }else{
                            sidx++;
                            pidx++;
                        }
                    }
                }else{
                    return calcMatchRate(sidx,pidx,slen,plen,mlen);
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
                            return MATCH_FAILURE_VALUE;
                        }
                    }
                    pidx++;
                }else{
                    return calcMatchRate(sidx,pidx,slen,plen,mlen);
                }
            }else if(pch=='?'){
                pidx++;
                sidx++;
            }else{
                if(pch!=str.charAt(sidx)){
                    return MATCH_FAILURE_VALUE;
                }
                pidx++;
                sidx++;
                mlen++;
            }
        }
        return calcMatchRate(sidx,pidx,slen,plen,mlen);
    }
}
