package i2f.core.graphics.d3.triangle.impl;

import i2f.core.graphics.d3.D3Point;
import i2f.core.graphics.d3.D3Vector;
import i2f.core.graphics.d3.data.D3Model;
import i2f.core.graphics.d3.data.D3ModelFlat;
import i2f.core.graphics.d3.triangle.ITrianglize;
import i2f.core.graphics.math.Calc;

import java.util.*;

/**
 * @author ltb
 * @date 2022/6/19 18:25
 * @desc 最短散点距离三角化实现
 */
public class ShortestDistanceTrianglize implements ITrianglize {

    // 使用余弦定理，求出三角形中三个角的角度
    // 滤除掉角度大于angleLimit的三角形，这部分三角形定义为钝角三角形，当钝角达到一定角度，将会趋近直线，三角片面无意义
    public double maxTriangleAngleLimit =90;

    // 是否使用距离规则双向三角化
    public boolean enableDoubleDirectTrianglize =true;

    // 是否包含原有三角片面
    public boolean includeSourceTriangles=true;

    // 是否强制要求从下一层查找三角面
    public boolean enableForceFindInNextLevel =false;

    // 每一层的最小层高
    public double levelMinHeight =20;

    // 每个点需要多少个三角面
    public int pointConstructFlatCount =1;

    // 点集合
    protected List<D3Point> points=new ArrayList<>();

    // 片面中点的索引组合唯一性，保证片面不重复的关键
    protected Set<String> contains=new HashSet<>();

    // 面集合
    protected List<D3ModelFlat> trangles=new ArrayList<>();


    /**
     * 从点中获取距离，用于距离规则排序
     */
    public interface D3PointDistanceRuleValue {
        double axis(D3Point p);
    }

    /**
     * 默认按照Y轴排序
     */
    public D3PointDistanceRuleValue axisValue=new D3PointDistanceRuleValue() {
        @Override
        public double axis(D3Point p) {
            return p.y;
        }
    };

    /**
     * 根据指定下标点，按照排序规则，作为比较器比较以进行排序
     * 这个实现，不需要修改
     */
    protected static class D3PointIndexesReferenceComparator implements Comparator<Integer>{
        public List<D3Point> points=new ArrayList<>();
        public D3PointDistanceRuleValue axisValue;
        @Override
        public int compare(Integer o1, Integer o2) {
            if(axisValue.axis(points.get(o1))==axisValue.axis(points.get(o2))){
                return 0;
            }
            if(axisValue.axis(points.get(o1))<axisValue.axis(points.get(o2))){
                return 1;
            }
            return -1;
        }
    }
    protected D3PointIndexesReferenceComparator indexesComparator=new D3PointIndexesReferenceComparator();

    protected static class IndexDistance
    {
        public int index;
        public double distance;
    };

