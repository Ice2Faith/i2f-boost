package i2f.core.match.impl;

import i2f.core.match.IMatcher;

/**
 * @author ltb
 * @date 2022/4/25 17:15
 * @desc 通配符ant模式匹配类，处理*，?，**的通配符
 * *匹配0-多个符号
 * ?匹配一个符号
 * **匹配多级，每一集需要指定的分隔符
 * 允许patten中对*和?进行转义
 * 转义规则：
 * * --> \*
 * ? --> \?
 * 当\之后不是关键的*和?时，\的含义保持，不需要转义
 * 因此\\就是\\，而不是\
 */
public class AntMatcher implements IMatcher {
    protected String sep;
    public AntMatcher(){

    }
    public AntMatcher(String sep){
        this.sep=sep;
    }

    public String getSep() {
        return sep;
    }

    public AntMatcher setSep(String sep) {
        this.sep = sep;
        return this;
    }

    /**
     * 支持ant-match方式，可以自行指定分隔符
     * 例如.分隔的包名匹配方式，/分隔的路径匹配方式
     * @param str
     * @param patten
     * @return
     */
    @Override
    public  double match(String str,String patten){
        int pi=0;
        int plen=patten.length();
        int si=0;
        int slen=str.length();
        int mlen=0;
        while(pi<plen){
            if(si>=slen){
                return MATCH_FAILURE_VALUE;
            }
            char pch=patten.charAt(pi);
            if(pch=='\\'){
                if((pi+1)<plen){
                    char npch=patten.charAt(pi+1);
                    if(npch=='*' || npch=='?'){
                        if(npch!=str.charAt(si)){
                            return MATCH_FAILURE_VALUE;
                        }else{
                            si++;
                            pi+=2;
                        }
                    }else{
                        if(npch!=str.charAt(si)){
                            return MATCH_FAILURE_VALUE;
                        }else{
                            si++;
                            pi++;
                        }
                    }
                }else{
                    return calcMatchRate(si,pi,slen,plen,mlen);
                }
            }
            else if(pch=='*'){
                if((pi+1)<plen){
                    char npch=patten.charAt(pi+1);
                    if(npch=='*'){
                        // 多分段匹配
                        String jumpPatten="";
                        String nextPatten="";
                        int j=0;
                        while((pi+2+j)<plen && (patten.charAt(pi+2+j)=='*' || patten.substring(pi+2+j).startsWith(sep))){
                            jumpPatten+=patten.charAt(pi+2+j);
                            j++;
                        }
                        int k=0;
                        while((pi+2+j+k)<plen && patten.charAt(pi+2+j+k)!='*' && !patten.substring(pi+2+j+k).startsWith(sep)){
                            nextPatten+=patten.charAt(pi+2+j+k);
                            k++;
                        }


                        int m=0;
                        while((m+si)<slen){
                            String nextStr=str.substring(m+si);
                            if(nextStr.startsWith(nextPatten)){
                                m+=nextPatten.length();
                                mlen+=nextPatten.length();
                                break;
                            }
                            m++;
                        }
                        pi+=j+2+k;
                        si+=m;
                    }else{
                        // 单分段匹配
                        String nextPatten="";
                        int j=0;
                        while((pi+1+j)<plen && patten.charAt(pi+1+j)!='*' ){
                            nextPatten+=patten.charAt(pi+1+j);
                            j++;
                        }
                        int m=0;
                        while((m+si)<slen){
                            String nextStr=str.substring(m+si);
                            if(nextStr.startsWith(nextPatten)){
                                m+=nextPatten.length();
                                mlen+=nextPatten.length();
                                break;
                            }
                            if(nextStr.startsWith(sep)){
                                break;
                            }
                            m++;
                        }
                        pi+=j+1;
                        si+=m;
                    }
                }else{
                    String lstr=str.substring(si);
                    if(lstr.contains(sep)){
                        return MATCH_FAILURE_VALUE;
                    }else{
                        return calcMatchRate(si,pi,slen,plen,mlen);
                    }
                }
            }else if(pch=='?'){
                pi++;
                si++;
            }
            else{
                if(str.charAt(si)==pch){
                    pi++;
                    si++;
                    mlen++;
                }else{
                    return MATCH_FAILURE_VALUE;
                }
            }
        }

        return calcMatchRate(si,pi,slen,plen,mlen);
    }
}
