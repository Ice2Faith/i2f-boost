package i2f.core.graphics.d3.shape;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3Vector;
import i2f.core.graphics.d3.data.D3Model;
import i2f.core.graphics.d3.data.D3ModelFlat;
import i2f.core.graphics.math.Calc;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/18 0:11
 * @desc 球体
 */
@Data
@NoArgsConstructor
public class Ball {
    public double radius;

    public Ball( double radius) {
        this.radius = radius;
    }

    public D3Model makeModel(int aAngleCount,int bAngleCount){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();
        for (int i = 0; i < bAngleCount; i++)
        {
            for (int j = 0; j < aAngleCount; j++)
            {
                double x = radius*Math.sin(2 * Calc.PI / aAngleCount* j)*Math.sin(Calc.PI / bAngleCount* i);
                double z = radius*Math.cos(2 * Calc.PI / aAngleCount*j);
                double y = radius*Math.sin(2 * Calc.PI / aAngleCount* j)*Math.cos(Calc.PI / bAngleCount* i);
                ret.points.add(new D3Point(x,y,z));
            }
        }
        return ret;
    }

    //球体递归划分
    public D3Model makeModel(int level){
        double a=radius*1.0/Math.sqrt(1+Math.pow(0.61828,2.0));
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();


        D3Model icosdata=new Icosahedron().makeModel(a);
        List<D3Point> pointsVec=new LinkedList<>();
        List<D3ModelFlat> tranglesVec=new LinkedList<>();

        //数据拷贝
        pointsVec.addAll(icosdata.points);
        tranglesVec.addAll(icosdata.flats);

        for(int n=0;n<level;n++)//进行递归划分
        {
            D3Point[] prePoints=new D3Point[6];//一个三角形三个点加上三个中点存储
            for (int i = 0; i < prePoints.length; i++) {
                prePoints[i]=new D3Point();
            }
            int[] preIndexs=new int[6];//6个点的下标
            int preTranglesCount=tranglesVec.size();
            for(int i=0;i<preTranglesCount;i++)
            {
                //获取原三角的三点坐标和下标
                prePoints[0]=pointsVec.get(tranglesVec.get(0).p1);
                prePoints[1]=pointsVec.get(tranglesVec.get(0).p2);
                prePoints[2]=pointsVec.get(tranglesVec.get(0).p3);
                preIndexs[0]=tranglesVec.get(0).p1;
                preIndexs[1]=tranglesVec.get(0).p2;
                preIndexs[2]=tranglesVec.get(0).p3;

                //计算三中点坐标
                prePoints[3].x=(prePoints[0].x+prePoints[1].x)/2.0;
                prePoints[3].y=(prePoints[0].y+prePoints[1].y)/2.0;
                prePoints[3].z=(prePoints[0].z+prePoints[1].z)/2.0;

                prePoints[4].x=(prePoints[1].x+prePoints[2].x)/2.0;
                prePoints[4].y=(prePoints[1].y+prePoints[2].y)/2.0;
                prePoints[4].z=(prePoints[1].z+prePoints[2].z)/2.0;

                prePoints[5].x=(prePoints[2].x+prePoints[0].x)/2.0;
                prePoints[5].y=(prePoints[2].y+prePoints[0].y)/2.0;
                prePoints[5].z=(prePoints[2].z+prePoints[0].z)/2.0;

                //单位化并重置到应该在的位置
                D3Vector v3=new D3Vector(prePoints[3]);
                v3=v3.unitization();
                prePoints[3].x=radius*v3.x;
                prePoints[3].y=radius*v3.y;
                prePoints[3].z=radius*v3.z;

                D3Vector v4=new D3Vector(prePoints[4]);
                v4=v4.unitization();
                prePoints[4].x=radius*v4.x;
                prePoints[4].y=radius*v4.y;
                prePoints[4].z=radius*v4.z;

                D3Vector v5=new D3Vector(prePoints[5]);
                v5=v5.unitization();
                prePoints[5].x=radius*v5.x;
                prePoints[5].y=radius*v5.y;
                prePoints[5].z=radius*v5.z;

                //添加新算出来的三中点和下标，这里没有去除重复数据，只为了更快获取结果
                preIndexs[3]=pointsVec.size();
                pointsVec.add(prePoints[3]);
                preIndexs[4]=pointsVec.size();
                pointsVec.add(prePoints[4]);
                preIndexs[5]=pointsVec.size();
                pointsVec.add(prePoints[5]);

                //添加一个三角形拆分成的四个三角形
                tranglesVec.add(new D3ModelFlat(preIndexs[0],preIndexs[3],preIndexs[5]));
                tranglesVec.add(new D3ModelFlat(preIndexs[3],preIndexs[1],preIndexs[4]));
                tranglesVec.add(new D3ModelFlat(preIndexs[3],preIndexs[4],preIndexs[5]));
                tranglesVec.add(new D3ModelFlat(preIndexs[5],preIndexs[4],preIndexs[2]));
                //移除原来的大三角形
                tranglesVec.remove(0);
            }

        }

        ret.points.addAll(pointsVec);
        ret.flats.addAll(tranglesVec);

        return ret;
    }
}
