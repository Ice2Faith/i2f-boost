package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.d2.Point;
import i2f.core.graphics.image.filter.IImageFilter;
import i2f.core.graphics.math.Calc;

import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 图像矩形校正
 */
public  class RectangleNormalizeImageFilter implements IImageFilter {
    // 顺时针环绕的变形矩形四个顶点
    protected Point[] points;
    protected int nwid;
    protected int nhei;


    public RectangleNormalizeImageFilter(Point[] points, int nwid, int nhei) {
        this.points = points;
        this.nwid = nwid;
        this.nhei = nhei;
    }

    public RectangleNormalizeImageFilter(Point[] points) {
        this.points = points;
        double minx = Calc.min(points[0].x,points[1].x,points[2].x,points[3].x);
        double miny = Calc.min(points[0].y,points[1].y,points[2].y,points[3].y);
        double maxx = Calc.max(points[0].x,points[1].x,points[2].x,points[3].x);
        double maxy = Calc.max(points[0].y,points[1].y,points[2].y,points[3].y);
        nwid = (int)Calc.abs(maxx - minx);
        nhei = (int)Calc.abs(maxy - miny);
    }

    @Override
    public BufferedImage filter(BufferedImage img) {
        if (nwid <= 0)
        {
            nwid = img.getWidth();
        }
        if (nhei <= 0)
        {
            nhei = img.getHeight();
        }
        BufferedImage simg = img;
        BufferedImage dimg = new BufferedImage(nwid,nhei,BufferedImage.TYPE_4BYTE_ABGR);

        Point leftTop = points[0];
        Point rightTop = points[1];
        Point rightDown = points[2];
        Point leftDown = points[3];

        for (int i = 0; i < nhei; i++)
        {
            for (int j = 0; j < nwid; j++)
            {
                double ratex = j * 1.0 / nwid;
                double ratey = i * 1.0 / nhei;
                Point toptp = getLineRatePoint(leftTop, rightTop, ratex);
                Point downtp = getLineRatePoint(leftDown, rightDown, ratex);
                Point target = getLineRatePoint(toptp, downtp, ratey);

                int tx=(int)target.x;
                int ty=(int)target.y;
                double rx=target.x-tx;
                double ry=target.y-ty;
                Rgba color = Rgba.argb(simg.getRGB(tx,ty));
                if(tx+1<simg.getWidth()){
                    Rgba cx=Rgba.argb(simg.getRGB(tx+1,ty));
                    color.r=(int)(color.r*(1.0-rx)+cx.r*rx);
                    color.g=(int)(color.g*(1.0-rx)+cx.g*rx);
                    color.b=(int)(color.b*(1.0-rx)+cx.b*rx);
                    color.a=(int)(color.a*(1.0-rx)+cx.a*rx);
                }
                if(ty+1<simg.getHeight()){
                    Rgba cy=Rgba.argb(simg.getRGB(tx,ty+1));
                    color.r=(int)(color.r*(1.0-ry)+cy.r*ry);
                    color.g=(int)(color.g*(1.0-ry)+cy.g*ry);
                    color.b=(int)(color.b*(1.0-ry)+cy.b*ry);
                    color.a=(int)(color.a*(1.0-ry)+cy.a*ry);
                }

                dimg.setRGB(j, i, color.argb());
            }
        }

        return dimg;
    }

    public static Point getLineRatePoint(Point sp, Point ep, double rate)
    {
        Point ret = new Point(0, 0);
        double dx = ep.x - sp.x;
        double dy = ep.y - sp.y;
        ret.x = (float)(sp.x + dx * rate);
        ret.y = (float)(sp.y + dy * rate);
        return ret;
    }
}
