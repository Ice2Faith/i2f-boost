package i2f.core.jdbc.data;

import i2f.core.annotations.remark.Author;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.reflect.interfaces.PropertyAccessor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * 作为数据库查询返回结果集存在
 * 提供更高效的值获取方式
 * 通过方法：getCols():List<String>获取返回结果的列名
 * 通过方法：getDatas():List<Map<String, Object>>获取所有的结果，其中的每一个Map就是一行数据
 * Map以列名为键，以数据为值
 * 因此你需要注意列名的大小写是影响你获取值的
 * 因此提供了方法：getDataIgnoreCase(int,String):T来不区分大小写方式获取一个值
 */
@Author("i2f")
@Data
@NoArgsConstructor
public class DBResultData{
    private List<String> cols=new ArrayList<>();
    private List<Map<String,Object>> datas=new ArrayList<>();

    public DBResultData(List<String> cols,List<Map<String,Object>> datas){
        this.cols=cols;
        this.datas=datas;
    }
    public int getCountRows(){
        return datas.size();
    }
    public int getCountCols(){
        return cols.size();
    }
    public boolean hasData(){
        return datas.size()!=0;
    }
    public int getColumnCount(){
        return cols.size();
    }
    public String getColumnName(int index){
        return cols.get(index);
    }
    public<T> T getSingle(){
        return getData(0,0);
    }
    public Map<String,Object> getOne(){
        return this.datas.get(0);
    }
    public<T> T getData(int line,int col){
        return (T)(datas.get(line).get(cols.get(col)));
    }
    public<T> T getData(int line,String colName){
        return  (T)(datas.get(line).get(colName));
    }
    public<T> T getDataIgnoreCase(int line,String colName){
        T ret=null;
        for(String pk : cols){
            if(pk.equalsIgnoreCase(colName)){
                return  (T)(datas.get(line).get(pk));
            }
        }
        return  ret;
    }

    /**
     * 宽松匹配字段映射
     * 将给定的列名，映射为目标bean的字段名
     * 使用宽松比较的情况下，将会优先equals,其次是ignoreEquals,最后是忽略所有符号的仅英文字符的匹配
     * 也就是说，如下情况将都会匹配到：
     * java字段：userId
     * 列名可以是：userId,userid,USERID,user_id,USER_ID,user-ID
     * @param namings 具有的列名
     * @param beanClass 目标bean的类型
     * @return
     */
    public static Map<String,PropertyAccessor> looseNamingMapping(List<String> namings,Class beanClass){
        List<PropertyAccessor> fields = ReflectResolver.getLogicalReadWriteFields(beanClass);
        Map<String,PropertyAccessor> columnMapping=new HashMap<>(namings.size()*2);
        for(String col : namings){
            // 根据匹配权重得到的匹配映射表
            TreeMap<Integer,PropertyAccessor> waitMap=new TreeMap<>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return -Integer.compare(o1,o2);
                }
            });
            for(PropertyAccessor accessor : fields){
                String fieldName= accessor.getName();
                if(col.equals(fieldName)){
                    waitMap.put(100,accessor); // 100%匹配，完全一致
                }else if(col.equalsIgnoreCase(fieldName)){
                    waitMap.put(80,accessor); // 80%匹配，忽略大小写一致
                }else{
                    String colAzName=col.replaceAll("_|-","");
                    String fieldAzName=col.replaceAll("_|-","");
                    if(colAzName.equals(fieldAzName)){
                        waitMap.put(60,accessor); // 60%匹配，去除符号后完全匹配
                    }else if(colAzName.equalsIgnoreCase(fieldAzName)){
                        waitMap.put(50,accessor); // 50%匹配，去除符号后大小写不敏感匹配
                    }
                }
            }
            if(waitMap.size()>0){ // 根据treemap特性，获取第一个，即为最优匹配项
                PropertyAccessor accessor = waitMap.entrySet().iterator().next().getValue();
                columnMapping.put(col,accessor);
            }
        }
        return columnMapping;
    }

    /**
     * 将结果映射为指定的实体bean
     * 需要指定实体bean类型
     * 并且使用宽松比较
     * 使用宽松比较的情况下，将会优先equals,其次是ignoreEquals,最后是忽略所有符号的仅英文字符的匹配
     * 也就是说，如下情况将都会匹配到：
     * java字段：userId
     * 列名可以是：userId,userid,USERID,user_id,USER_ID,user-ID
     * @param beanClass
     * @param <T>
     * @return
     */
    public <T> List<T> parseBean(Class<T> beanClass){
        Map<String,PropertyAccessor> columnMapping=looseNamingMapping(cols,beanClass);
        List<T> ret=new ArrayList<>(this.datas.size());
        for(Map<String,Object> row : this.datas){
            T obj=ReflectResolver.instance(beanClass);
            for(Map.Entry<String,Object> item : row.entrySet()){
                String name=item.getKey();
                Object val=item.getValue();
                PropertyAccessor accessor=columnMapping.get(name);
                if(accessor!=null){
                    accessor.setInvokeObject(obj);
                    accessor.set(val);
                }
            }
        }
        return ret;
    }

}
