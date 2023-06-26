package i2f.core.reflection.pkg.core;


import i2f.core.annotations.remark.Author;
import i2f.core.container.collection.CollectionUtil;
import i2f.core.reflection.pkg.data.ResourceMetaData;
import i2f.core.resource.ResourceUtil;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * @author ltb
 * @date 2021/9/23
 */
@Author("i2f")
public class ResourceScanner {

    public static void main(String[] args) throws IOException {
        List<ResourceMetaData> list = resources("langs", "database", "web","i2f/core/pkg");
        for(Object item : list){
            System.out.println(item);
        }
    }

    public static final int CLASS_NAMES_COUNT = 1024*10;

    public static List<ResourceMetaData> resources(String ... paths) throws IOException {
        if(paths==null || paths.length==0){
            return scanAllResources();
        }
        List<String> items=getShortlyPrefixes(paths);
        Map<URL,String> urls=new HashMap<>(64);
        for(String pkg : items){
            try{
                URL[] urlEnum= ResourceUtil.getClasspathResources(pkg);
                for(URL url :  urlEnum){
                    urls.put(url,pkg);
                }
            }catch(Exception e){

            }
        }

        List<ResourceMetaData> list=new ArrayList<>(CLASS_NAMES_COUNT);
        for(Map.Entry<URL,String> item : urls.entrySet()){
            scanAllResourcesByURL(item.getKey(),item.getValue(),item.getValue(),list);
        }
        return list;
    }

    public static List<ResourceMetaData> scanAllResourcesByURL(URL url, String relativePath,String basePath, List<ResourceMetaData> list){
        String protocol = url.getProtocol().toLowerCase();
        if ("file".equals(protocol)) {
            File file = new File(url.getFile());
            scanAllInPath(file, relativePath,basePath, list);
        } else if ("jar".equals(protocol)) {
            String ufile = url.getFile();
            String fileName = ufile.substring("file:/".length());
            int idx = fileName.indexOf("!");
            if (idx < 0) {
                idx = fileName.length();
            }
            String jarFile = fileName.substring(0, idx);
            File file = new File(jarFile);
            scanAllInJar(file, relativePath,basePath, list);
        }
        return list;
    }
    public static List<ResourceMetaData> scanAllResources() throws IOException {
        List<ResourceMetaData> list = new ArrayList<>(CLASS_NAMES_COUNT);
        URL url = ResourceUtil.getResource("");

        scanAllResourcesByURL(url,null,"",list);

        return list;
    }

    public static List<ResourceMetaData> scanAllInPath(File path, String relativePath,String basePath, List<ResourceMetaData> list) {
        if (path.isFile()) {
            processFile(path, relativePath,basePath, list);
            return list;
        }
        File[] allFiles = path.listFiles();
        for (File item : allFiles) {
            if (item.isDirectory()) {
                String fileName = item.getName();
                String className = getName(fileName);
                String npkg = null;
                if (relativePath == null) {
                    npkg = className;
                } else {
                    npkg = relativePath + "/" + className;
                }
                scanAllInPath(item, npkg,basePath, list);
            } else if (item.isFile()) {
                processFile(item, relativePath,basePath, list);
            }
        }
        return list;
    }

    private static String getName(String fileName) {
        int idx=fileName.lastIndexOf("/");
        if(idx>=0){
            return fileName.substring(idx+1);
        }
        return fileName;
    }

    public static List<ResourceMetaData> processFile(File file, String relativePath,String basePath, List<ResourceMetaData> list) {
        String fileName = file.getName();
        String className = getName(fileName);
        String npkg = null;
        if (relativePath == null) {
            npkg = className;
        } else {
            npkg = relativePath + "/" + className;
        }
        if (isJarFile(fileName)) {
            scanAllInJar(file, relativePath,basePath, list);
        } else if (file.isFile()) {
            ResourceMetaData data = new ResourceMetaData();
            data.setName(fileName);
            data.setFullName(npkg);
            data.setFile(file);
            data.setLocation(file.getAbsolutePath());
            try{
                data.setUrl(file.toURI().toURL());
            }catch(Exception e){

            }
            list.add(data);
        }
        return list;
    }

