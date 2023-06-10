package i2f.liteflow.spel;

import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author ltb
 * @date 2022/4/18 16:48
 * @desc
 */
public class StandaloneSpelExpressionResolver {


    public static volatile ExpressionParser parser = new SpelExpressionParser();
    public static volatile PropertyAccessor accessor = new BeanFactoryAccessor();
    public static volatile ParserContext parserContext = new TemplateParserContext();
    public static volatile StandardEvaluationContext evaluationContext = new StandardEvaluationContext();


    public static <T> T getValue(String express, Class<T> returnType, Object rootObj) {
        evaluationContext.setRootObject(rootObj);
        evaluationContext.addPropertyAccessor(new ReflectivePropertyAccessor());
        Expression expression = parser.parseExpression(express, parserContext);
        return expression.getValue(evaluationContext, returnType);
    }

    public static String getString(String express, Object rootObj) {
        return getValue(express, String.class, rootObj);
    }

    public static int getInt(String express, Object rootObj) {
        return getValue(express, Integer.class, rootObj);
    }

    public static long getLong(String express, Object rootObj) {
        return getValue(express, Long.class, rootObj);
    }

    public static float getFloat(String express, Object rootObj) {
        return getValue(express, Float.class, rootObj);
    }

    public static double getDouble(String express, Object rootObj) {
        return getValue(express, Double.class, rootObj);
    }

    public static boolean getBool(String express, Object rootObj) {
        return getValue(express, Boolean.class, rootObj);
    }

}
