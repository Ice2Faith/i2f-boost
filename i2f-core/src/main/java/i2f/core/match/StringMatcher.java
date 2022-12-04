package i2f.core.match;

import i2f.core.annotations.notice.CloudBe;
import i2f.core.iterator.Iterators;
import i2f.core.match.impl.AntMatcher;
import i2f.core.match.impl.SimpleMatcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/1 14:31
 * @desc 针对IMatcher实现类做增强
 * IMatcher允许在进行匹配时，返回匹配的精确度
 * 因此这里用来对同一个字符串，进行多个匹配表达式匹配时，得到匹配结果的优先级
 * 用于辅助某些情况，多个匹配结果都符合时，选用最精确匹配的情况
 */
public class StringMatcher {

    public static final String ANT_SEP_PATH="/";

    public static final String ANT_SEP_CLASS=".";

    private static class MatchWeightItem{
        public String str;
        public double weight;
        public MatchWeightItem(){}
        public MatchWeightItem(String str,double weight){
            this.str=str;
            this.weight=weight;
        }
    }

    public static StringMatcher simple(){
        return new StringMatcher(new SimpleMatcher());
    }

    public static StringMatcher ant(String sep){
        return new StringMatcher(new AntMatcher(sep));
    }

    public static StringMatcher antPath(){
        return new StringMatcher(new AntMatcher(ANT_SEP_PATH));
    }

    public static StringMatcher antClass(){
        return new StringMatcher(new AntMatcher(ANT_SEP_CLASS));
    }

    protected IMatcher matcher;

    public StringMatcher(){
        this.matcher=new SimpleMatcher();
    }
    public StringMatcher(IMatcher matcher){
        this.matcher=matcher;
    }

    public IMatcher getMatcher() {
        return matcher;
    }

    public StringMatcher setMatcher(IMatcher matcher) {
        this.matcher = matcher;
        return this;
    }

    public  List<String> priorMatches(String str, String ... pattens){
        return priorMatches(str, Iterators.of(pattens), -1);
    }

    public  List<String> priorMatches(String str,Iterable<String> pattens){
        return priorMatches(str, Iterators.of(pattens), -1);
    }

    public List<String> priorMatches(String str, Iterator<String> iterator,@CloudBe("-1") int maxReturnCount){
        List<MatchWeightItem> list=new ArrayList<>();
        int cnt=0;
        while(iterator.hasNext()){
            String item= iterator.next();
            double rate=matchWithRate(str,item);
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


    public  boolean match(String str,String patten){
        return matched(matchWithRate(str,patten));
    }

    // 由于浮点数存在精度问题，如果直接与0.0比较判定匹配，则可能会误判，直接与-1比较也可能会发生误判，因此取中间值-0.5，保证不会误判
    public  boolean matched(double rate){
        return matcher.matched(rate);
    }

    public double matchWithRate(String str,String patten){
       return matcher.match(str,patten);
    }
}
