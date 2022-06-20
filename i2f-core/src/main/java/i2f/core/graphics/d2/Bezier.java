package i2f.core.graphics.d2;

import i2f.core.graphics.math.Calc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/18 23:52
 * @desc
 */
public class Bezier {
    public static double bernstein(int idx, int cnt, double rate)
    {
        double cni=1,tp=1;
        for(int j=1;j<=idx;j++)
        {
            if(cnt-j+1>0) {
                cni *= cnt - j + 1;
            }
            tp*=j;
        }
        cni=cni/tp;
        return cni*Math.pow(rate,idx)*Math.pow(1-rate,cnt-idx);
    }
    public static Point bezierPoint(double rate, List<Point> points)
    {
        double sumX = 0,sumY=0;
        for (int i = 0; i<points.size() ; i++)
        {
            double brate=bernstein(i, points.size()-1, rate);
            sumX += points.get(i).x* brate;
            sumY += points.get(i).y* brate;

        }
        return new Point(sumX,sumY);
    }

    public static List<Point> resamples(List<Point> points, int resampleCount){
        List<Point> ret=new ArrayList<>(resampleCount);
        for (int i = 0; i < resampleCount; i++)
        {
            Point p= Bezier.bezierPoint(1.0*i / resampleCount,points);
            ret.add(p);
        }
        return ret;
    }

    public static List<Point> samples(List<Point> points){
        double len=0;
        for(int i=1;i<points.size();i++){
            Point pre=points.get(i-1);
            Point cur=points.get(i);
            len+= Calc.abs(cur.x-pre.x)+Calc.abs(cur.y-pre.y);
        }
        len*=1.2;
        int count=(int)len;
        return resamples(points,count);
    }

}
