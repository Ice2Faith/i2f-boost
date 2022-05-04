package i2f.core.unsafe;

import i2f.core.exception.BoostException;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author ltb
 * @date 2022/5/4 17:01
 * @desc
 */
public class UnsafeHacker {
    private static volatile Unsafe unsafe;
    public static Unsafe getUnsafe(){
        if(unsafe==null){
            synchronized (UnsafeHacker.class){
                if(unsafe==null){
                    try{
                        Field field = Unsafe.class.getDeclaredField("theUnsafe");
                        field.setAccessible(true);
                        unsafe = (Unsafe) field.get(null);
                    }catch(Exception e){
                        throw new BoostException(e.getMessage(),e);
                    }
                }
            }
        }
        return unsafe;
    }
    public static long addressOf(Object obj){
        long ret=0;
        if(obj==null){
            return ret;
        }
        Unsafe unsafe=getUnsafe();
        Object[] array = new Object[] { obj };

        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        int addressSize = unsafe.addressSize();

        switch (addressSize) {
            case 4:
                ret = unsafe.getInt(array, baseOffset);
                break;
            case 8:
                ret = unsafe.getLong(array, baseOffset);
                break;
            default:
                throw new BoostException("unsupported address size: " + addressSize);
        }
        return ret;
    }
    public static String addressHexOf(Object obj){
        return String.format("0x%08X",addressOf(obj));
    }
    public static long sizeOf(Object obj){
        return ObjectSizeCalculator.getObjectSize(obj);
    }

}
