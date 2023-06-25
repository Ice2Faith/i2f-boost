package i2f.extension.elasticsearch;

import i2f.core.reflection.reflect.bean.BeanResolver;
import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.core.reflection.reflect.interfaces.PropertyAccessor;
import i2f.core.std.api.ApiPage;
import i2f.extension.elasticsearch.annotation.EsField;
import i2f.extension.elasticsearch.annotation.EsId;
import i2f.extension.elasticsearch.annotation.EsIndex;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/5/8 16:59
 * @desc
 */
public class EsBeanManager {
    protected EsManager manager;

    public EsBeanManager(){

    }
    public EsBeanManager(EsManager manager){
        this.manager=manager;
    }

    public EsManager getManager() {
        return manager;
    }

    public EsBeanManager setManager(EsManager manager) {
        this.manager = manager;
        return this;
    }

    //////////////////////////////////////////
    public static volatile ConcurrentHashMap<Class,String> esIndexCache=new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<Field,String> esFieldNameCache=new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<Class,PropertyAccessor> esIdFieldCache=new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<Class,List<PropertyAccessor>> esFieldsCache=new ConcurrentHashMap<>();
    public static String getIndex(Class clazz){
        if(esIndexCache.containsKey(clazz)){
            return esIndexCache.get(clazz);
        }
        String ret=clazz.getSimpleName();
        EsIndex ann = ReflectResolver.findAnnotation(clazz, EsIndex.class, false);
        if(ann!=null){
            if(!"".equals(ann.value())){
                ret=ann.value();
            }
        }
        esIndexCache.put(clazz,ret);
        return ret;
    }
    public static String getName(Field field){
        if(esFieldNameCache.containsKey(field)){
            return esFieldNameCache.get(field);
        }
        String ret= field.getName();
        EsField ann = ReflectResolver.findAnnotation(field, EsField.class, false);
        if(ann!=null){
            if(!"".equals(ann.alias())){
                ret=ann.alias();
            }
        }
        esFieldNameCache.put(field,ret);
        return ret;
    }
    public static PropertyAccessor getIdField(Class clazz){
        if(esIdFieldCache.containsKey(clazz)){
            return esIdFieldCache.get(clazz);
        }
        List<PropertyAccessor> list = ReflectResolver.getLogicalReadWriteFieldsWithAnnotations(clazz, false, EsId.class);
        Iterator<PropertyAccessor> iterator= list.iterator();
        PropertyAccessor ret=null;
        if(iterator.hasNext()){
            ret=iterator.next();
        }
        esIdFieldCache.put(clazz,ret);
        return ret;
    }
    public static Object getIdValue(Object bean){
        PropertyAccessor accessor=getIdField(bean.getClass());
        accessor.setInvokeObject(bean);
        return accessor.get();
    }
    public static List<PropertyAccessor> getEsFields(Class clazz){
        if(esFieldsCache.containsKey(clazz)){
            return esFieldsCache.get(clazz);
        }
        List<PropertyAccessor> list = ReflectResolver.getLogicalReadWriteFields(clazz);
        List<PropertyAccessor> ret=new ArrayList<>();
        for(PropertyAccessor item : list){
            Field field=item.getField();
            if(field==null){
                continue;
            }
            EsField ann= ReflectResolver.findAnnotation(field,EsField.class,false);
            if(ann!=null){
                if(!ann.value()){
                    continue;
                }
            }
            ret.add(item);
        }
        esFieldsCache.put(clazz,ret);
        return ret;
    }
    public static String getIdName(Class clazz){
        PropertyAccessor accessor=getIdField(clazz);
        if(accessor==null){
            return null;
        }
        Field field= accessor.getField();

        return getName(field);
    }
    public static Map<String, Object> bean2EsMap(Object obj){
        Map<String, Object> ret=new HashMap<>();
        if(obj==null){
            return null;
        }
        Class clazz=obj.getClass();
        List<PropertyAccessor> fields=getEsFields(clazz);
        for(PropertyAccessor item : fields){
            item.setInvokeObject(obj);
            Object val=item.get();
            Field field= item.getField();
            String name=getName(field);
            ret.put(name,val);
        }
        return ret;
    }
    public static<T> T esMap2Bean(Map<String,Object> map,Class<T> beanClass){
        Map<String,Object> beanMap=new HashMap<>();
        List<PropertyAccessor> fields=getEsFields(beanClass);
        for(Map.Entry<String,Object> item : map.entrySet()){
            String name=item.getKey();
            Object val=item.getValue();
            for(PropertyAccessor prop : fields){
                Field field= prop.getField();
                String fname=getName(field);
                if(fname.equals(name)){
                    name=field.getName();
                }
            }
            beanMap.put(name,val);
        }
        return BeanResolver.copy(beanMap,beanClass,true);
    }
    //////////////////////////////////////////

