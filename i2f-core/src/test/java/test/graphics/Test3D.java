package test.graphics;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.D3Painter;
import i2f.core.graphics.d3.data.D3Model;
import i2f.core.graphics.d3.projection.ID3Projection;
import i2f.core.graphics.d3.projection.impl.ThreePointProjection;
import i2f.core.graphics.d3.shape.Ball;
import i2f.core.graphics.d3.shape.D3TreeLine;
import i2f.core.graphics.d3.triangle.impl.ShortestDistanceTrianglize;
import i2f.core.graphics.d3.visual.D3Canvas;
import i2f.core.graphics.d3.visual.D3Frame;
import i2f.core.graphics.math.Calc;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/19 0:09
 * @desc
 */
public class Test3D implements D3Frame.OnDraw{
    protected D3Model mod1= null;
    protected D3TreeLine tree=null;
    protected Rgba beginColor=Rgba.rgba(Calc.rand(100),Calc.rand(50),Calc.rand(50),255);
    protected Rgba endColor=Rgba.rgba(Calc.rand(150,255),Calc.rand(150,255),Calc.rand(150,255),255);
    public Test3D() throws IOException {
        List<Point> points= Arrays.asList(
                new Point(20,30),
                new Point(20,35),
                new Point(25,20),
                new Point(35,35),
                new Point(15,10),
                new Point(40,10),
                new Point(50,50),
                new Point(150,150)
        );

//        mod1=new SpinBezierCube(points).makeModelSpinX(15,20);
//        mod1=new Ball(150).makeModel(10,10);
//        mod1=new Cone(80,120).makeModel(10,10,5);
        mod1=new Ball(150).makeModel(4);
//        mod1=new Torus(150,50).makeModel(20,10);
//        mod1=new Cylinder(50,150).makeModel(5,10,3);
//        mod1=new Cuboid(100,150,200).makeModel(3,3,3);
//        tree=D3TreeLine.makeTree(new D3Line(new D3Point(0,0,0),new D3Point(0,80,0)),8);
//        mod1= tree.modelize();
//        mod1=new Icosahedron().makeModel(150);
//        System.out.println(mod1);

        ShortestDistanceTrianglize trianglize=new ShortestDistanceTrianglize();
        trianglize.pointConstructFlatCount=1;
        trianglize.maxTriangleAngleLimit=150;
        trianglize.enableForceFindInNextLevel=false;
//        mod1=trianglize.trianglize(mod1);
////
//        trianglize.axisValue=new ShortestDistanceTrianglize.D3PointDistanceRuleValue() {
//            @Override
//            public double axis(D3Point p) {
//                return p.x;
//            }
//        };
//        mod1=trianglize.trianglize(mod1);
////
//        trianglize.axisValue=new ShortestDistanceTrianglize.D3PointDistanceRuleValue() {
//            @Override
//            public double axis(D3Point p) {
//                return p.z;
//            }
//        };
//        mod1=trianglize.trianglize(mod1);
//

//        mod1= TmFileUtil.load(new File("D:\\IDEA_ROOT\\DevCenter\\i2f-boost\\i2f-core\\src\\test\\java\\test\\graphics\\Bunny.tm"));

    }
    @Override
    public void draw(D3Painter painter, D3Canvas canvas, D3Frame frame) {

        painter.enableBlanking=true;

        painter.clean(Rgba.black());

        painter.setPointColor(Rgba.rgb(255,0,128));
        painter.setLineColor(Rgba.rgb(0,255,128));
        painter.setFillColor(Rgba.white());

        painter.drawAxis(1000);

//        tree.drawTree(painter,beginColor,endColor,10);
        painter.drawModelPoints(mod1);

//        Thread.sleep(3000);
//        painter.drawLine(new D3Point(200,0,0),new D3Point(0,200,0));
//
//        painter.drawLine(new D3Point(0,200,0),new D3Point(0,0,200));
//
//        painter.drawLine(new D3Point(0,0,200),new D3Point(200,0,0));



//        Thread.sleep(5000);



//        Thread.sleep(5000);
//
        painter.drawModelFlats(mod1);
//
//
//        painter.drawModelLines(mod1);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        ThreePointProjection vproj=new ThreePointProjection(false,500,200);
        ID3Projection proj=vproj;

//        proj=new ObliqueProjection(true,Calc.angle2radian(60),Calc.angle2radian(45));

        D3Frame.OnDraw drawer=new Test3D();

        D3Frame frame=new D3Frame(1080,720,drawer,proj);
        frame.getCanvas().painter.enableBlanking=true;
        frame.getCanvas().painter.viewPoint= vproj.viewPoint();
        frame.getCanvas().painter.light.point=vproj.viewPoint().point();
        frame.setVisible(true);
        frame.refresh();

    }
}
