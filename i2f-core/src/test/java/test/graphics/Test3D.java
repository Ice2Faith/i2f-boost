package test.graphics;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d3.D3Painter;
import i2f.core.graphics.d3.data.D3Model;
import i2f.core.graphics.d3.projection.ID3Projection;
import i2f.core.graphics.d3.projection.impl.ThreePointProjection;
import i2f.core.graphics.d3.shape.Torus;
import i2f.core.graphics.d3.triangle.ITrianglize;
import i2f.core.graphics.d3.triangle.impl.ShortestDistanceTrianglize;
import i2f.core.graphics.d3.visual.D3Canvas;
import i2f.core.graphics.d3.visual.D3Frame;

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

//        mod1=new SpinBezierCube(points).makeModelSpinX(300,40);
//        mod1=new Ball(150).makeModel(20,20);
//        mod1=new Cone(80,120).makeModel(60,120,30);
//        mod1=new Ball(150).makeModel(3);
        mod1=new Torus(150,50).makeModel(60,60);
//        System.out.println(mod1);

        ITrianglize trianglize=new ShortestDistanceTrianglize();
        mod1=trianglize.trianglize(mod1);

//        mod1= TmFileUtil.load(new File("D:\\IDEA_ROOT\\DevCenter\\i2f-boost\\i2f-core\\src\\test\\java\\test\\graphics\\Bunny.tm"));

    }
    @Override
    public void draw(D3Painter painter, D3Canvas canvas, D3Frame frame) {


        painter.clean(Rgba.black());

        painter.setPointColor(Rgba.rgb(255,0,128));
        painter.setLineColor(Rgba.rgb(255,255,128));
        painter.setFillColor(Rgba.white());

        painter.drawAxis(1000);

        painter.drawModelPoints(mod1);

//        Thread.sleep(3000);
//        painter.drawLine(new D3Point(200,0,0),new D3Point(0,200,0));
//
//        painter.drawLine(new D3Point(0,200,0),new D3Point(0,0,200));
//
//        painter.drawLine(new D3Point(0,0,200),new D3Point(200,0,0));



//        Thread.sleep(5000);

        painter.drawModelLines(mod1);

//        Thread.sleep(5000);
//
//        painter.drawModelFlats(mod1);
//
//
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        ID3Projection proj=new ThreePointProjection(false,500,200);


        D3Frame.OnDraw drawer=new Test3D();

        D3Frame frame=new D3Frame(1080,720,drawer,proj);
        frame.setVisible(true);
        frame.refresh();

    }
}
