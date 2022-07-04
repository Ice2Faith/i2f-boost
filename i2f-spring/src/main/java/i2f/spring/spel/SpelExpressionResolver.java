package i2f.spring.spel;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2022/4/18 16:48
 * @desc
 */
@Component
public class SpelExpressionResolver  implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=applicationContext;
    }

    protected ApplicationContext context;


    public static volatile ExpressionParser parser =new SpelExpressionParser();
    public static volatile PropertyAccessor accessor=new BeanFactoryAccessor();
    public static volatile ParserContext parserContext=new TemplateParserContext();
    public static volatile StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    public <T> T getValue(String express,Class<T> returnType){
        evaluationContext.setRootObject(context);
        evaluationContext.addPropertyAccessor(accessor);
        Expression expression = parser.parseExpression(express, parserContext);
        return expression.getValue(evaluationContext,returnType);
    }

    public  String getString(String express){
        return getValue(express,String.class);
    }

    public  int getInt(String express){
        return getValue(express,Integer.class);
    }

    public  long getLong(String express){
        return getValue(express,Long.class);
    }


    public  float getFloat(String express){
        return getValue(express,Float.class);
    }

    public  double getDouble(String express){
        return getValue(express,Double.class);
    }

    public  boolean getBool(String express){
        return getValue(express,Boolean.class);
    }

}
