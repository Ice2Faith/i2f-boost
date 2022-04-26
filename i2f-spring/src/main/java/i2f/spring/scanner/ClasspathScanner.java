package i2f.spring.scanner;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/4/25 11:31
 * @desc
 */
public class ClasspathScanner {
    public static volatile ClassPathScanningCandidateComponentProvider provider=new ClassPathScanningCandidateComponentProvider(false);

    public static Set<BeanDefinition> scan(Environment environment,String basePackage,TypeFilter ... filters){
        provider.setEnvironment(environment);
        provider.resetFilters(false);
        for(TypeFilter item : filters){
            provider.addIncludeFilter(item);
        }
        return provider.findCandidateComponents(basePackage);
    }

    public static Set<String> scanClassNames(Environment environment,String basePackage,TypeFilter ... filters){
        Set<BeanDefinition> set=scan(environment, basePackage, filters);
        Set<String> ret=new HashSet<>();
        for(BeanDefinition item : set){
            ret.add(item.getBeanClassName());
        }
        return ret;
    }

    public static Set<String> scanClassNamesWithAnnotations(Environment environment, String basePackage, Class<Annotation> ... anns){
        TypeFilter[] filters=new TypeFilter[anns.length];
        for (int i = 0; i < anns.length; i++) {
            filters[i]=new AnnotationTypeFilter(anns[i]);
        }
        return scanClassNames(environment,basePackage,filters);
    }

    public static Set<String> scanClassNamesWithAssignable(Environment environment, String basePackage, Class ... classes){
        TypeFilter[] filters=new TypeFilter[classes.length];
        for (int i = 0; i < classes.length; i++) {
            filters[i]=new AssignableTypeFilter(classes[i]);
        }
        return scanClassNames(environment,basePackage,filters);
    }


    public static Set<BeanDefinition> scan(String basePackage, TypeFilter ... filters){
        return scan(null,basePackage,filters);
    }

    public static Set<String> scanClassNames(String basePackage,TypeFilter ... filters){
        return scanClassNames(null,basePackage,filters);
    }

    public static Set<String> scanClassNamesWithAnnotations(String basePackage, Class<Annotation> ... anns){
        return scanClassNamesWithAnnotations(null,basePackage,anns);
    }

    public static Set<String> scanClassNamesWithAssignable(String basePackage, Class ... classes){
        return scanClassNamesWithAssignable(null,basePackage,classes);
    }

}
