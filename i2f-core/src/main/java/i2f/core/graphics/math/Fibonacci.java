package i2f.core.graphics.math;

/**
 * @author ltb
 * @date 2022/6/17 23:53
 * @desc 采用缓存方式，进行斐波那契数列求值
 */
public class Fibonacci {
    private static volatile long[] nums=new long[30];
    static {
        for(int i=0;i< nums.length;i++){
            nums[i]=0;
        }
    }
    public static long get(int i){
        if(i<0){
            return -1;
        }
        if(i>=0 && i<nums.length){
            if(nums[i]>0){
                return nums[i];
            }
        }
        if(i==0 || i==1){
            nums[i]=1;
            return nums[i];
        }
        long ret=get(i-1)+get(i-2);
        if(i>=0 && i<nums.length){
            nums[i]=ret;
        }
        return ret;
    }

}
