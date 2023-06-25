package i2f.extension.netty.tcp.rpc.handler;

import i2f.core.lang.functional.supplier.ISupplier1;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 定义了根据类名，查找实例服务的实现
 * 在RPC中
 * 最关键的就是
 * 在服务端接收到一个RPC调用之后
 * 服务端需要根据客户端指定的类名，找到对应的类的实例
 * 进行调用实例方法，实现RPC调用
 * 本类通过resisterBean注册一个实例
 * 通过get方法获取对应类的实例
 */
public class InstanceBeanSupplier implements ISupplier1<Object, Class> {
    protected InternalLogger logger = InternalLoggerFactory.getInstance(this.getClass());

    private ConcurrentHashMap<String, Object> nameMap = new ConcurrentHashMap<>();

    public void registerBean(Object obj) {
        registerBean(null, obj);
    }

    public void registerBean(String name, Object obj) {
        if (obj != null) {
            if (name == null || "".equals(name)) {
                name = obj.getClass().getSimpleName();
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
            }
            nameMap.put(name, obj);
        }
    }

    @Override
    public Object get(Class clazz) {
        try {
            Object ret = null;
            for (String key : nameMap.keySet()) {
                Object item = nameMap.get(key);
                if (clazz.isAssignableFrom(item.getClass())) {
                    if (item.getClass().equals(clazz)) {
                        return item;
                    }
                    if (ret == null) {
                        ret = item;
                    }
                }
            }
            if (ret != null) {
                return ret;
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        throw new IllegalStateException("none instance bean of type " + clazz);
    }
}
