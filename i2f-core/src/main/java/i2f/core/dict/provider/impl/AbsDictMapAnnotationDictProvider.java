package i2f.core.dict.provider.impl;


import i2f.core.dict.annotations.DictMap;
import i2f.core.dict.data.DictItem;
import i2f.core.dict.provider.IDictProvider;
import i2f.core.dict.reflect.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/2/21 16:07
 * @desc
 */
public abstract class AbsDictMapAnnotationDictProvider implements IDictProvider {

    @Override
    public List<DictItem> getDictItems(Field field, Field dictField) {
        List<DictItem> ret = new ArrayList<>();
        DictMap dann = ReflectUtil.getAnnotation(dictField, DictMap.class);
        if (dann == null) {
            dann = ReflectUtil.getAnnotation(field, DictMap.class);
        }
        if (dann == null) {
            return ret;
        }
        List<DictItem> list = findDictList(dann);
        return list;
    }

    public abstract List<DictItem> findDictList(DictMap ann);
}
