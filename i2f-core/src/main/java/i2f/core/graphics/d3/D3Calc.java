package i2f.core.graphics.d3;

/**
 * @author ltb
 * @date 2022/6/26 16:05
 * @desc
 */
public class D3Calc {
    /**
     * 将一个点按照direction方向移动length距离
     * @param startPoint
     * @param length
     * @param aAngle
     * @param bAngle
     * @return
     */
    public static D3Point directionMove(D3Point startPoint, double length, double aAngle,double bAngle)
    {
        D3SphericalPoint off=new D3SphericalPoint(length,aAngle,bAngle);
        D3Point npoff=off.point();
        return new D3Point(startPoint.x+npoff.x,startPoint.y+npoff.y,startPoint.z+npoff.z);
    }

    /**
     * 分别在x/y方向移动指定的距离
     * @param startPoint
     * @param dx
     * @param dy
     * @return
     */
    public static D3Point offsetMove(D3Point startPoint, double dx, double dy,double dz){
        return new D3Point(startPoint.x+dx,startPoint.y+dy,startPoint.z+dz);
    }

}
