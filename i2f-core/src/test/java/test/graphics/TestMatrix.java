package test.graphics;

import i2f.core.graphics.math.Matrix;

/**
 * @author ltb
 * @date 2022/6/20 11:30
 * @desc
 */
public class TestMatrix {
    public static void main(String[] args){
        Matrix m1=new Matrix(new double[][]{
                {1,2,3},
                {4,5,6}

        });
        System.out.println(m1);
        Matrix m2=new Matrix(new double[][]{
                {1,2},
                {3,4},
                {5,6},
        });
        System.out.println(m2);
        Matrix m3=Matrix.mul(m1,m2);
        System.out.println(m3);

        m1=m1.transpose();
        m2=m2.transpose();
        System.out.println(m1);
        System.out.println(m2);

        m3=m1.mul(m2);
        System.out.println(m3);

        Matrix m4 = Matrix.makeE(4);
        System.out.println(m4);

    }
}
