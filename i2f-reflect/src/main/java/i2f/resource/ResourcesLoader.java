package i2f.resource;

import i2f.reflect.ReflectResolver;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;
import java.util.jar.*;

/**
 * @author Ice2Faith
 * @date 2024/2/27 14:10
 * @desc
 */
public class ResourcesLoader {
    private static ConcurrentHashMap<URL, String> RES_CACHE = new ConcurrentHashMap<>();


    public static URL getClasspathResource(String name) {
        name = ReflectResolver.getClasspathName(name);
        return ReflectResolver.getClassLoader().getResource(name);
    }

    public static InputStream getResourceAsStream(URL url) throws IOException {
        String href = url.toString();
        if(!href.startsWith("jar:")){
            return url.openStream();
        }
        String[] arr = href.split("\\!\\/");
        if(arr.length<=2){
            return url.openStream();
        }
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        AtomicBoolean success=new AtomicBoolean(false);
        URL nextUrl=new URL(arr[0]+"!/"+arr[1]);
        InputStream is = nextUrl.openStream();
        getResourceToStream(is,true,arr,2,success,bos);
        bos.flush();
        bos.close();
        if(success.get()){
            return new ByteArrayInputStream(bos.toByteArray());
        }
        return null;
    }

    private static void getResourceToStream(InputStream is,boolean closeIs, String[] arr, int index, AtomicBoolean success, ByteArrayOutputStream bos) {
        if(index>=arr.length){
            return;
        }
        JarInputStream jis=null;
        try{
            if(is instanceof JarInputStream){
                jis=(JarInputStream)is;
            }else{
                jis = new JarInputStream(is);
            }
            JarEntry entry = null;
            boolean isFind=false;
            while ((entry = jis.getNextJarEntry()) != null) {
                String name = entry.getName();
                if (entry.isDirectory()) {

                } else {
                    if(name.equals(arr[index])){
                        isFind=true;
                        if(index==arr.length-1){
                            byte[] buff=new byte[8192];
                            int len=0;
                            while((len=jis.read(buff,0,buff.length))>0){
                                bos.write(buff,0,len);
                            }
                            bos.flush();
                            success.set(true);
                        }else{
                            getResourceToStream(jis,false,arr,index+1,success,bos);
                        }
                    }
                }
                jis.closeEntry();
                if(isFind){
                    break;
                }
            }
        }catch (IOException e){

        }finally {
            if(closeIs){
                if(jis!=null){
                    try{
                        jis.close();
                    }catch(Exception e){
                    }
                }
            }
        }
    }

    public static Map<URL, String> getResources() {
        return getResources(false);
    }

    public static Map<URL, String> getResources(boolean force) {
        if (!RES_CACHE.isEmpty() && !force) {
            return new HashMap<>(RES_CACHE);
        }

        Map<URL, String> ret = scanResources(true, null, (url, name) -> {
            return !ResourcesLoader.isClassFile(name);
        });

        RES_CACHE.clear();
        RES_CACHE.putAll(ret);
        return ret;
    }

    public static Map<String, Class<?>> getClasses() {
        return getClasses(true);
    }

    public static Map<String, Class<?>> getClasses(boolean jumpJre) {
        return getClasses(jumpJre, (path, clazz) -> {
            if (clazz == null) {
                return false;
            }
            String name = clazz.getName();
            if (name.startsWith("java.")) {
                return false;
            }
            if (name.startsWith("javax.")) {
                return false;
            }
            if (name.startsWith("javafx.")) {
                return false;
            }
            if (name.startsWith("sun.")) {
                return false;
            }
            if (name.startsWith("jdk.")) {
                return false;
            }
            if (name.startsWith("com.sun.")) {
                return false;
            }
            return true;
        });
    }

    public static Map<String, Class<?>> getClasses(boolean jumpJre, BiPredicate<String, Class<?>> filter) {
        Map<String, Class<?>> ret = new LinkedHashMap<>();
        Map<URL, String> classes = scanResources(jumpJre, null, (url, str) -> {
            return isLegalClassFile(str);
        });
        for (Map.Entry<URL, String> entry : classes.entrySet()) {
            String value = entry.getValue();
            Class<?> clazz = ReflectResolver.loadClass(ReflectResolver.path2ClassName(value));
            if (filter == null || filter.test(value, clazz)) {
                ret.put(value, clazz);
            }
        }
        return ret;
    }

    public static Map<URL, String> scanResources() {
        return scanResources(true, null, null);
    }

