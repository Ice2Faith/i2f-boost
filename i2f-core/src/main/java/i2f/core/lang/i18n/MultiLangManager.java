package i2f.core.lang.i18n;

import i2f.core.match.impl.AntMatcher;
import i2f.core.properties.PropertiesUtil;
import i2f.core.resource.ResourceUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/5/25 10:50
 * @desc
 */
public class MultiLangManager {
    public static void main(String[] args) throws IOException {
        MultiLangManager manager=loadProperties("classpath:langs","lang");
        String hello = manager.get("hello", defaultLang);
        System.out.println("default:"+hello);
        hello = manager.get("hello", "cn");
        System.out.println("cn:"+hello);
        hello = manager.get("hello", "en");
        System.out.println("en:"+hello);
        System.out.println("done");
    }
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
        Map<URL,String> urls = ResourceUtil.resources(path,langName+"*.properties",new AntMatcher());

        MultiLangManager manager=new MultiLangManager();
        for(Map.Entry<URL,String> item : urls.entrySet()){
            String fname=item.getValue();
            String lang=fname.substring(langName.length());
            lang=lang.substring(0,lang.length()-".properties".length());
            if("".equals(lang)){
                lang=defaultLang;
            }
            if(lang.startsWith("-")){
                lang=lang.substring(1);
            }
            InputStream is=item.getKey().openStream();
            InputStreamReader reader=new InputStreamReader(is,"UTF-8");
            Properties prop = PropertiesUtil.load(reader);
            ConcurrentHashMap<String,String> map=new ConcurrentHashMap<>();
            for(Map.Entry<Object,Object> entry : prop.entrySet()){
                map.put(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            }
            manager.strs.put(lang,map);
            reader.close();
        }
        return manager;
    }
}
