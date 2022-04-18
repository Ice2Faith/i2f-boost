package i2f.spring.spel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author ltb
 * @date 2022/4/18 16:48
 * @desc
 */
public class SpelExpressionResolver  {

    public static volatile ExpressionParser parser =new SpelExpressionParser();
    public static volatile PropertyAccessor accessor=new BeanFactoryAccessor();
    public static volatile ParserContext parserContext=new TemplateParserContext();
    public static volatile StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    public static<T> T getValue(ApplicationContext context,String express,Class<T> returnType){
        evaluationContext.setRootObject(context);
        evaluationContext.addPropertyAccessor(accessor);
        Expression expression = parser.parseExpression(express, parserContext);
        return expression.getValue(evaluationContext,returnType);
    }

    public static String getString(ApplicationContext context,String express){
        return getValue(context,express,String.class);
    }

    public static int getInt(ApplicationContext context,String express){
        return getValue(context,express,Integer.class);
    }

    public static long getLong(ApplicationContext context,String express){
        return getValue(context,express,Long.class);
    }


    public static float getFloat(ApplicationContext context,String express){
        return getValue(context,express,Float.class);
    }

    public static double getDouble(ApplicationContext context,String express){
        return getValue(context,express,Double.class);
    }

    public static boolean getBool(ApplicationContext context,String express){
        return getValue(context,express,Boolean.class);
    }

}
