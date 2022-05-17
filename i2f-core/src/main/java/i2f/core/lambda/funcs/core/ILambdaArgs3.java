package i2f.core.lambda.funcs.core;

/**
 * @author ltb
 * @date 2022/5/17 11:03
 * @desc
 */
@FunctionalInterface
public interface ILambdaArgs3<T,R,V1,V2,V3> extends ILambdaArgs {
    R lambda(T source, V1 val1,V2 val2,V3 val3);
}