    public static Map<URL, String> scanResources(
            BiPredicate<String, Boolean> jarFilter,
            BiPredicate<URL, String> resourceFilter){
        return scanResources(true,jarFilter,resourceFilter);
    }

    public static Map<URL, String> scanResources(
            boolean jumpJre,
            BiPredicate<String, Boolean> jarFilter,
            BiPredicate<URL, String> resourceFilter) {
        Map<URL, String> ret = new LinkedHashMap<>();
        Set<String> allClasspath = getAllClasspath(jumpJre);
        for (String item : allClasspath) {
            File file = new File(item);
            Map<URL, String> next = findResources(jumpJre, file, jarFilter, resourceFilter);
            ret.putAll(next);
        }
        return ret;
    }


    public static boolean isJarFile(String name) {
        if (name == null) {
            return false;
        }
        int idx = name.lastIndexOf(".");
        if (idx < 0) {
            return false;
        }
        String suffix = name.substring(idx).toLowerCase();
        return Arrays.asList(".jar", ".war").contains(suffix);
    }

    public static boolean isClassFile(String name) {
        if (name == null) {
            return false;
        }
        int idx = name.lastIndexOf(".");
        if (idx < 0) {
            return false;
        }
        String suffix = name.substring(idx).toLowerCase();
        return Arrays.asList(".class").contains(suffix);
    }

    public static boolean isLegalClassFile(String name) {
        if (!isClassFile(name)) {
            return false;
        }
        String className = ReflectResolver.path2ClassName(ReflectResolver.getClasspathName(name));
        if (!className.matches("^[a-zA-Z0-9$.]+$")) {
            return false;
        }
        if (className.matches(".*\\d+\\..*")) {
            return false;
        }
        if (className.matches("^\\d+$")) {
            return false;
        }
        if (className.matches(".*\\$\\d+.*")) {
            return false;
        }
        return true;
    }


    public static Map<URL, String> findResources(boolean jumpJre,
                                                 File file,
                                                 BiPredicate<String, Boolean> jarFilter,
                                                 BiPredicate<URL, String> resourceFilter) {
        if (file.isDirectory()) {
            return findPathResources(jumpJre, file, "", jarFilter, resourceFilter);
        } else {
            return findJarResources(jumpJre, file, "", jarFilter, resourceFilter);
        }
    }

    protected static Map<URL, String> findPathResources(boolean jumpJre,
                                                        File file, String prefix,
                                                        BiPredicate<String, Boolean> jarFilter,
                                                        BiPredicate<URL, String> resourceFilter) {
        Map<URL, String> ret = new LinkedHashMap<>();
        if (file.isFile()) {
            return findJarResources(jumpJre, file, prefix, jarFilter, resourceFilter);
        }
        if (!file.isDirectory()) {
            return ret;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return ret;
        }
        for (File item : files) {
            String name = item.getName();
            String nextPrefix = prefix + "/" + name;
            if ("".equals(name)) {
                nextPrefix = prefix;
            }
            if (isJarFile(name)) {
                Map<URL, String> next = findJarResources(jumpJre, item, nextPrefix, jarFilter, resourceFilter);
                ret.putAll(next);
            } else if (item.isDirectory()) {
                Map<URL, String> next = findPathResources(jumpJre, item, nextPrefix, jarFilter, resourceFilter);
                ret.putAll(next);
            } else {
                try {
                    boolean keep = true;
                    URL key = item.toURI().toURL();
                    if (jumpJre && key.toString().contains("/jre/lib/")) {
                        keep = false;
                    }
                    String value = nextPrefix;
                    if (keep && (resourceFilter == null || resourceFilter.test(key, value))) {
                        ret.put(key, value);
                    }
                } catch (Exception e) {
                }
            }
        }
        return ret;
    }

    protected static Map<URL, String> findJarResources(boolean jumpJre,
                                                       File file, String prefix,
                                                       BiPredicate<String, Boolean> jarFilter,
                                                       BiPredicate<URL, String> resourceFilter) {
        Map<URL, String> ret = new LinkedHashMap<>();
        if (file.isDirectory()) {
            return findPathResources(jumpJre, file, prefix, jarFilter, resourceFilter);
        }
        if (!isJarFile(file.getName())) {
            return ret;
        }
        URL url = null;
        try {
            url = file.toURI().toURL();
        } catch (Exception e) {
        }
        if (url == null) {
            return ret;
        }
        if (jumpJre && url.toString().contains("/jre/lib/")) {
            return ret;
        }
        if (jarFilter != null && !jarFilter.test(file.getName(), true)) {
            return ret;
        }
        try (InputStream is = new FileInputStream(file)) {
            Map<URL, String> next = findJarInputStreamResources(jumpJre, is, false, url, prefix, jarFilter, resourceFilter);
            ret.putAll(next);
        } catch (IOException e) {
//            throw new IllegalStateException("read jar file exception",e);
        }

        return ret;
    }

