package i2f.spring.cglib.impl;


import i2f.spring.cglib.IEnhancerSetting;
import org.springframework.cglib.proxy.Enhancer;

public class NoneEnhancerSetting implements IEnhancerSetting {
    @Override
    public void set(Enhancer enhancer) {

    }
}
