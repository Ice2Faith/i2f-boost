package i2f.extension.image.gif;

import i2f.core.graphics.color.Rgba;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/24 17:33
 * @desc
 */
public class Gif {
    public int repeat=0;
    public int width=480;
    public int height=720;
    public int quality=10;
    public Rgba transparentColor = Rgba.rgba(1,0,1,255);
    public List<GifFrame> frames=new ArrayList<>();
}
