package test.lambda;

import i2f.core.lang.lambda.Lambdas;

/**
 * @author ltb
 * @date 2022/5/17 16:17
 * @desc
 */
public class TestLambdas {
    public static void main(String[] args) throws Exception {
        System.out.println(Lambdas.fieldName(SysUser::getAvatar));
        System.out.println(Lambdas.fieldName(SysUser::setRoleIds));
        System.out.println(Lambdas.fieldName(SysUser::buildSysUser));
        System.out.println(Lambdas.methodName(SysUser::toString));
        System.out.println(Lambdas.methodName(SysUser::setRoleIds));
        System.out.println(Lambdas.methodName(SysUser::testArgs2));
        System.out.println(Lambdas.methodName(SysUser::testArgs3));

    }
}
