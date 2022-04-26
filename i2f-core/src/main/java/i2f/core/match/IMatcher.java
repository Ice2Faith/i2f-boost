package i2f.core.match;

/**
 * @author ltb
 * @date 2022/4/26 9:34
 * @desc 一个允许返回匹配字符串，并且可以得出匹配精确度的匹配接口
 * 在一般匹配结果为布尔值的情况下，增强了用于同一个串匹配多个表达式时，能够得出哪一个表达式更加匹配
 * 在某些情况下，用于根据匹配表达式得到表达式之间的优先级
 */
public interface IMatcher {
    double MATCH_FAILURE_VALUE=-1.0;
    double MATCH_SUCCESS_LIMIT=-0.5;
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
    double match(String str,String patten);
    default boolean matched(double rate){
        return rate>MATCH_SUCCESS_LIMIT;
    }
    default double calcMatchRate(int matchEndStrIndex,int matchEndPattenIndex,int strLen,int pattenLen,int matchEndMatchedCount){
        return ((((matchEndStrIndex+matchEndPattenIndex)*1.0/(strLen+pattenLen))*0.5)+((matchEndMatchedCount*1.0/strLen)*0.5));
    }
}
