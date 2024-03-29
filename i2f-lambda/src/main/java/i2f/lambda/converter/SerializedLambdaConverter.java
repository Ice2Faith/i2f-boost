package i2f.lambda.converter;

import i2f.functional.delegator.ExFunctionalDelegator;
import i2f.lambda.inflater.LambdaInflater;

import java.lang.invoke.SerializedLambda;

/**
 * @author Ice2Faith
 * @date 2024/3/29 16:41
 * @desc
 */
public class SerializedLambdaConverter extends ExFunctionalDelegator<SerializedLambda> {
    public static final SerializedLambdaConverter INSTANCE=new SerializedLambdaConverter();

    public SerializedLambdaConverter() {
        super(LambdaInflater::getSerializedLambdaNullable);
    }


}
