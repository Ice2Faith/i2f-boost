package i2f.core.graphics.d3;

import i2f.core.graphics.d2.Point;

/**
 * @author ltb
 * @date 2022/6/18 19:29
 * @desc
 */
public class D3VaryUtil {
    //乘4*4三维变换矩阵
    public static D3Point vary(D3Point p,double[][] matrix){
        if(matrix.length<4){
            return p;
        }
        double[] pointVector={p.x,p.y,p.z,1.0};
        double[] resultVector={0,0,0,0};
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if(matrix[i].length<4){
                    return p;
                }
                resultVector[j]+=pointVector[i]*matrix[i][j];
            }
        }
        // 点向量变换前后，都要把向量的第四维度置为1
        for(int i=0;i<4;i++){
            resultVector[i]/=resultVector[3];
        }
        return new D3Point(resultVector[0],resultVector[1],resultVector[2]);
    }


    // 透视投影,世界坐标系转观察坐标系,视径R,z轴角度aAngle，水平夹角：0-2*PI,y轴角度bAngle，垂直夹角：0-PI
    public static D3Point projWorldOrgToViewOrg(boolean useMatrix,D3Point p, double r, double aAngle, double bAngle) {
        if(useMatrix){
            double[][] matrix={
                    { Math.cos(aAngle), -Math.cos(bAngle)*Math.sin(aAngle), -Math.sin(bAngle)*Math.sin(aAngle), 0 },
                    { 0, Math.sin(bAngle), -Math.cos(bAngle), 0 },
                    { -Math.sin(aAngle), -Math.cos(bAngle)*Math.cos(aAngle), -Math.sin(bAngle)*Math.cos(aAngle), 0 },
                    { 0, 0, r, 1 }
            };
            return D3VaryUtil.vary(p,matrix);
        }else{
            double k1 = Math.sin(aAngle);
            double k2 = Math.sin(bAngle);
            double k3 = Math.cos(aAngle);
            double k4 = Math.cos(bAngle);
            double k5 = k2*k3, k6 = k2*k1, k7 = k4*k3, k8 = k4*k1;
            D3Point word = p;
            double x = word.x*k3 - k1*word.z;
            double y = k2*word.y - k8*word.x - k7*word.z;
            double z = r - k6*word.x - k4*word.y - k5*word.z;
            return new D3Point(x,y,z);
        }
    }

    // 观察坐标系转屏幕坐标系，视距d:视点到视心的距离,xoy
    public static Point projViewOrgToScreenOrg(boolean useMatrix, D3Point p, double d) {
        if(useMatrix){
            double[][] matrix={
                    { 1, 0, 0, 0 },
                    { 0, 1, 0, 0 },
                    { 0, 0, 0, 1.0 / d },
                    { 0, 0, 0, 0 }
            };
            D3Point pp= D3VaryUtil.vary(p,matrix);
            return new Point(pp.x,pp.y);
        }else{
            double x = d*p.x / p.z;
            double y = d*p.y / p.z;
            return new Point(x,y);
        }
    }

    /*观察坐标系转具有深度的屏幕坐标系，
	相机镜头的近焦和远焦
	近切面Near：物体离相机的距离，越小越模糊，
	远切面Far：一段指定长度直线的图像在此处变为灭点,大于此值时将会变得模糊,
	修改后为三维屏幕坐标,z标识伪深度
	*/
    public D3Point viewOrgToDeepScreenD3Org(D3Point  p, double d, double Near, double Far)
    {
        double rx = d*(p.x) / (p.z);
        double ry = d*(p.y) / (p.z);
        double rz = Far*(1.0 - Near / (p.z)) / (Far - Near);
        return new D3Point(rx,ry,rz);
    }
}
