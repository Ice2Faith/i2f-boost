package i2f.core.verifycode.core;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/8/15 14:28
 * @desc
 */
public class GraphicsUtil {
    // awt 中，0 度是屏幕垂直向下的
    // 而图形学中，0 度是屏幕水平向右的
    // 也就是说，需要调换x,y轴才行
    public static double awtRadian(double bx, double by, double ex, double ey) {
        return MathUtil.radian(by, bx, ey, ex);
    }


    public static void drawArrow(Graphics2D g, int bx, int by, int ex, int ey) {
        g.drawLine(bx, by, ex, ey);
        double radian = awtRadian(ex, ey, bx, by);

        double dis = MathUtil.distance(bx, by, ex, ey);
        double ldis = dis * 0.15;

        double lradian = radian + Math.toRadians(30);

        double lex = ex + ldis * Math.sin(lradian);
        double ley = ey + ldis * Math.cos(lradian);
        g.drawLine(ex, ey, (int) lex, (int) ley);

        double rradian = radian - Math.toRadians(30);
        double rex = ex + ldis * Math.sin(rradian);
        double rey = ey + ldis * Math.cos(rradian);
        g.drawLine(ex, ey, (int) rex, (int) rey);
    }

    public static void drawArtString(Graphics2D g, String str, int posX, int posY, boolean center) {
        drawArtString(g, str, posX, posY, center,
                GraphicsUtil::defaultArtStringGdiConsumer,
                GraphicsUtil::defaultArtStringTransformConsumer);
    }

    public static void defaultArtStringGdiConsumer(Graphics2D gdi) {
        gdi.setColor(new Color(MathUtil.RANDOM.nextInt(225), MathUtil.RANDOM.nextInt(225), MathUtil.RANDOM.nextInt(225)));
    }

    public static void defaultArtStringTransformConsumer(AffineTransform trans, Graphics2D gdi) {
        trans.rotate((MathUtil.RANDOM.nextDouble() - 0.5) * (Math.PI / 4));
        trans.scale(MathUtil.RANDOM.nextDouble() + 0.5, MathUtil.RANDOM.nextDouble() + 0.5);
        trans.shear(MathUtil.RANDOM.nextDouble() * 0.5, MathUtil.RANDOM.nextDouble() * 0.5);

        int fh = (int) (gdi.getFontMetrics().getHeight());
        trans.translate(0, (MathUtil.RANDOM.nextDouble() - 0.5) * fh);
    }

    public static void drawArtString(Graphics2D g, String str, int posX, int posY,
                                     boolean center,
                                     Consumer<Graphics2D> chGdiConsumer,
                                     BiConsumer<AffineTransform, Graphics2D> chTransConsumer) {
        drawArtString(g, str, posX, posY, center, chGdiConsumer, chTransConsumer, (wid) -> wid * 2);
    }

    public static void drawArtString(Graphics2D g, String str, int posX, int posY,
                                     boolean center,
                                     Consumer<Graphics2D> chGdiConsumer,
                                     BiConsumer<AffineTransform, Graphics2D> chTransConsumer,
                                     Function<Double, Double> incWidthConverter) {
        Font oldFont = g.getFont();
        Color oldColor = g.getColor();
        FontMetrics strMetrics = g.getFontMetrics();
        Rectangle2D strBounds = strMetrics.getStringBounds(str, g);
        if (center) {
            int ox = 0;
            for (int i = 0; i < str.length(); i++) {
                String ch = str.charAt(i) + "";

                FontMetrics metrics = g.getFontMetrics();
                Rectangle2D chBounds = metrics.getStringBounds(ch, g);

                double incWid = chBounds.getWidth();
                if (incWidthConverter != null) {
                    incWid = incWidthConverter.apply(incWid);
                }
                ox += incWid;
            }
            posX = posX - (ox / 2);
            posY = (int) (posY - strBounds.getHeight() / 2 * 1.5);
        }

        int ox = 0;
        for (int i = 0; i < str.length(); i++) {
            String ch = str.charAt(i) + "";

            if (chGdiConsumer != null) {
                chGdiConsumer.accept(g);
            }

            FontMetrics metrics = g.getFontMetrics();
            Rectangle2D chBounds = metrics.getStringBounds(ch, g);

            double px = posX + ox;
            double py = posY;

            double incWid = chBounds.getWidth();
            if (incWidthConverter != null) {
                incWid = incWidthConverter.apply(incWid);
            }
            ox += incWid;

            drawTransformString(g, ch, (int) px, (int) py, chTransConsumer);
        }
        g.setFont(oldFont);
        g.setColor(oldColor);
    }

    public static void drawTransformString(Graphics2D g, String str, int posX, int posY, BiConsumer<AffineTransform, Graphics2D> consumer) {
        drawTransform(g, posX, posY, (gdi) -> {
            int tx = 0;
            int ty = 0;

            FontMetrics metrics = gdi.getFontMetrics();
            Rectangle2D bounds = metrics.getStringBounds(str, g);
            gdi.drawString(str, tx, (int) (ty + bounds.getHeight()));
        }, consumer);
    }

    public static void drawCenterString(Graphics2D g, String str, int posX, int posY, BiConsumer<AffineTransform, Graphics2D> consumer) {
        drawTransform(g, posX, posY, (gdi) -> {
            FontMetrics metrics = gdi.getFontMetrics();
            Rectangle2D bounds = metrics.getStringBounds(str, g);

            int tx = (int) (-bounds.getWidth() / 2);
            int ty = (int) (-bounds.getHeight() / 2 * 1.5);

            gdi.drawString(str, tx, (int) (ty + bounds.getHeight()));
        }, consumer);
    }

    public static void drawTransform(Graphics2D g, int posX, int posY,
                                     Consumer<Graphics2D> drawer) {
        drawTransform(g, posX, posY, drawer, null);
    }

    public static void drawTransform(Graphics2D g, int posX, int posY,
                                     Consumer<Graphics2D> drawer,
                                     BiConsumer<AffineTransform, Graphics2D> transConsumer) {
        AffineTransform oldTrans = g.getTransform();

        AffineTransform trans = new AffineTransform();
        trans.translate(posX, posY);
        if (transConsumer != null) {
            transConsumer.accept(trans, g);
        }
        g.setTransform(trans);

        drawer.accept(g);

        g.setTransform(oldTrans);

    }
}
