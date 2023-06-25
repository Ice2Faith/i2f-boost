package i2f.core.dict.resolver;


import i2f.core.dict.annotations.DictDecode;
import i2f.core.dict.annotations.DictEncode;
import i2f.core.dict.data.DictItem;
import i2f.core.dict.provider.IDictProvider;
import i2f.core.dict.provider.impl.DictsAnnotationDictProvider;
import i2f.core.dict.reflect.ReflectUtil;
import i2f.core.dict.type.ConvertUtil;
import i2f.core.type.str.Strings;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2023/2/21 11:40
 * @desc
 */
public class DictResolver {
    public static final IDictProvider AnnotationDictProvider = new DictsAnnotationDictProvider();

    public static void decode(Object bean) throws Exception {
        decode(bean, AnnotationDictProvider);
    }

    public static void decode(Object bean, IDictProvider... providers) throws Exception {
        if (bean == null) {
            return;
        }
        Class<?> clazz = bean.getClass();
        Set<Field> fields = ReflectUtil.getFields(clazz);
        Map<String, Field> fieldMap = new LinkedHashMap<>();
        for (Field item : fields) {
            if (!fieldMap.containsKey(item.getName())) {
                fieldMap.put(item.getName(), item);
            }
        }

        for (Field field : fields) {
            DictDecode deann = ReflectUtil.getAnnotation(field, DictDecode.class);
            if (deann == null) {
                continue;
            }
            String fieldName = deann.value();
            Field dictField = field;
            if (!Strings.isEmpty(fieldName)) {
                Field fd = fieldMap.get(fieldName);
                if (fd != null) {
                    dictField = fd;
                }
            }

            List<DictItem> dicts = new ArrayList<>();
            for (IDictProvider provider : providers) {
                dicts = provider.getDictItems(field, dictField);
                if (dicts.size() > 0) {
                    break;
                }
            }
            if (dicts.size() == 0) {
                continue;
            }

            field.setAccessible(true);
            dictField.setAccessible(true);

            Object val = dictField.get(bean);
            String code = "";
            if (val != null) {
                code = String.valueOf(val);
            }

            for (DictItem dict : dicts) {
                if (code.equals(dict.getCode())) {
                    try {
                        Class<?> type = field.getType();
                        Object sval = ConvertUtil.tryConvert(dict.getText(), type, null);
                        field.set(bean, sval);
                        break;
                    } catch (Exception e) {

                    }

                }
            }

        }
    }

    public static void encode(Object bean) throws Exception {
        encode(bean, AnnotationDictProvider);
    }

    public static void encode(Object bean, IDictProvider... providers) throws Exception {
        if (bean == null) {
            return;
        }
        Class<?> clazz = bean.getClass();
        Set<Field> fields = ReflectUtil.getFields(clazz);
        Map<String, Field> fieldMap = new LinkedHashMap<>();
        for (Field item : fields) {
            fieldMap.put(item.getName(), item);
        }

        for (Field field : fields) {
            DictEncode deann = ReflectUtil.getAnnotation(field, DictEncode.class);
            if (deann == null) {
                continue;
            }
            String fieldName = deann.value();
            Field dictField = field;
            if (!Strings.isEmpty(fieldName)) {
                Field fd = fieldMap.get(fieldName);
                if (fd != null) {
                    dictField = fd;
                }
            }

            List<DictItem> dicts = new ArrayList<>();
            for (IDictProvider provider : providers) {
                dicts = provider.getDictItems(field, dictField);
                if (dicts.size() > 0) {
                    break;
                }
            }
            if (dicts.size() == 0) {
                continue;
            }

            field.setAccessible(true);
            dictField.setAccessible(true);

            Object val = dictField.get(bean);
            String text = "";
            if (val != null) {
                text = String.valueOf(val);
            }

            for (DictItem dict : dicts) {
                if (text.equals(dict.getText())) {

                    try {
                        Class<?> type = field.getType();
                        Object sval = null;
                        if (!"".equals(dict.getCode())) {
                            sval = ConvertUtil.tryConvert(dict.getCode(), type);
                        }
                        field.set(bean, sval);
                        break;
                    } catch (Exception e) {

                    }

                }
            }

        }
    }


}
