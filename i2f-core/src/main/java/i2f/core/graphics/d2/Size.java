package i2f.core.graphics.d2;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/17 22:49
 * @desc 尺寸
 */
@Data
@NoArgsConstructor
public class Size {
    public double dx;
    public double dy;

    public Size(double dx, double dy){
        this.dx=dx;
        this.dy=dy;
    }

    /**
     * 自适应缩放到目标尺寸
     * 保持宽高比的情况下，自适应缩放到dstSize的尺寸内
     * 适用于，保持宽高比的情况下，显示图片等场景
     * @param srcSize 原始尺寸
     * @param dstSize 目标尺寸
     * @return 应该自适应的尺寸
     */
    public static Size adaptSize(Size srcSize,Size dstSize){
        double pwid=srcSize.dx;
        double phei=srcSize.dy;

        double rateWid=pwid*1.0/ dstSize.dx;
        double rateHei=phei*1.0/dstSize.dy;

        if(pwid/rateHei>dstSize.dx){ // 按照高度缩放，大于目标宽度
            pwid=(int)(pwid/rateWid);
            phei=(int)(phei/rateWid);
        }else{
            pwid=(int)(pwid/rateHei);
            phei=(int)(phei/rateHei);
        }
        return new Size(pwid,phei);
    }

    public Size adaptSize(Size dstSize){
        return adaptSize(this,dstSize);
    }
}
