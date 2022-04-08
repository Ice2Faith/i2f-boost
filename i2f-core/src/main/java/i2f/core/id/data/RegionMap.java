package i2f.core.id.data;

import i2f.core.annotations.remark.Author;
import i2f.core.resource.ResourceUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Author("i2f")
public class RegionMap {
    public static final String REGION_MAP_LOCATION="assets/regionMap.txt";
    public static Map<String,String> regionMap=new HashMap<>();
    static {
        try {
            InputStream is= ResourceUtil.getClasspathResourceAsStream(REGION_MAP_LOCATION);
            regionMap=generateMap(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String decode(String regionCode){
        return regionMap.get(regionCode);
    }
    public static Map<String,String> generateMap(InputStream is) throws IOException {
        Map<String,String> map=new HashMap<>();

        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        String line="";
        while((line= reader.readLine())!=null){
            line=line.trim();
            if(line==null || "".equals(line)){
                continue;
            }
            String[] arr=line.split("\\s+",2);
            if(arr.length>=2){
                map.put(arr[0],arr[1]);
            }
        }
        reader.close();
        return map;
    }
}
