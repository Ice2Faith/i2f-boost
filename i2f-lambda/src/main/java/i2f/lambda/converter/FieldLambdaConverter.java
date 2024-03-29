package i2f.lambda.converter;

import i2f.functional.delegator.ExFunctionalDelegator;
import i2f.lambda.inflater.LambdaInflater;

import java.lang.reflect.Field;

/**
 * @author Ice2Faith
 * @date 2024/3/29 16:41
 * @desc
 */
public class FieldLambdaConverter extends ExFunctionalDelegator<Field> {
    public static final FieldLambdaConverter INSTANCE=new FieldLambdaConverter();

    public FieldLambdaConverter() {
        super(LambdaInflater::getSerializedLambdaFieldNullable);
    }

}
