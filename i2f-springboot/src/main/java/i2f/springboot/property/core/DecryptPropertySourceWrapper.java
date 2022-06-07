package i2f.springboot.property.core;

import org.springframework.core.env.PropertySource;

/**
 * @author ltb
 * @date 2022/6/7 9:33
 * @desc
 */
public class DecryptPropertySourceWrapper<T> extends PropertySource<T> {
    private PropertySource<T> delegate;
    private IPropertyDecryptor decryptor;

    public DecryptPropertySourceWrapper(PropertySource<T> delegate,IPropertyDecryptor decryptor) {
        super(delegate.getName(), delegate.getSource());
        this.delegate = delegate;
        this.decryptor=decryptor;
    }

    @Override
    public Object getProperty(String name) {
        Object src=this.delegate.getProperty(name);
        Object ret=decryptor.decrypt(src,name);
        return ret;
    }

    public PropertySource<T> getDelegate() {
        return this.delegate;
    }
}
