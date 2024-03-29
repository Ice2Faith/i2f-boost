package i2f.lambda.converter;

import i2f.functional.delegator.ExFunctionalDelegator;
import i2f.lambda.inflater.LambdaInflater;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2024/3/29 16:41
 * @desc
 */
public class MethodLambdaConverter extends ExFunctionalDelegator<Method> {
    public static final MethodLambdaConverter INSTANCE=new MethodLambdaConverter();

    public MethodLambdaConverter() {
        super(LambdaInflater::getSerializedLambdaMethodNullable);
    }

}
