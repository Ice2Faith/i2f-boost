package i2f.core.dict.type.impl;


import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class String2DateConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        if (!TypeUtil.instanceOfAny(tarType, Date.class)) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        boolean ok = TypeUtil.instanceOfAny(obj.getClass(), String.class);
        if (!ok) {
            return false;
        }
        Date date = parseDate(obj);
        return date != null;
    }

    @Override
    public Object convert(Object obj, Class<?> tarType) {
        if (obj == null) {
            return null;
        }
        return parseDate(obj);
    }

    private Date parseDate(Object obj) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss SSS").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyy/MM/dd").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyy-MM").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyy/MM").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyyMMddHHmmssSSS").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyyMMddHHmm").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        try {
            return new SimpleDateFormat("yyyyMM").parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        return null;
    }
}
