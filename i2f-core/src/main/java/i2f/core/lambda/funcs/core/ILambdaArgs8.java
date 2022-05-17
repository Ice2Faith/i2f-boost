package i2f.core.lambda.funcs.core;

/**
 * @author ltb
 * @date 2022/5/17 11:03
 * @desc
 */
@FunctionalInterface
public interface ILambdaArgs8<T,R,V1,V2,V3,V4,V5,V6,V7,V8> extends ILambdaArgs {
    R lambda(T source, V1 val1,V2 val2,V3 val3,V4 val4,V5 val5,V6 val6,V7 val7,V8 val8);
}