    public boolean indexCreate(Class clazz) throws IOException {
        String indexName=getIndex(clazz);
        return manager.indexCreate(indexName);
    }
    public GetIndexResponse indexSearch(Class clazz) throws IOException {
        String indexName=getIndex(clazz);
        return manager.indexSearch(indexName);
    }

    public boolean indexDelete(Class clazz) throws IOException {
        String indexName=getIndex(clazz);
        return manager.indexDelete(indexName);
    }
    public boolean indexExists(Class clazz) throws IOException {
        String indexName=getIndex(clazz);
        return manager.indexExists(indexName);
    }
    ////////////////////////////////////////////
    public boolean recordsInsert(Object bean) throws IOException {
        String indexName=getIndex(bean.getClass());
        Object id=getIdValue(bean);
        Map<String, Object> map=bean2EsMap(bean);
        return manager.recordsInsert(indexName,String.valueOf(id),map);
    }
    public boolean recordsUpdate(Object bean) throws IOException {
        String indexName=getIndex(bean.getClass());
        Object id=getIdValue(bean);
        Map<String, Object> map=bean2EsMap(bean);
        return manager.recordsUpdate(indexName,String.valueOf(id),map);
    }
    public boolean recordsDelete(Object bean) throws IOException {
        String indexName=getIndex(bean.getClass());
        Object id=getIdValue(bean);
        return manager.recordsDelete(indexName,String.valueOf(id));
    }
    public GetResponse recordsGet(Object bean) throws IOException {
        String indexName=getIndex(bean.getClass());
        Object id=getIdValue(bean);
        return manager.recordsGet(indexName,String.valueOf(id));
    }
    public<T> T recordsGetAsBean(T bean) throws IOException {
        String indexName=getIndex(bean.getClass());
        Object id=getIdValue(bean);
        Map<String,Object> map=manager.recordsGetAsMap(indexName,String.valueOf(id));
        Class<T> clazz=(Class<T>) bean.getClass();
        return esMap2Bean(map,clazz);
    }
    ////////////////////////////////////////////
    public<T> boolean recordsBatchInsert(List<T> list) throws IOException {
        T bean=list.get(0);
        String indexName=getIndex(bean.getClass());
        Map<String,Map<String, Object>> docs=new HashMap<>();
        for(T item : list){
            Object id=getIdValue(item);
            Map<String,Object> map=bean2EsMap(item);
            docs.put(String.valueOf(id),map);
        }
        return manager.recordsBatchInsertMap(indexName,docs);
    }
    public<T> boolean recordsBatchDelete(List<T> list) throws IOException {
        T bean= list.get(0);
        String indexName=getIndex(bean.getClass());
        List<String> ids=new ArrayList<>();
        for(T item : list){
            Object id=getIdValue(item);
            ids.add(String.valueOf(id));
        }
        return manager.recordsBatchDelete(indexName,ids);
    }
    public<T> boolean recordsBatchUpdate(List<T> list) throws IOException {
        T bean=list.get(0);
        String indexName=getIndex(bean.getClass());
        Map<String,Map<String, Object>> docs=new HashMap<>();
        for(T item : list){
            Object id=getIdValue(item);
            Map<String,Object> map=bean2EsMap(item);
            docs.put(String.valueOf(id),map);
        }
        return manager.recordsBatchUpdateMap(indexName,docs);
    }
    ///////////////////////////////////////////
    public<T> ApiPage<T> search(Class<T> beanClass,SearchSourceBuilder builder) throws IOException {
        String indexName=getIndex(beanClass);
        ApiPage<Map<String, Object>> rpage= manager.searchAsMap(indexName,builder);
        List<T> list=new ArrayList<>();
        for(Map<String, Object> item : rpage.getList()){
            T bean=esMap2Bean(item,beanClass);
            list.add(bean);
        }
        ApiPage<T> ret=new ApiPage<>();
        ret.setTotal(rpage.getTotal());
        ret.setList(list);
        return ret;
    }

    public<T> ApiPage<T> searchAll(Class<T> beanClass) throws IOException {
        String indexName=getIndex(beanClass);
        ApiPage<Map<String, Object>> rpage= manager.searchAllAsMap(indexName);
        List<T> list=new ArrayList<>();
        for(Map<String, Object> item : rpage.getList()){
            T bean=esMap2Bean(item,beanClass);
            list.add(bean);
        }
        ApiPage<T> ret=new ApiPage<>();
        ret.setTotal(rpage.getTotal());
        ret.setList(list);
        return ret;
    }

    public EsQuery query(){
        return EsQuery.queryBean(this);
    }



}
