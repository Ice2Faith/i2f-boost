package i2f.core.resource;

import i2f.core.annotations.remark.Author;
import i2f.core.array.ArrayUtil;
import i2f.core.collection.CollectionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;

/**
 * @author ltb
 * @date 2022/1/18 13:54
 * @desc
 */
@Author("i2f")
public class ResourceUtil {
    public static final String CLASSPATH_PREFIX="classpath:";

    public static ClassLoader getLoader() {
        ClassLoader loader=Thread.currentThread().getContextClassLoader();
        if(loader==null){
            loader=ResourceUtil.class.getClassLoader();
        }
        return loader;
    }
    public static URL getClasspathResource(String location) throws IOException {
        URL[] urls=getClasspathResources(location);
        if(urls.length>0){
            return urls[0];
        }
        return null;
    }
    public static URL[] getClasspathResources(String location) throws IOException {
        return getResources(CLASSPATH_PREFIX+location);
    }
    public static URL getResource(String location) throws IOException {
        URL[] urls=getResources(location);
        if(urls.length>0){
            return urls[0];
        }
        return null;
    }
    public static URL[] getResources(String location) throws IOException {
        if(location==null){
            return new URL[0];
        }
        String lloc=location.toLowerCase();
        if(lloc.startsWith(CLASSPATH_PREFIX)){
            lloc = location.substring(CLASSPATH_PREFIX.length());
            Enumeration<URL> enums=getLoader().getResources(lloc);
            return ArrayUtil.toArray(CollectionUtil.toCollection(new HashSet<URL>(),enums),URL[].class);
        }else{
            File file=new File(location);
            URL url=file.toURI().toURL();
            return new URL[]{url};
        }
    }
    public static InputStream getClasspathResourceAsStream(String location) throws IOException {
        return getResourceAsStream(CLASSPATH_PREFIX+location);
    }
    public static InputStream getResourceAsStream(String location) throws IOException {
        URL[] urls= getResources(location);
        if(urls.length==0){
            throw new FileNotFoundException("could not found resource as:"+location);
        }
        return urls[0].openStream();
    }

}
