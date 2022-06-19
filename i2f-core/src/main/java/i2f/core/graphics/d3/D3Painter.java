package i2f.core.graphics.d3;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.d2.Point;
import i2f.core.graphics.d2.projection.IProjection;
import i2f.core.graphics.d2.projection.impl.MathCenterProjection;
import i2f.core.graphics.d3.data.D3Model;
import i2f.core.graphics.d3.data.D3ModelFlat;
import i2f.core.graphics.d3.projection.ID3Projection;
import i2f.core.graphics.d3.transform.ID3Transform;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/19 15:26
 * @desc 3D绘图类
 */
public class D3Painter {
    public BufferedImage hdc;
    public ID3Projection d3proj;
    public IProjection d2proj;
    public Rgba pointColor;
    public Rgba lineColor;
    public Rgba fillColor;
    protected boolean disableTransform=false;
    public List<ID3Transform> transforms=new ArrayList<>();

    public D3Painter(BufferedImage hdc, ID3Projection d3proj, IProjection d2proj) {
        this.hdc = hdc;
        this.d3proj = d3proj;
        this.d2proj = d2proj;
    }

    public D3Painter(int width,int height,ID3Projection d3proj){
        this.hdc=new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
        this.d3proj=d3proj;
        this.d2proj= new MathCenterProjection(width,height);
    }

    public D3Painter(BufferedImage hdc,ID3Projection d3proj){
        this.hdc=hdc;
        this.d3proj=d3proj;
        this.d2proj=new MathCenterProjection(hdc.getWidth(),hdc.getHeight());
    }

    public Graphics getD2(){
        return hdc.getGraphics();
    }

    public int width(){
        return hdc.getWidth();
    }

    public int height(){
        return hdc.getHeight();
    }

    public D3Painter clean(){
        Graphics d2=getD2();
        d2.setColor(new Color(fillColor.argb(),true));
        d2.fillRect(0,0,hdc.getWidth(),hdc.getHeight());
        return this;
    }

    public D3Painter clean(Rgba bkColor){
        Rgba bakFillColor=fillColor;
        fillColor=bkColor;
        clean();
        fillColor=bakFillColor;
        return this;
    }

    /**
     * 使用直接点绘制，在长度超出视距时不会发生翻转，但是绘制效果不好
     * @param length
     * @return
     */
    public D3Painter drawAxis(double length){

        Rgba bakPointColor=pointColor;
        boolean bakDisableTransform=disableTransform;
        disableTransform=true;
        Rgba[] cls={Rgba.red(),Rgba.green(),Rgba.blue()};
        for(double d=0;d<length;d+=1){
            D3Point[] pts={new D3Point(d,0,0),new D3Point(0,d,0),new D3Point(0,0,d)};
            for(int i=0;i<3;i++){
                setPointColor(cls[i]);
                drawPoint(pts[i]);
            }
        }
        disableTransform=bakDisableTransform;
        pointColor=bakPointColor;

        return this;
    }

    /**
     * 使用2D库绘制，在超出视距时会发生翻转，但是绘制效果好
     * @param length
     * @return
     */
    public D3Painter drawAxisLine(double length){
        Rgba bakLineColor=lineColor;
        boolean bakDisableTransform=disableTransform;
        disableTransform=true;
        lineColor=Rgba.red();
        drawLine(new D3Point(0,0,0),new D3Point(length,50,0));

        lineColor=Rgba.green();
        drawLine(new D3Point(0,0,0),new D3Point(0,length,50));

        lineColor=Rgba.blue();
        drawLine(new D3Point(0,0,0),new D3Point(50,0,length));
        disableTransform=bakDisableTransform;
        lineColor=bakLineColor;

        return this;
    }

    public BufferedImage getHdc() {
        return hdc;
    }

    public D3Painter setHdc(BufferedImage hdc) {
        this.hdc = hdc;
        this.d2proj.onSizeChange(this.hdc.getWidth(),this.hdc.getHeight());
        return this;
    }

    public ID3Projection getD3proj() {
        return d3proj;
    }

    public D3Painter setD3proj(ID3Projection d3proj) {
        this.d3proj = d3proj;
        return this;
    }

    public IProjection getD2proj() {
        return d2proj;
    }

    public D3Painter setD2proj(IProjection d2proj) {
        this.d2proj = d2proj;
        return this;
    }

    public Rgba getPointColor() {
        return pointColor;
    }

    public D3Painter setPointColor(Rgba pointColor) {
        this.pointColor = pointColor;
        return this;
    }

    public Rgba getLineColor() {
        return lineColor;
    }

