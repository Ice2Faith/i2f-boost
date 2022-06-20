package i2f.core.graphics.math;

import java.util.List;

/**
 * @author ltb
 * @date 2022/6/20 11:05
 * @desc 矩阵类
 */
public class MatrixInt {
    protected int cols;
    protected int rows;
    protected int[][] data;
    private void prepare(int rows,int cols){
        this.cols =cols;
        this.rows =rows;
        this.data=new int[this.rows][this.cols];
    }
    public MatrixInt(){
        prepare(0,0);
    }
    public MatrixInt(int rows, int cols){
        prepare(rows, cols);
    }
    public MatrixInt(int size){
        prepare(size,size);
    }
    public MatrixInt(int[][] arr){
        int maxWid=0;
        for(int[] item : arr){
            if(item.length>maxWid){
                maxWid=item.length;
            }
        }
        prepare(arr.length,maxWid);
        for(int y=0;y< arr.length;y++){
            int[] line=arr[y];
            for(int x=0;x<line.length;x++){
                data[y][x]=line[x];
            }
        }
    }
    public MatrixInt(int[] arr){
        prepare(1,arr.length);
        for(int x=0;x<arr.length;x++){
            data[0][x]=arr[x];
        }
    }
    public MatrixInt(List<Integer> arr){
        prepare(1,arr.size());
        for(int x=0;x<arr.size();x++){
            data[0][x]=arr.get(x);
        }
    }

    public int cols(){
        return cols;
    }

    public int rows(){
        return rows;
    }

    public int[] row(int r){
        int[] ret=new int[data[r].length];
        for(int i=0;i<data[r].length;i++){
            ret[i]=data[r][i];
        }
        return ret;
    }

    public int[] col(int c){
        int[] ret=new int[data.length];
        for(int i=0;i<data.length;i++){
            ret[i]=data[i][c];
        }
        return ret;
    }

    public int pos(int r,int c){
        return data[r][c];
    }

    public int pos(int r,int c,int val){
        int ret=data[r][c];
        data[r][c]=val;
        return ret;
    }

    @Override
    public String toString() {
        return textMatrix(this);
    }

    public boolean isRegular(){
        return isRegular(this);
    }

    public boolean isDiff(MatrixInt m2){
        return isDiff(this,m2);
    }

    public void assertDiff(MatrixInt m2){
        assertDiff(this,m2);
    }

    public MatrixInt createLike(){
        return createLike(this);
    }

    public MatrixInt transpose(){
        return transpose(this);
    }

    public MatrixInt add(MatrixInt m2){
        return add(this,m2);
    }

    public MatrixInt mul(int val){
        return mul(this,val);
    }

    public MatrixInt mul(MatrixInt m2){
        return mul(this,m2);
    }

    /**
     * 创建单位矩阵
     */
    public static MatrixInt makeE(int size){
        MatrixInt ret=new MatrixInt(size);
        for(int i=0;i<size;i++){
            ret.data[i][i]=1;
        }
        return ret;
    }

    /**
     * 复制矩阵
     * @param m
     * @return
     */
    public static MatrixInt copyOf(MatrixInt m){
        MatrixInt ret=createLike(m);
        for(int y = 0; y<m.rows; y++){
            for(int x = 0; x<m.cols; x++){
                ret.data[y][x]=m.data[y][x];
            }
        }
        return ret;
    }


    /**
     * 每个元素都乘上值
     * @param m
     * @param val
     */
    public static void everyMul(MatrixInt m, int val){
        for(int y = 0; y<m.rows; y++){
            for(int x = 0; x<m.cols; x++){
                m.data[y][x]*=val;
            }
        }
    }

    /**
     * 每个元素都加上值
     * @param m
     * @param val
     */
    public static void everyAdd(MatrixInt m, int val){
        for(int y = 0; y<m.rows; y++){
            for(int x = 0; x<m.cols; x++){
                m.data[y][x]+=val;
            }
        }
    }

