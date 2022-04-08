package i2f.extension.proxy.cglib.impl;

import i2f.extension.proxy.cglib.IEnhancerSetting;
import net.sf.cglib.proxy.Enhancer;

public class NoneEnhancerSetting implements IEnhancerSetting {
    @Override
    public void set(Enhancer enhancer) {

    }
}
