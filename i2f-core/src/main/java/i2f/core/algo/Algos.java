package i2f.core.algo;

/**
 * @author ltb
 * @date 2022/5/21 23:24
 * @desc
 */
public class Algos {
    public static void main(String[] args){
        String str1="when str1[i-1]=str2[j-1] then d[i-1][j-1]";
        String str2="when str1[i-1]!=str2[j-1] then min(d[i-1][j],d[i][j-1],d[i-1][j-1])+1";
        int dis=editDistance(str1,str2);
        System.out.println(dis);
    }
    /**
     * 最短编辑距离问题
     * 用于比较两个字符串str1和str2在经过单字符的插入、删除、替换三种操作的最小次数
     * 使得将str1变换为str2
     * 例如：
     * str1=love
     * str2=lwe
     * 则编辑次数为2
     * 即删除o,将v替换为w
     * 算法描述：
     * 使用二维数组，记录子问题的最优解，知道结束后，得到整体的最优解
     * d[i][j]={
     *     when len2=0 then i
     *     when len1=0 then j
     *     when str1[i-1]=str2[j-1] then d[i-1][j-1]
     *     when str1[i-1]!=str2[j-1] then min(d[i-1][j],d[i][j-1],d[i-1][j-1])+1
     * }
     */
    public static int editDistance(String str1,String str2){
        int str1Len=str1.length();
        int str2Len=str2.length();
        int[][] map=new int[str1Len+1][str2Len+1];
        for(int i=0;i<=str1Len;i++){
            map[i][0]=i;
        }
        for(int i=0;i<=str2Len;i++){
            map[0][i]=i;
        }
        for(int i=1;i<=str1Len;i++){
            for(int j=1;j<=str2Len;j++){
                if(str1.charAt(i-1)==str2.charAt(j-1)){
                    map[i][j]=map[i-1][j-1];
                }else{
                    int temp=Math.min(map[i-1][j],map[i][j-1]);
                    map[i][j]=Math.min(temp,map[i-1][j-1])+1;
                }
            }
        }
        return map[str1Len][str2Len];
    }

    public static<T extends Comparable<T>> T min(T ... objs){
        T ret=objs[0];
        for(T item : objs){
            if(item.compareTo(ret)<0){
                ret=item;
            }
        }
        return ret;
    }
    public static<T extends Comparable<T>> T max(T ... objs){
        T ret=objs[0];
        for(T item : objs){
            if(item.compareTo(ret)>0){
                ret=item;
            }
        }
        return ret;
    }
}
