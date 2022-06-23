package i2f.core.graphics.d3.shape;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.d3.*;
import i2f.core.graphics.d3.data.D3Model;
import i2f.core.graphics.d3.data.D3ModelFlat;
import i2f.core.graphics.math.Calc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/23 14:13
 * @desc 绘制一颗3D的树干
 */
public class D3TreeLine {
    public static class TreeBole{
        public D3Line line;
        public int level;
        public int maxLevel;

        public TreeBole(D3Line line, int level, int maxLevel) {
            this.line = line;
            this.level = level;
            this.maxLevel = maxLevel;
        }
    }
    public List<TreeBole> lines=new ArrayList<>();

    public void drawTree(D3Painter painter, Rgba beginColor, Rgba endColor, double beginWidth){
        for(TreeBole item : lines){
            double rate=item.level*1.0/ item.maxLevel;
            double rrate=1.0-rate;
            double r=beginColor.r*rate+endColor.r*rrate;
            double g=beginColor.g*rate+endColor.g*rrate;
            double b=beginColor.b*rate+endColor.b*rrate;
            double a=beginColor.r*rate+endColor.a*rrate;
            Rgba c=Rgba.rgba((int)r,(int)g,(int)b,(int)a);
            double wid=beginWidth*rate;
            painter.lineColor=c;
            painter.drawLine(item.line);
        }
    }

    /**
     * 转换为一个三维模型，但是三角面就是一条直线
     * @return
     */
    public D3Model modelize(){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();
        for(TreeBole bole : lines){
            D3Line item=bole.line;
            ret.points.add(item.begin);
            ret.points.add(item.end);

            ret.flats.add(new D3ModelFlat(ret.points.size()-2,ret.points.size()-1,ret.points.size()-2));
        }

        return ret;
    }
    public static D3TreeLine makeTree(D3Line begin,int level){
        D3TreeLine ret=new D3TreeLine();
        ret.lines=new ArrayList<>(1024);
        TreeBole bole=new TreeBole(begin,level,level);
        ret.lines.add(bole);
        makeTreeNext(ret.lines,bole);
        return ret;
    }
    protected static void makeTreeNext(List<TreeBole> lines,TreeBole bole){
        if(bole.level<0){
            return;
        }
        D3Line begin=bole.line;
        D3Point end=begin.end;
        D3Vector v=new D3Vector(begin);
        D3SphericalPoint sp = v.spherical();
        int count= Calc.rand(2,5);
        for(int i=0;i<count;i++){
            if(Calc.randPercent()<0.3*(1.0-(bole.level*1.0/bole.maxLevel))){ // 随机裁剪
                continue;
            }
            double nlen=begin.length()/2*Calc.randPercent()+begin.length()/2; // >0.5*len
            double nag= sp.aAngle+Calc.angle2radian(Calc.rand(-60,60));
            double nbg=sp.bAngle+Calc.angle2radian(Calc.rand(-60,60));
            D3SphericalPoint off=new D3SphericalPoint(nlen,nag,nbg);
            D3Point npoff=off.point();
            D3Point nend=new D3Point(end.x+npoff.x,end.y+npoff.y,end.z+npoff.z);
            D3Line nline=new D3Line(end,nend);
            lines.add(new TreeBole(nline, bole.level, bole.maxLevel));
            makeTreeNext(lines,new TreeBole(nline, bole.level-1, bole.maxLevel));
        }
    }
}