    // 升序
    protected Comparator<IndexDistance> indexDistanceComparator =new Comparator<IndexDistance>() {
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

    protected boolean filterOutTriangle(D3Point p1, D3Point p2, D3Point p3){
        D3Vector v1=new D3Vector(p1,p2);
        D3Vector v2=new D3Vector(p2,p3);
        D3Vector v3=new D3Vector(p3,p1);

        double la=v1.length();
        double lb=v2.length();
        double lc=v3.length();

        double aa=Calc.angleTriangle(la,lb,lc);
        double ab=Calc.angleTriangle(lb,la,lc);
        double ac=Calc.angleTriangle(lc,la,lb);

        if(aa> maxTriangleAngleLimit || ab> maxTriangleAngleLimit || ac> maxTriangleAngleLimit){
            return true;
        }
        return false;
    }

    protected boolean filterAndSaveTriangle(int i1,int i2,int i3){
        List<Integer> pids=new ArrayList<>();
        // 排除重复用同一个点的情况
        if(i1==i2){
            return false;
        }
        if(i2==i3){
            return false;
        }
        if(i3==i1){
            return false;
        }
        // 加入点索引列表
        pids.add(i1);
        pids.add(i2);
        pids.add(i3);
        // 对三个点索引排序
        pids.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
        // 按照顺序得到唯一性的点ID，达到去重的目的
        String pid=pids.get(0)+"|"+pids.get(1)+"|"+pids.get(2);
        if(contains.contains(pid)){
            return false;
        }

        D3Point p1=points.get(pids.get(0));
        D3Point p2=points.get(pids.get(1));
        D3Point p3=points.get(pids.get(2));

        if(filterOutTriangle(p1,p2,p3)){
            return false;
        }

        // 满足要求的片面，加入最终集合
        trangles.add(new D3ModelFlat(i1,i2,i3));
        contains.add(pid);
        return true;
    }
    @Override
    public D3Model trianglize(D3Model mod) {

        points=new ArrayList<>(mod.points.size());
        points.addAll(mod.points);

        // 对点云按照指定距离规则排序
        // 这里仅排序的是点云的索引，不对点云本身数据顺序做修改
        // 这样有利于，再三角化策略的进行，否则一旦改变原始点云数据顺序
        // 想要再三角化或者在原始三角化基础上再三角化，将会带来困难
        List<Integer> indexes=new ArrayList<>(points.size());
        for (int i = 0; i < points.size(); i++) {
            indexes.add(i);
        }

        indexesComparator.axisValue=axisValue;
        indexesComparator.points=points;
        indexes.sort(indexesComparator);

        // 片面中点的索引组合唯一性，保证片面不重复的关键
        contains=new HashSet<>(points.size()*2);

        // 预申请空间
        trangles=new ArrayList<>(points.size()*3);

        if(includeSourceTriangles) {
            // 载入原始片面，并加入唯一性验证中
            if (mod.flats != null) {
                trangles.addAll(mod.flats);
                for(D3ModelFlat item : mod.flats){
                    filterAndSaveTriangle(item.p1,item.p2,item.p3);
                }
            }
        }

        // 算法分两轮，分别是按照距离规则的正方向三角化和逆方向三角化
        // 目的是，单一方向时，部分点比较特殊，三角化效果不完善

        // 第一轮，按照排序方向正方向进行
        // 由于点已经按照某个距离规则排序
        // 因此对每个点中，依次找大于当前点距离的点，构成三角片面
        // 排序之后，在当前点之后的点，能够保证其距离大于等于当前点
        // 算法的目的就是按照某个排序规则，将上下两层的点，构成三角片面
        int count = points.size();
        for (int i = 0; i < count; i++)
        {
            // 存储之后的点，对于当前点的距离
            List<IndexDistance> sdv=new ArrayList<>(points.size());
            // 计算之后的点和当前点的距离
            int beginJ=i+1;
            if(!enableForceFindInNextLevel){
                beginJ=0;
            }
            for (int j = beginJ; j < count; j++)
            {
                // 排除当前点
                if((int)indexes.get(j)==(int)indexes.get(i)){
                    continue;
                }
                // 排除和当前点疑似在同一层的点
                if(Math.abs(axisValue.axis(points.get(indexes.get(i)))-axisValue.axis(points.get(indexes.get(j))))< levelMinHeight){
                    continue;
                }
                IndexDistance tps=new IndexDistance();
                D3Vector tpv=new D3Vector(points.get(indexes.get(i)), points.get(indexes.get(j)));
                tps.distance = tpv.length();
                tps.index = indexes.get(j);
                sdv.add(tps);
            }
            // 排序，取得最短距离
            sdv.sort(indexDistanceComparator);
            int fcnt=0;
            // 分别取相邻的两个最短点，和当前点构成三角片面
            for(int p=1;p<sdv.size();p++){
                boolean ok=filterAndSaveTriangle(indexes.get(i),sdv.get(p-1).index,sdv.get(p).index);
                // 达到最大片面个数跳出当前点处理
                if(ok) {
                    fcnt++;
                }
                if(fcnt>= pointConstructFlatCount){
                    break;
                }
            }
        }


        if(enableDoubleDirectTrianglize) {
            // 第二轮，按照排序规则逆方向进行
            for (int i = count - 1; i >= 0; i--) {
                List<IndexDistance> sdv = new ArrayList<>(points.size());
                int beginJ=i - 1;
                if(!enableForceFindInNextLevel){
                    beginJ=count - 1;
                }
                for (int j = beginJ; j >= 0; j--) {
                    if ((int) indexes.get(j) == (int) indexes.get(i)) {
                        continue;
                    }
                    if (Math.abs(axisValue.axis(points.get(indexes.get(i))) - axisValue.axis(points.get(indexes.get(j)))) < levelMinHeight) {
                        continue;
                    }
                    IndexDistance tps = new IndexDistance();
                    D3Vector tpv = new D3Vector(points.get(indexes.get(i)), points.get(indexes.get(j)));
                    tps.distance = tpv.length();
                    tps.index = indexes.get(j);
                    sdv.add(tps);
                }
                sdv.sort(indexDistanceComparator);
                int fcnt = 0;
                for (int p = 1; p < sdv.size(); p++) {
                    boolean ok=filterAndSaveTriangle(indexes.get(i),sdv.get(p - 1).index,sdv.get(p).index);
                    if(ok){
                        fcnt++;
                    }
                    if (fcnt >= pointConstructFlatCount) {
                        break;
                    }
                }

            }

        }

        D3Model ret=new D3Model();
        ret.points=points;
        ret.flats=trangles;
        return ret;
    }
}
