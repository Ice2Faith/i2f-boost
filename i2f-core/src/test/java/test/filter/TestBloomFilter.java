package test.filter;

import i2f.core.filter.BloomFilter;

/**
 * @author ltb
 * @date 2022/4/27 9:17
 * @desc
 */
public class TestBloomFilter {
    public static void main(String[] args){
        System.out.println(BloomFilter.calcBestBitLen(0.03,100));
        System.out.println(BloomFilter.calcBestHashProviderCount(0.03,100));
        BloomFilter<String> filter=new BloomFilter<>();
        String[] arr={
                "aaa","bbb","aa","bb",
                "aaa","bb","ccc","ccc"
        };
        for(String item : arr){
            boolean ex=filter.exists(item);
            System.out.println("ex:"+ex+" of "+item);
            if(!ex){
                filter.mark(item);
                System.out.println("mk:"+item);
            }
        }
    }
}
