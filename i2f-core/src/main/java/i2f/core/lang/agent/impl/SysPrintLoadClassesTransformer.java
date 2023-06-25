package i2f.core.lang.agent.impl;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author ltb
 * @date 2022/4/3 12:16
 * @desc
 */
public class SysPrintLoadClassesTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println("load class:"+className);
        return classfileBuffer;
    }
}
