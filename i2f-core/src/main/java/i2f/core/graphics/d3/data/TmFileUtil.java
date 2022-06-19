package i2f.core.graphics.d3.data;

import i2f.core.graphics.d3.D3Point;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author ltb
 * @date 2022/6/19 17:49
 * @desc TM文件工具
 * TM文件结构
 * 第一行，三个值，分别是：点数量，三角面数量，附加参数
 * 从第二行开始为数据
 * 依次为
 * 点数量行数的点坐标
 * 三角面数量行数的三角面下标
 */
public class TmFileUtil {
    public static D3Model load(File file) throws IOException {
        InputStream is=new FileInputStream(file);
        D3Model mod=load(is);
        is.close();
        return mod;
    }
    public static D3Model load(InputStream is){
        D3Model ret=new D3Model();
        ret.points=new ArrayList<>();
        ret.flats=new ArrayList<>();
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        Scanner scanner=new Scanner(reader);
        int pointsCnt=0,flatsCount=0,argsNum=0;
        // 读取第一行，头信息
        pointsCnt=scanner.nextInt();
        flatsCount=scanner.nextInt();
        argsNum=scanner.nextInt();
        // 重新一次性分配好空间
        ret.points=new ArrayList<>(pointsCnt);
        ret.flats=new ArrayList<>(flatsCount);
        // 读取所有的点
        for(int i=0;i<pointsCnt;i++){
            double px=scanner.nextDouble();
            double py=scanner.nextDouble();
            double pz=scanner.nextDouble();
            ret.points.add(new D3Point(px,py,pz));
        }
        // 读取所有的面
        for(int i=0;i<flatsCount;i++){
            int p1=scanner.nextInt();
            int p2=scanner.nextInt();
            int p3=scanner.nextInt();
            ret.flats.add(new D3ModelFlat(p1,p2,p3));
        }

        return ret;
    }
    public static void save(File file,D3Model mod) throws IOException{
        OutputStream os=new FileOutputStream(file);
        save(os,mod);
        os.close();
    }

    public static void save(OutputStream os,D3Model mod) throws IOException {
        PrintStream print=new PrintStream(os);
        // 写出头信息
        print.printf("%d %d %d\n",mod.points.size(),mod.flats.size(),0);
        // 写出所有点
        for(D3Point item : mod.points){
            print.printf("%f %f %f\n",item.x,item.y,item.z);
        }
        // 写出所有面
        for(D3ModelFlat item : mod.flats){
            print.printf("%d %d %d\n",item.p1,item.p2,item.p3);
        }
        os.flush();
    }
}
