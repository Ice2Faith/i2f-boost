package i2f.core.graphics.d3.visual;

import i2f.core.graphics.d3.D3Painter;
import lombok.Data;

import java.awt.*;

/**
 * @author ltb
 * @date 2022/6/19 16:41
 * @desc
 */
@Data
public class D3Canvas extends Canvas {
    public D3Painter painter;
    public D3Canvas(D3Painter painter){
        this.painter=painter;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(painter.hdc,0,0,null);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }



}
