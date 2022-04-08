package i2f.extension.query.annotation;


import i2f.extension.query.enums.EsQueryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EsQuery {
    EsQueryType type() default EsQueryType.EQ;
    String alias() default "";
}
