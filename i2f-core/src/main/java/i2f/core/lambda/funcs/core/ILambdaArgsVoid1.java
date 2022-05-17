package i2f.core.lambda.funcs.core;

/**
 * @author ltb
 * @date 2022/5/17 11:03
 * @desc
 */
@FunctionalInterface
public interface ILambdaArgsVoid1<T,V1> extends ILambdaArgs {
    void lambda(T source, V1 val1);
}