    protected static Map<URL, String> findJarInputStreamResources(boolean jumpJre,
                                                                  InputStream is, boolean autoClose, URL baseUrl, String prefix,
                                                                  BiPredicate<String, Boolean> jarFilter,
                                                                  BiPredicate<URL, String> resourceFilter) {
        Map<URL, String> ret = new LinkedHashMap<>();
        JarInputStream jis = null;
        try {
            if(is instanceof JarInputStream){
                jis=(JarInputStream)is;
            }else{
                jis = new JarInputStream(is);
            }
            JarEntry entry = null;
            while ((entry = jis.getNextJarEntry()) != null) {
                String name = entry.getName();
                if (entry.isDirectory()) {

                } else {
                    String urlStr = baseUrl.toString();
                    if (!urlStr.startsWith("jar:")) {
                        urlStr = "jar:" + urlStr;
                    }
                    if (!"".equals(name)) {
                        urlStr = urlStr + "!/" + name;
                    }
                    URL key = new URL(urlStr);
                    String value = prefix + "!" + name;
                    if ("".equals(name)) {
                        value = prefix;
                    }
                    boolean keep = true;
                    if (jumpJre && key.toString().contains("/jre/lib/")) {
                        keep = false;
                    }
                    if (keep && (resourceFilter == null || resourceFilter.test(key, value))) {
                        ret.put(key, value);
                    }

                    if (keep && isJarFile(name)) {
                        if (jarFilter == null || jarFilter.test(name, false)) {
                            Map<URL, String> next = findJarInputStreamResources(jumpJre, jis, false, key, value, jarFilter, resourceFilter);
                            ret.putAll(next);
                        }
                    }
                }
                jis.closeEntry();
            }
        } catch (IOException e) {
//            throw new IllegalStateException("read jar stream exception",e);
        } finally {
            if (jis != null) {
                if (autoClose) {
                    try {
                        jis.close();
                    } catch (IOException e) {
                        throw new IllegalStateException("close jar stream exception", e);
                    }
                }
            }
        }
        return ret;
    }

    public static Set<String> getAllClasspath(){
        return getAllClasspath(true);
    }

    public static Set<String> getAllClasspath(boolean jumpJre) {
        Set<String> ret = new LinkedHashSet<>();
        String classPath = ManagementFactory.getRuntimeMXBean().getClassPath();
        String pathSeparator = File.pathSeparator;
        int size=0;
        if(classPath!=null){
            String[] arr = classPath.split(pathSeparator);
            for (String item : arr) {
                if(jumpJre && (item.contains("/jre/lib/") || item.contains("\\jre\\lib\\"))){
                    continue;
                }
                ret.add(item);
                size++;
            }
        }

        // 处理只有一个jar是classpath的情况
        // 这个情况，这个jar必定是入口jar
        // 则这个jar的manifest属性可能指定了class-path
        // 需要添加到已知的classpath中
        if(size==1){
            String str = ret.iterator().next();
            File file = new File(str);
            if(ResourcesLoader.isJarFile(file.getName())){
                try(JarFile jf=new JarFile(file)){
                    Manifest manifest = jf.getManifest();
                    Attributes mainAttributes = manifest.getMainAttributes();
                    String paths = mainAttributes.getValue("Class-Path");
                    if(paths!=null){
                        String[] arr = paths.split("\\s+");
                        for (String item : arr) {
                            File ext = new File(item);
                            if(!ext.exists()){
                                ext=new File(file.getParentFile(),item);
                            }
                            if(ext.exists()){
                                String extPath=ext.getAbsolutePath();
                                if(jumpJre && (extPath.contains("/jre/lib/") || extPath.contains("\\jre\\lib\\"))){
                                    continue;
                                }
                                ret.add(ext.getAbsolutePath());
                                size++;
                            }
                        }
                    }
                }catch (IOException e){

                }
            }
        }

        return ret;
    }
}
