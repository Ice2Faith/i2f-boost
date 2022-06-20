package i2f.core.graphics.d2;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.d2.visual.D2Frame;
import i2f.core.graphics.math.Calc;
import lombok.Data;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/20 9:05
 * @desc
 */
@Data
public class CherryTree {
    public static void drawCallback(D2Frame frame){
        BufferedImage img=frame.getCanvas().img;
        CherryTree tree=new CherryTree(img);
        tree.setListener(new OnStepListener() {
            @SneakyThrows
            @Override
            public void onStep(BufferedImage img) {
                frame.refresh();
                Thread.sleep(5);
            }
        });

        Point startPoint=new Point(img.getWidth()/2,img.getHeight());
        Point endPoint=new Point(img.getWidth()/2+Calc.rand(100)-50,img.getHeight()*0.7);
        Line line=new Line(startPoint,endPoint);
        tree.drawTree(12,startPoint,line.direction(),line.length());
    }
    public static void main(String[] args){
        D2Frame frame=new D2Frame(1080,720);

        frame.setVisible(true);

        drawCallback(frame);

    }
    public interface OnStepListener{
        void onStep(BufferedImage img);
    }
    protected BufferedImage img;
    protected OnStepListener listener;
    protected Rgba startColor;
    protected Rgba endColor;
    protected Graphics graphics;
    public CherryTree(int width,int height){
        img=new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
        graphics=img.getGraphics();
    }
    public CherryTree(BufferedImage img){
        this.img=img;
        graphics=img.getGraphics();
    }
    protected void stepListener(){
        if(listener!=null){
            listener.onStep(img);
        }
    }

    public void drawTree(int level,Point startPoint,double direction,double startLen){
        startColor = Rgba.rgba(Calc.rand(100),Calc.rand(100),Calc.rand(100),255);
        endColor = Rgba.rgba(Calc.rand(100,255),Calc.rand(100,255),Calc.rand(100,255),100);
        drawBole(level,level,startPoint, direction, startLen);
        stepListener();
        Point endPoint = D2Calc.directionMove(startPoint, startLen, direction);
        double nlen = nextLen(startLen);
        drawTreeNext(level - 1,level, endPoint, nlen,direction);
        stepListener();
    }

    protected void drawBole(int level,int sumLevel,Point startPoint, double direction, double len){

		/*if (!g_allowDraw){
			keyCheckCallback();
			Sleep(1);
			return;
		}*/

        double cr = 0, cg = 0, cb = 0,ca=255;

        double sr = startColor.r;
        double sg = startColor.g;
        double sb = startColor.b;
        double sa=startColor.a;

        double er = endColor.r;
        double eg = endColor.g;
        double eb = endColor.b;
        double ea=endColor.a;

        double rate = level*1.0 / sumLevel;

        cr = sr*rate + er*(1.0 - rate);
        cg = sg*rate + eg*(1.0 - rate);
        cb = sb*rate + eb*(1.0 - rate);
        ca = sa*rate + ea*(1.0 - rate);

        Rgba color = Rgba.rgba((int)cr,(int)cg,(int)cb,(int)ca);

        int width = (int)Math.log((double)level);
        if (width < 1){
            width = 1;
        }
        //HPEN pen = CreatePen(0, width,color );
        //HGDIOBJ oldpen=SelectObject(hdc, pen);
        Point endPoint = D2Calc.directionMove(startPoint, len, direction);
        Point[] points=new Point[4];
        points[0] = startPoint;
        points[3] = endPoint;

        for (int i = 1; i < 3; i++){

            double qlen = (Calc.rand() % (50-10*i) + (30+10*i))*1.0 / 100 * len;
            double qdirect = nextDirection(direction);
            Point qend = D2Calc.directionMove(startPoint, qlen, qdirect);
            //Ellipse(hdc, qend.x - 3, qend.y - 3, qend.x + 3, qend.y + 3);
            points[i] = qend;
        }

        List<Point> samples = Bezier.samples(Arrays.asList(points));
        for(Point item : samples){
            int dx=(int)item.x;
            int dy=(int)item.y;
            if(dx<0 || dy<0 || dx>=img.getWidth() || dy>=img.getHeight()){
                continue;
            }
            if(width<=1){
                img.setRGB(dx,dy,color.argb());
            }else{
                graphics.setColor(new Color(color.argb(),true));
                graphics.fillOval(dx,dy,(int)width,(int)width);
            }
        }
        stepListener();
    }
    protected double nextLen(double len){
        return len * ((Calc.rand()%25+70)*1.0/100);
    }
    protected double nextDirection(double startDirection){
        double fac = Calc.angle2radian(Calc.rand() % 60 - 60);
        if (Calc.rand() % 100 < 50){
            fac = 0.0 - fac;
        }
        return startDirection + fac;
    }
    protected void drawTreeNext(int level,int sumLevel,Point startEnd,double startLen,double startDirection){
        if (level <= 0){
            return;
        }
        int boleCount =  Calc.rand() % 2 + 2;
        for (int i = 0; i < boleCount; i++){
            double ndirect = nextDirection(startDirection);
            drawBole(level,sumLevel,startEnd, ndirect, startLen);

            double rate = level*1.0 / sumLevel;
//            if (Calc.rand() % 100 < (1.0 - rate) * 30){
//                continue;
//            }
            Point endPoint = D2Calc.directionMove(startEnd, startLen, ndirect);
            double nlen = nextLen(startLen);
            drawTreeNext(level - 1,sumLevel, endPoint, nlen, ndirect);
        }
    }
}
