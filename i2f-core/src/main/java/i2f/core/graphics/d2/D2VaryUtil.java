package i2f.core.graphics.d2;

/**
 * @author ltb
 * @date 2022/6/21 14:57
 * @desc 2D变换工具类
 */
public class D2VaryUtil {
    //乘3*3二维变换矩阵
    public static Point vary(Point p, double[][] matrix){
        if(matrix.length<3){
            return p;
        }
        double[] pointVector={p.x,p.y,1.0};
        double[] resultVector={0,0,0,0};
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(matrix[i].length<3){
                    return p;
                }
                resultVector[j]+=pointVector[i]*matrix[i][j];
            }
        }
        // 点向量变换前后，都要把向量的第四维度置为1
        for(int i=0;i<3;i++){
            resultVector[i]/=resultVector[2];
        }
        return new Point(resultVector[0],resultVector[1]);
    }
}
