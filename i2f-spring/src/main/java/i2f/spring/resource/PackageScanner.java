package i2f.spring.resource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

public class PackageScanner {
    public static final String ALL_DIR_PATTEN = "/**";
    public static final String ALL_CLASS_PATTEN = ALL_DIR_PATTEN + "/*.class";
    private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    public static Set<Resource> antMatchResources(String patten, Predicate<Resource> filter) throws IOException {
        String classpathPatten = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + patten;
        Set<Resource> ret = new LinkedHashSet<>();
        Resource[] arr = resourcePatternResolver.getResources(classpathPatten);
        for (Resource item : arr) {
            if (filter == null || filter.test(item)) {
                ret.add(item);
            }
        }
        return ret;
    }

    public static Set<Resource> scanResourcesEndWith(String str, Predicate<Resource> filter) throws IOException {
        String classpathPatten = ALL_DIR_PATTEN + "/*" + str;
        return antMatchResources(classpathPatten, filter);
    }

    public static Set<Class<?>> scanClasses(String basePackage, Predicate<String> classNameFilter, Predicate<Class<?>> classFilter) throws IOException {
        Set<Class<?>> ret = new LinkedHashSet<>();
        String patten = ClassUtils.convertClassNameToResourcePath(basePackage) + ALL_CLASS_PATTEN;
        Set<Resource> resources = antMatchResources(patten, null);
        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        for (Resource resource : resources) {
            MetadataReader reader = readerFactory.getMetadataReader(resource);
            String className = reader.getClassMetadata().getClassName();
            if (classNameFilter == null || classNameFilter.test(className)) {
                try {
                    Class<?> clazz = classLoader.loadClass(className);
                    if (classFilter == null || classFilter.test(clazz)) {
                        ret.add(clazz);
                    }
                } catch (Throwable e) {

                }
            }
        }
        return ret;
    }

    public static Set<Class<?>> scanClassesExtendsFrom(String basePackage, Class<?>... parentsClass) throws IOException {
        return scanClasses(basePackage, null, e -> {
            for (Class<?> item : parentsClass) {
                if (item.isAssignableFrom(e)) {
                    return true;
                }
            }
            return false;
        });
    }

    public static Set<Class<?>> scanClassesWithAnnotation(String basePackage, Class<? extends Annotation>... annotations) throws IOException {
        return scanClasses(basePackage, null, e -> {
            for (Class<? extends Annotation> item : annotations) {
                if (e.isAnnotationPresent(item)) {
                    return true;
                }
            }
            return false;
        });
    }
}
