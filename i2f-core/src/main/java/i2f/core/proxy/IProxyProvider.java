package i2f.core.proxy;

import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.annotations.remark.Usage;

/**
 * @author ltb
 * @date 2022/3/25 20:23
 * @desc
 */
@Author("i2f")
@Remark("defined proxy provider")
public interface IProxyProvider {
    @Usage("IRegistry reg=new JdkProxyProvider().proxy(new RegistryImpl(),new BasicProxyHandler());")
    @Remark("for obj make a proxy object as return value using handler process.")
    <T> T proxy(Object obj,IProxyHandler handler);
}