    /**
     * 每个元素都设置为值
     * @param m
     * @param val
     */
    public static void everySet(MatrixInt m, int val){
        for(int y = 0; y<m.rows; y++){
            for(int x = 0; x<m.cols; x++){
                m.data[y][x]=val;
            }
        }
    }


    /**
     * 是否是方阵
     * @param m
     * @return
     */
    public static boolean isRegular(MatrixInt m){
        return m.cols==m.rows;
    }

    /**
     * 判断不是同型矩阵
     * @param m1
     * @param m2
     * @return
     */
    public static boolean isDiff(MatrixInt m1, MatrixInt m2){
        if(m1.cols !=m2.cols || m1.rows !=m2.rows){
            return true;
        }
        return false;
    }

    /**
     * 不是同型矩阵直接抛出异常
     * @param m1
     * @param m2
     */
    public static void assertDiff(MatrixInt m1, MatrixInt m2){
        if(isDiff(m1, m2)){
            throw new MatrixDiffrentException();
        }
    }

    /**
     * 创建同型矩阵
     * @param m
     * @return
     */
    public static MatrixInt createLike(MatrixInt m){
        return new MatrixInt(m.rows,m.cols);
    }

    /**
     * 矩阵转置
     * @param m
     * @return
     */
    public static MatrixInt transpose(MatrixInt m){
        MatrixInt ret=new MatrixInt(m.cols,m.rows);
        for(int y = 0; y<m.rows; y++){
            for(int x = 0; x<m.cols; x++){
                ret.data[x][y]=m.data[y][x];
            }
        }
        return ret;
    }

    /**
     * 矩阵相加
     * @param m1
     * @param m2
     * @return
     */
    public static MatrixInt add(MatrixInt m1, MatrixInt m2){
        assertDiff(m1,m2);
        MatrixInt ret=createLike(m1);
        for(int y = 0; y<m1.rows; y++){
            for(int x = 0; x<m1.cols; x++){
                ret.data[y][x]=m1.data[y][x]+m2.data[y][x];
            }
        }
        return ret;
    }

    /**
     * 矩阵与常量相乘
     * @param m
     * @param val
     * @return
     */
    public static MatrixInt mul(MatrixInt m, int val){
        MatrixInt ret=createLike(m);
        for(int y = 0; y<m.rows; y++){
            for(int x = 0; x<m.cols; x++){
                ret.data[y][x]=m.data[y][x]*val;
            }
        }
        return ret;
    }

    /**
     * 矩阵相乘
     * 条件：A矩阵的列的数量等于B矩阵的行的数量
     * 运算规则：A的每一行中的数字对应乘以B的每一列的数字把结果相加起来
     * @param m1
     * @param m2
     * @return
     */
    public static MatrixInt mul(MatrixInt m1, MatrixInt m2){
        if(m1.cols!=m2.rows){
            throw new MatrixDiffrentException();
        }
        MatrixInt ret=new MatrixInt(m1.rows,m2.cols);
        for(int y=0;y<ret.rows;y++){
            for(int x=0;x<ret.cols;x++){
                int sum=0;
                for(int i=0;i<m1.cols;i++){
                    sum+=m1.data[y][i]*m2.data[i][x];
                }
                ret.data[y][x]=sum;
            }
        }
        return ret;
    }

    /**
     *
     * @param m
     * @return
     */
    public static String textMatrix(MatrixInt m){
        StringBuilder builder=new StringBuilder();
        builder.append(m.getClass().getSimpleName()).append("(").append(m.rows).append(",").append(m.cols).append("):\n");
        for(int y = 0; y<m.rows; y++){
            builder.append("[");
            for(int x = 0; x<m.cols; x++){
                if(x!=0){
                    builder.append("\t,");
                }
                builder.append(m.data[y][x]);
            }
            builder.append("]\n");
        }
        return builder.toString();
    }
}
