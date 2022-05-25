package i2f.core.type;

import i2f.core.data.Pair;
import i2f.core.exception.BoostException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/5/25 13:43
 * @desc 这个类必须是抽象的，使用时直接抽象实例化一个本对象子类，通过getType方法获取类型即可
 * 示例：
 * Class type=new TypeToken<Integer>(){}.getType();
 * 得到的type即为java.lang.Integer
 * 另外，在反射时，也可以根据具体的Field类型，得到泛型的类型
 * 这样就可以在泛型反射时，能够正确实例化类型
 */
public abstract class TypeToken<T> {
    private List<Pair<String,Object>> list;
    private Map<String,Integer> map;
    public static void main(String[] args){
        System.out.println(new TypeToken<Integer>() {}.getType());
        System.out.println(getListType(TypeToken.class,"list"));
        System.out.println(getMapKeyType(TypeToken.class,"map"));
        System.out.println(getMapValueType(TypeToken.class,"map"));
    }
    public Class<T> getType(){
        return getGenericsClassType(getClass());
    }

    @Override
    public String toString() {
        return getType().getName();
    }

    public static<E> Class<E> getMapKeyType(Class<E> clazz,String fieldName){
        return getFieldType(clazz, fieldName,0);
    }

    public static<E> Class<E> getMapValueType(Class<E> clazz,String fieldName){
        return getFieldType(clazz, fieldName,1);
    }

    public static<E> Class<E> getListType(Class<?> clazz,String fieldName){
        return getFieldType(clazz, fieldName,0);
    }

    public static<E> Class<E> getFieldType(Class<?> clazz,String fieldName){
        return getFieldType(clazz,fieldName,0);
    }

    public static<E> Class<E> getFieldType(Class<?> clazz,String fieldName,int idx){
        try{
            Field field = clazz.getDeclaredField(fieldName);
            if(field==null){
                field= clazz.getField(fieldName);
            }
            return (Class<E>)rawType(getGenericsFieldTypes(field)[idx]);
        }catch(Exception e){
            throw new BoostException(e.getMessage(),e);
        }
    }

    public static<E> Class<E> rawType(Type type){
        if(type instanceof ParameterizedType){
            return (Class<E>)((ParameterizedType)type).getRawType();
        }
        return (Class<E>)type;
    }

    public static<E> Class<E> getGenericsClassType(Class<?> clazz){
        return (Class<E>) getGenericsClassTypes(clazz)[0];
    }

    public static Type[] getGenericsClassTypes(Class<?> clazz){
        Type genType = clazz.getGenericSuperclass();
        return getGenericsTypes(genType);
    }

    public static<E> Class<E> getGenericsFieldType(Field field){
        return (Class<E>)rawType(getGenericsFieldTypes(field)[0]);
    }

    public static Type[] getGenericsFieldTypes(Field field){
        Type genType = field.getGenericType();
        return getGenericsTypes(genType);
    }

    public static Type[] getGenericsTypes(Type genType){
        if(genType instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) genType;
            return ptype.getActualTypeArguments();
        }
        throw new BoostException("not a generics type!");
    }

}
