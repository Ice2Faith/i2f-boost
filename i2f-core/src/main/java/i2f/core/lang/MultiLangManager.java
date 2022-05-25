package i2f.core.lang;

import i2f.core.properties.PropertiesUtil;
import i2f.core.resource.ResourceUtil;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author ltb
 * @date 2022/5/25 10:50
 * @desc
 */
public class MultiLangManager {
    public static final String defaultLang = "default";
    protected volatile ConcurrentHashMap<String, ConcurrentHashMap<String, String>> strs = new ConcurrentHashMap<>();

    public String get(String key, String lang) {
        ConcurrentHashMap<String, String> langMap = getLangMap(lang);
        if (langMap.containsKey(key)) {
            return langMap.get(key);
        }
        return findAny(key);
    }

    public String findAny(String key) {
        for (ConcurrentHashMap<String, String> item : strs.values()) {
            if (item.containsKey(key)) {
                return item.get(key);
            }
        }
        return null;
    }

    public ConcurrentHashMap<String, String> getLangMap(String lang) {
        ConcurrentHashMap<String, String> map = strs.get(lang);
        if (map == null) {
            map = strs.get(defaultLang);
        }
        if (map == null) {
            if (!strs.isEmpty()) {
                String any = strs.keySet().iterator().next();
                map = strs.get(any);
            }
        }
        return map;
    }

    public static MultiLangManager loadProperties(String path, String langName) throws IOException {
        URL[] urls = ResourceUtil.getResources(path);
        Map<String, InputStream> resources=new HashMap<>();
        for (URL url : urls) {
            String protocol = url.getProtocol().toLowerCase();
            if ("file".equals(protocol)) {
                File file = new File(url.getFile());
                File[] files = file.listFiles();
                for(File item : files){
                    String fname=item.getName();
                    if(fname.startsWith(langName) && fname.endsWith(".properties")){
                        if(!resources.containsKey(fname)){
                            resources.put(fname,new FileInputStream(item));
                        }
                    }
                }
            } else if ("jar".equals(protocol)) {
                String ufile = url.getFile();
                String fileName = ufile.substring("file:/".length());
                int idx = fileName.indexOf("!");
                if (idx < 0) {
                    idx = fileName.length();
                }
                String jarFile = fileName.substring(0, idx);
                try {
                    JarInputStream jis = new JarInputStream(new BufferedInputStream(new FileInputStream(jarFile)));

                    JarEntry entry = jis.getNextJarEntry();
                    while (entry != null) {
                        String name = entry.getName();

                        if (name.startsWith(path)) {
                            int pidx=name.lastIndexOf("/");
                            String fname=name.substring(pidx+1);
                            if(fname.startsWith(langName) && fname.endsWith(".properties")){
                                URL res=ResourceUtil.getClasspathResource(name);
                                if(!resources.containsKey(fname)){
                                    resources.put(fname,res.openStream());
                                }
                            }
                        }
                        entry = jis.getNextJarEntry();
                    }
                    jis.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        MultiLangManager manager=new MultiLangManager();
        for(Map.Entry<String,InputStream> item : resources.entrySet()){
            String fname=item.getKey();
            String lang=fname.substring(langName.length());
            lang=lang.substring(0,lang.length()-".properties".length());
            if("".equals(lang)){
                lang=defaultLang;
            }
            InputStream is=item.getValue();
            Properties prop = PropertiesUtil.load(is);
            ConcurrentHashMap<String,String> map=new ConcurrentHashMap<>();
            for(Map.Entry<Object,Object> entry : prop.entrySet()){
                map.put(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            }
            manager.strs.put(lang,map);
            is.close();
        }
        return manager;
    }
}