    public static List<ResourceMetaData> scanAllInJar(File jar, String relativePath,String basePath, List<ResourceMetaData> list) {
        if (!isJarFile(jar.getName())) {
            scanAllInPath(jar, relativePath,basePath, list);
            return list;
        }

        if (!jar.exists() || !jar.isFile()) {
            return list;
        }

        File tmpDir=new File(System.getProperty("java.io.tmpdir"));
        tmpDir=new File(tmpDir,UUID.randomUUID().toString());


        try {
            JarFile jarFile = new JarFile(jar);
            JarInputStream jis = new JarInputStream(new BufferedInputStream(new FileInputStream(jar)));

            JarEntry entry = jis.getNextJarEntry();
            while (entry != null) {
                String name = entry.getName();

                String className = getName(name);

                 if (isJarFile(name)) {
                    String pfile = jar.getAbsolutePath() + "!" + name;
                    pfile = pfile.replaceAll("\\\\", "/");
                    URL url = new URL("jar", "", -1, pfile);
                    File file = new File(url.getFile());

                    File tfile=new File(tmpDir, name);
                    if(!tfile.getParentFile().exists()){
                        tfile.getParentFile().mkdirs();
                    }
                    unpackJar(jarFile,entry,tfile);
                    scanAllInJar(tfile,relativePath,basePath,list);
                    tfile.delete();
                }else if (!entry.isDirectory() && name.startsWith(basePath)) {
                    ResourceMetaData data = new ResourceMetaData();
                    data.setName(className);
                    data.setFullName(name);
                    data.setLocation(jar.getAbsolutePath() + "/!" + name);
                    URL url=ResourceUtil.getClasspathResource(name);
                    data.setUrl(url);
                    list.add(data);
                }
                entry = jis.getNextJarEntry();
            }
            jis.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        tmpDir.delete();

        return list;
    }

    // 解压 jar 包;
    public static void unpackJar(JarFile jarFile, JarEntry entry, File file) throws IOException {
        try (InputStream is = jarFile.getInputStream(entry)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.flush();
            }
        }
    }

    public static boolean isJarFile(String fileName) {
        String suffix = getSuffix(fileName).toLowerCase();
        if (".jar".equals(suffix)) {
            return true;
        }
        return false;
    }

    public static String getSuffix(String fileName) {
        int idx = fileName.lastIndexOf(".");
        if (idx >= 0) {
            String suffix = fileName.substring(idx);
            return suffix;
        }
        return "";
    }

    // 取多个包名的最短前缀包：
    // 输入： com com.i2f org org.cglib
    // 输出： com org
    public static List<String> getShortlyPrefixes(String ... pfxes) {
        return getShortlyCommonPrefixes("/","/",pfxes);
    }
    public static List<String> getShortlyCommonPrefixes(String separator,String separatorRegex,String ... pfxes) {
        Set<String> set=new HashSet<>(36);
        CollectionUtil.collect(set, pfxes);
        String[] arr=new String[set.size()];
        int p=0;
        for(String item : set){
            arr[p++]=item;
        }
        List<String> ret=new ArrayList<>(arr.length);
        List<String[]> partsList=new ArrayList<>(arr.length);
        Set<Integer> lens=new TreeSet<>();
        int maxStrLen=0;
        for(String item : arr){
            String[] parts=item.split(separatorRegex);
            partsList.add(parts);
            if(item.length()>maxStrLen){
                maxStrLen=item.length();
            }
            lens.add(parts.length);
        }
        StringBuilder builder=new StringBuilder(maxStrLen+8);
        Set<Integer> excludeIdx = new TreeSet<>();
        Iterator<Integer> it=lens.iterator();
        while(it.hasNext()){
            int len=it.next();

            for(int i=0;i<arr.length;i++){
                String[] item= partsList.get(i);
                if(item.length<len){
                    continue;
                }
                if(excludeIdx.contains(i)){
                    continue;
                }
                builder.setLength(0);
                for(int j=0;j<len;j++){
                    if(j!=0){
                        builder.append(separator);
                    }
                    builder.append(item[j]);
                }
                String prefix=builder.toString();

                if(!set.contains(prefix)){
                    continue;
                }
                for(int j=0;j<arr.length;j++){
                    if(excludeIdx.contains(j)){
                        continue;
                    }
                    if(arr[j].equals(prefix)){
                        continue;
                    }
                    String pitem=arr[j];
                    if(pitem.length()>prefix.length()){
                        if(pitem.startsWith(prefix+separator)){
                            excludeIdx.add(j);
                        }
                    }else{
                        if(pitem.startsWith(prefix)){
                            excludeIdx.add(j);
                        }
                    }


                }
            }
        }

        for(int i=0;i<arr.length;i++){
            if(!excludeIdx.contains(i)){
                ret.add(arr[i]);
            }
        }

        return ret;
    }

}
