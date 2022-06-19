package i2f.core.graphics.d3.triangle.impl;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3Vector;
import i2f.core.graphics.d3.data.D3Model;
import i2f.core.graphics.d3.data.D3ModelFlat;
import i2f.core.graphics.d3.triangle.ITrianglize;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/19 18:25
 * @desc 最短散点距离三角化实现
 */
public class ShortestDistanceTrianglize implements ITrianglize {
    // 降序
    public Comparator<D3Point> comparator=new Comparator<D3Point>() {
        @Override
        public int compare(D3Point o1, D3Point o2) {
            if(o1.x==o2.x){
                return 0;
            }
            if(o1.x>o2.x){
                return 1;
            }
            return -1;
        }
    };

    public static class IndexDistance
    {
        public int index;
        public double distance;
    };

    // 升序
    public Comparator<IndexDistance> indexDistanceComparator =new Comparator<IndexDistance>() {
        @Override
        public int compare(IndexDistance o1, IndexDistance o2) {
            if(o1.distance==o2.distance){
                return 0;
            }
            if(o1.distance>o2.distance){
                return 1;
            }
            return -1;
        }
    };
    @Override
    public D3Model trianglize(D3Model mod) {
        List<D3Point> points=new ArrayList<>(mod.points.size());
        points.addAll(mod.points);
        points.sort(comparator);

        List<D3ModelFlat> trangles=new ArrayList<>();
        int count = points.size();
        for (int i = 0; i < count-1; i++)
        {
            int lowCount = 0;
            List<IndexDistance> sdv=new ArrayList<>();
            IndexDistance self=new IndexDistance();
            self.distance = -1;
            self.index = i;
            sdv.add(self);
            for (int j = i+1; j < count; j++)
            {
                if (points.get(j).y == points.get(i).y) {
                    continue;
                }
                IndexDistance tps=new IndexDistance();
                D3Vector tpv=new D3Vector(points.get(i), points.get(j));
                tps.distance = tpv.length();
                tps.index = j;
                sdv.add(tps);
                lowCount++;
            }
            sdv.sort(indexDistanceComparator);
            if (lowCount >= 4)
            {
                D3ModelFlat dt1=new D3ModelFlat(sdv.get(0).index, sdv.get(1).index, sdv.get(2).index);
                D3ModelFlat dt2=new D3ModelFlat(sdv.get(0).index, sdv.get(2).index, sdv.get(3).index);
                D3ModelFlat dt3=new D3ModelFlat(sdv.get(0).index, sdv.get(3).index, sdv.get(1).index);
                D3ModelFlat dt4=new D3ModelFlat(sdv.get(1).index, sdv.get(2).index, sdv.get(3).index);
                trangles.add(dt1);
                trangles.add(dt2);
                trangles.add(dt3);
                trangles.add(dt4);
            }
            else if (lowCount == 3)
            {
                D3ModelFlat dt1=new D3ModelFlat(sdv.get(0).index, sdv.get(1).index, sdv.get(2).index);
                trangles.add(dt1);
            }
            else if (lowCount == 2)
            {
                D3ModelFlat dt1=new D3ModelFlat(sdv.get(0).index, sdv.get(1).index, sdv.get(0).index);
                trangles.add(dt1);
            }
            else if(lowCount==1)
            {
                D3ModelFlat dt1=new D3ModelFlat(sdv.get(0).index, sdv.get(0).index, sdv.get(0).index);
                trangles.add(dt1);
            }
        }

        D3Model ret=new D3Model();
        ret.points=points;
        ret.flats=trangles;
        return ret;
    }
}
