package i2f.core.graphics.image.filter.impl;

import i2f.core.graphics.color.Rgba;
import i2f.core.graphics.d2.Point;
import i2f.core.graphics.image.filter.IImageFilter;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/18 0:47
 * @desc 种子颜色替换
 */
public  class SeedReplaceColorImageFilter implements IImageFilter {

    protected int sx;
    protected int sy;
    protected double wrongRate;
    protected Rgba repColor;

    public SeedReplaceColorImageFilter(int sx, int sy, double wrongRate, Rgba repColor) {
        this.sx = sx;
        this.sy = sy;
        this.wrongRate = wrongRate;
        this.repColor = repColor;
    }

    @Override
    public BufferedImage filter(BufferedImage img) {

        BufferedImage simg = img;
        BufferedImage dimg = new BufferedImage(simg.getWidth(), simg.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        // 复制图像
        for (int x = 0; x < simg.getWidth(); x++) {
            for (int y = 0; y < simg.getHeight(); y++) {
                dimg.setRGB(x,y,simg.getRGB(x,y));
            }
        }

        byte[][] map=new byte[simg.getWidth()][simg.getHeight()];

        Rgba cmpColor=Rgba.argb(simg.getRGB(sx,sy));
        List<Point> points = new LinkedList<>();
        Point p = new Point(sx, sy);
        // 八个方向遍历
        int[][] steps ={
                new int[]{-1,-1},
                new int[]{0,-1},
                new int[]{1,-1},
                new int[]{-1,0},
                new int[]{1,0},
                new int[]{-1,1},
                new int[]{0,1},
                new int[]{1,1}
        };
        points.add(p);

        while (points.size()>0)
        {
            Point cur = points.get(0);
            map[(int)cur.x][(int)cur.y]=1;

            for (int j = 0; j < steps.length; j++)
            {
                int nx = (int)cur.x + steps[j][0];
                int ny = (int)cur.y + steps[j][1];

                if (nx < 0 || nx >= simg.getWidth() || ny < 0 || ny >= simg.getHeight())
                {
                    continue;
                }
                if(map[nx][ny]==1){
                    continue;
                }
                Rgba tarColor = Rgba.argb(simg.getRGB(nx, ny));
                boolean isTarget = false;
                if (wrongRate <= 0)
                {
                    if (cmpColor.argb() == tarColor.argb())
                    {
                        isTarget = true;
                    }
                }
                else
                {
                    double rate = Rgba.diff(cmpColor, tarColor);
                    if (rate <= wrongRate)
                    {
                        isTarget = true;
                    }
                }

                if (isTarget)
                {
                    points.add(new Point(nx, ny));
                    dimg.setRGB(nx, ny, repColor.argb());
                    map[nx][ny]=1;
                }
            }

            points.remove(0);
        }

        return dimg;
    }

}
