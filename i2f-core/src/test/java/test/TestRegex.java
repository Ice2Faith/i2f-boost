package test;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ltb
 * @date 2022/3/28 16:00
 * @desc
 */
public class TestRegex {
    public static void main(String[] args){
        List<RegexMatchItem> list=regexFinds(src,"订购受理流水=\\d+");
        System.out.println(list);
        StringBuilder builder=new StringBuilder();
        boolean isFirst=true;
        for(RegexMatchItem item : list){
            int idx=item.matchStr.indexOf("=");
            if(idx>=0){
                String no= item.matchStr.substring(idx+1);
                if(!"".equals(no)){
                    if(!isFirst){
                        builder.append(" ,");
                    }
                    builder.append(no);
                    isFirst=false;
                }
            }

        }
        System.out.println(builder.toString());
    }

    public static String src="【　　　　　　元素组间关系：全部与　　　　　　元素组信息：{订购个人增值产品信息1=[订购产品=(1000860005)惠生活铂金会员,失效时间=2023-01-01 00:00:00,受理时间=2022-01-22 11:15:42,数据变更生效日期=2022-01-22 00:00:00,数据变更失效日期=2099-12-31 00:00:00,订购受理流水=1203000000];　　　___　　　重点业务通信量系数1=[统计月份=202201,核心通信量系数=43177.72];　　　___　　　订购个人增值产品信息2=[订购产品=(120300078274)咪咕视频钻石会员包月15元,失效时间=2023-01-01 00:00:00,受理时间=2022-01-22 11:15:42,数据变更生效日期=2022-02-05 00:00:00,数据变更失效日期=2022-02-20 00:00:00,订购受理流水=120300078274)咪咕视频钻石会员包月15元];　　　___　　　订购个人增值产品信息3=[订购产品=(120300091217)和彩云超级会员月包,订购受理流水=12030007812111,失效时间=2023-01-01 00:00:00,受理时间=2022-01-22 11:15:42,数据变更生效日期=2022-02-05 00:00:00,数据变更失效日期=2022-02-20 00:00:00]}】　　　　　　公共计酬条件信息(小项层)：【　　　　　　元素组间关系：全部与　　　　　　元素组信息：{订购个人增值产品信息1=[订购受理流水=120300078123333,订购产品=(1000860005)惠生活铂金会员,失效时间=2023-01-01 00:00:00,受理时间=2022-01-22 11:15:42,数据变更生效日期=2022-01-22 00:00:00,数据变更失效日期=2099-12-31 00:00:00];　　　___　　　重点业务通信量系数1=[统计月份=202201,核心通信量系数=43177.72];　　　___　　　订购个人增值产品信息2=[订购产品=(120300078274)咪咕视频钻石会员包月15元,失效时间=2023-01-01 00:00:00,受理时间=2022-01-22 11:15:42,数据变更生效日期=2022-02-05 00:00:00,数据变更失效日期=2022-02-20 00:00:00];　　　___　　　订购个人增值产品信息3=[订购受理流水=120300078134444444,订购产品=(120300091217)和彩云超级会员月包,失效时间=2023-01-01 00:00:00,受理时间=2022-01-22 11:15:42,数据变更生效日期=2022-02-05 00:00:00,数据变更失效日期=2022-02-20 00:00:00]}】　　　　　　分档规则名称：5G应用礼包2档立即-首付12元【　　　　　　元素组间关系：全部与　　　　　　元素组信息：{订购个人增值产品信息1=[订购产品=(120300078274)咪咕视频钻石会员包月15元,生效时间=2022-01-01 12:00:03,失效时间=2023-01-01 00:00:00,数据变更生效日期=2022-02-05 00:00:00,数据变更失效日期=2022-02-16 00:00:00,订购受理流水=120300078155555];　　　___　　　汇总账单账目费用信息1=[统计日期=20220205,汇总账单账目编码=(10018037)惠生活铂金会员,汇总账目金额(元)=16.00];　　　___　　　订购个人增值产品信息2=[订购受理流水=120300078166666,订购产品=(1000860005)惠生活铂金会员,生效时间=2022-01-01 12:00:03,失效时间=2023-01-01 00:00:00,数据变更生效日期=2022-01-01 00:00:00,数据变更失效日期=2099-12-31 00:00:00];　　　___　　　汇总账单账目费用信息2=[统计日期=20220205,汇总账单账目编码=(6260070)咪咕视频业务信息费,汇总账目金额(元)=15.00];　　　___　　　订购个人增值产品信息3=[订购受理流水=空,订购产品= 空,生效时间= 空,失效时间= 空,数据变更生效日期= 空,数据变更失效日期= 空]}】";

    public static class RegexMatchItem {
        public String srcStr;
        public String regexStr;
        public String matchStr;
        public int idxStart;
        public int idxEnd;
    }
    public static List<RegexMatchItem> regexFinds(String str, String regex){
        List<RegexMatchItem> ret=new ArrayList<>();
        Pattern patten=Pattern.compile(regex);
        Matcher matcher=patten.matcher(str);
        while (matcher.find()){
            MatchResult result=matcher.toMatchResult();

            RegexMatchItem item=new RegexMatchItem();
            item.srcStr=str;
            item.regexStr=regex;
            item.matchStr=matcher.group();
            item.idxStart= result.start();
            item.idxEnd= result.end();

            ret.add(item);
        }
        return ret;
    }
}