    public D3Painter setLineColor(Rgba lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    public Rgba getFillColor() {
        return fillColor;
    }

    public D3Painter setFillColor(Rgba fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    /**
     * 三维坐标转为显示坐标
     * @param p
     * @return
     */
    public Point d3p2hdcp(D3Point p){
        D3Point np=new D3Point(p.x,p.y,p.z);
        if(!disableTransform){
            for(ID3Transform item : transforms){
                np=item.transform(np);
            }
        }
        Point p2=d3proj.projection(np);
        Point hp=d2proj.projection(p2);
        return hp;
    }

    public D3Painter drawPoint(D3Point p){
        Point hp=d3p2hdcp(p);
        int dx=(int)hp.x;
        int dy=(int)hp.y;
        if(dx<0 || dy<0 || dx>=hdc.getWidth() || dy>=hdc.getHeight()){
            return this;
        }
        hdc.setRGB(dx,dy,pointColor.argb());
        return this;
    }

    public D3Painter drawLine(D3Point begin,D3Point end){
        return drawLine(new D3Line(begin,end));
    }

    public D3Painter drawLine(D3Line l){
        Point begin=d3p2hdcp(l.begin);
        Point end=d3p2hdcp(l.end);
        Graphics gra=hdc.getGraphics();
        gra.setColor(new Color(lineColor.argb(),true));
        gra.drawLine((int)begin.x,(int)begin.y,(int)end.x,(int)end.y);
        return this;
    }

    public D3Painter drawFlatLine(D3Flat flat){
        return drawPolygon(Arrays.asList(flat.p1,flat.p2,flat.p3));
    }


    public D3Painter drawPolyline(List<D3Point> points){
        Graphics gra=hdc.getGraphics();
        gra.setColor(new Color(lineColor.argb(),true));
        List<Point> dps=new ArrayList<>(points.size());
        for(D3Point item : points){
            dps.add(d3p2hdcp(item));
        }
        int[] xs=new int[dps.size()];
        int[] ys=new int[dps.size()];
        for(int i=0;i<xs.length;i++){
            xs[i]=(int)dps.get(i).x;
            ys[i]=(int)dps.get(i).y;
        }
        gra.drawPolyline(xs,ys,xs.length);
        return this;
    }

    public D3Painter drawFlat(D3Flat flat){
        return drawFillPolygon(Arrays.asList(flat.p1,flat.p2,flat.p3));
    }

    public D3Painter drawPolygon(List<D3Point> points){
        Graphics gra=hdc.getGraphics();
        gra.setColor(new Color(lineColor.argb(),true));
        List<Point> dps=new ArrayList<>(points.size());
        for(D3Point item : points){
            dps.add(d3p2hdcp(item));
        }
        int[] xs=new int[dps.size()];
        int[] ys=new int[dps.size()];
        for(int i=0;i<xs.length;i++){
            xs[i]=(int)dps.get(i).x;
            ys[i]=(int)dps.get(i).y;
        }
        gra.drawPolygon(xs,ys,xs.length);
        return this;
    }

    public D3Painter drawFillPolygon(List<D3Point> points){
        Graphics gra=hdc.getGraphics();
        gra.setColor(new Color(fillColor.argb(),true));
        List<Point> dps=new ArrayList<>(points.size());
        for(D3Point item : points){
            dps.add(d3p2hdcp(item));
        }
        int[] xs=new int[dps.size()];
        int[] ys=new int[dps.size()];
        for(int i=0;i<xs.length;i++){
            xs[i]=(int)dps.get(i).x;
            ys[i]=(int)dps.get(i).y;
        }
        gra.fillPolygon(xs,ys,xs.length);
        return this;
    }

    public D3Painter drawModelPoints(D3Model mod){
        for(D3Point item : mod.getPoints()){
            drawPoint(item);
        }
        return this;
    }

    public D3Painter drawModelLines(D3Model mod){
        for(D3ModelFlat item : mod.getFlats()){
            D3Flat flat=new D3Flat();
            flat.p1=mod.getPoints().get(item.p1);
            flat.p2=mod.getPoints().get(item.p2);
            flat.p3=mod.getPoints().get(item.p3);
            drawFlatLine(flat);
        }
        return this;
    }

    public D3Painter drawModelFlats(D3Model mod){
        for(D3ModelFlat item : mod.getFlats()){
            D3Flat flat=new D3Flat();
            flat.p1=mod.getPoints().get(item.p1);
            flat.p2=mod.getPoints().get(item.p2);
            flat.p3=mod.getPoints().get(item.p3);
            drawFlat(flat);
        }
        return this;
    }
}
