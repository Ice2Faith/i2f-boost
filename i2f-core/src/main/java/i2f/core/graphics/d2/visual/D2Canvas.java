package i2f.core.graphics.d2.visual;

import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author ltb
 * @date 2022/6/20 9:44
 * @desc
 */
@Data
public class D2Canvas extends Canvas {
    public BufferedImage img;
    public D2Canvas(BufferedImage img){
        this.img=img;
    }
    public D2Canvas(int width,int height){
        this.img=new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,0,0,null);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

}
