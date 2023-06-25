package test.inject;

import i2f.core.lang.proxy.impl.JdkProxyProvider;
import i2f.core.zplugin.inject.IInjectFieldProvider;
import i2f.core.zplugin.inject.InjectProxyHandler;
import i2f.core.zplugin.inject.core.InjectProvider;
import test.ITest;
import test.Test2Impl;
import test.model.TestBean;

/**
 * @author ltb
 * @date 2022/5/9 17:23
 * @desc
 */
public class TestInject {
    public static void main(String[] args){
        InjectProvider provider = InjectProvider.provider();
        provider.registry("testBean",new IInjectFieldProvider() {
            @Override
            public Object getValue(String fieldName) throws Exception {
                if("sex".equals(fieldName)){
                    return "男";
                }
                if("age".equals(fieldName)){
                    return 12;
                }
                return null;
            }
        });

        ITest test=new JdkProxyProvider().proxy(new Test2Impl(),new InjectProxyHandler(provider));

        TestBean bean=new TestBean();

        bean.setAccount("user");
        test.bean(bean);
    }
}
