package i2f.core.lambda.funcs.core;

/**
 * @author ltb
 * @date 2022/5/17 11:03
 * @desc
 */
@FunctionalInterface
public interface ILambdaArgsVoid4<T,V1,V2,V3,V4> extends ILambdaArgs {
    void lambda(T source, V1 val1,V2 val2,V3 val3,V4 val4);
}
