package i2f.core.graphics.d2.polygon;

import i2f.core.graphics.d2.Point;

import java.util.List;

/**
 * @author ltb
 * @date 2022/7/31 22:03
 * @desc 多边形顺时针逆时针判断工具
 * 来源：https://www.cnblogs.com/sparkleDai/p/7604954.html?share_token=6a0191bc-27a6-400f-8a38-794776bc1064
 * 判断多边形是顺时针还是逆时针的方法
 *
 * 1、关于如何判定多边形是顺时针还是逆时针对于凸多边形而言，只需对某一个点计算cross product = ((xi - xi-1),(yi - yi-1)) x ((xi+1 - xi),(yi+1 - yi))
 * = (xi - xi-1) * (yi+1 - yi) - (yi - yi-1) * (xi+1 - xi)
 *
 * 如果上式的值为正，逆时针；为负则是顺时针
 *
 * 而对于一般的简单多边形，则需对于多边形的每一个点计算上述值，如果正值比较多，是逆时针；负值较多则为顺时针。
 */
public class PolygonClockDirectionTool {

    /**
     * 判断多边形是否是顺时针
     * @param points 多边形，至少三个点
     * @param upAxisY 是否是Y轴向上的坐标系
     * @return 0 无法判断，1 顺时针， -1 逆时针
     */
    public static int isClockwise(List<Point> points, boolean upAxisY){
        if(points==null){
            return 0;
        }
        int size=points.size();
        if(size<3){
            return 0;
        }
        int yTrans=upAxisY?1:-1;
        int count=0;
        for(int i=0;i<size;i++){
            int j=(i+1)%size;
            int k=(i+2)%size;

            double cp=(points.get(j).getX()-points.get(i).getX())
                    *(points.get(k).getY()*yTrans-points.get(j).getY()*yTrans)
                    -(points.get(j).getY()*yTrans-points.get(i).getY()*yTrans)
                    *(points.get(k).getX()-points.get(j).getX());

            if(cp<0){
                count--;
            }else{
                count++;
            }
        }
        if(count>0){
            return 1;
        }else if(count<0){
            return -1;
        }
        return 0;
    }

    /**
     * 判断是否是凸多边形
     * @param points 多边形，至少三个点
     * @param upAxisY 是否是Y轴向上的坐标系
     * @return 0 无法判断，1 凸多边形， -1 凹多边形
     */
    public static int isConvex(List<Point> points,boolean upAxisY){
        if(points==null){
            return 0;
        }
        int size=points.size();
        if(size<3){
            return 0;
        }
        int yTrans=upAxisY?1:-1;
        int flag=0;
        for(int i=0;i<size;i++){
            int j=(i+1)%size;
            int k=(i+2)%size;

            double cp=(points.get(j).getX()-points.get(i).getX())
                    *(points.get(k).getY()*yTrans-points.get(j).getY()*yTrans)
                    -(points.get(j).getY()*yTrans-points.get(i).getY()*yTrans)
                    *(points.get(k).getX()-points.get(j).getX());

            if(cp<0){
                flag |=1;
            }else{
                flag|=2;
            }
            if(flag==3){
                return -1;
            }
        }
        if(flag!=0){
            return 1;
        }
        return 0;
    }
}
