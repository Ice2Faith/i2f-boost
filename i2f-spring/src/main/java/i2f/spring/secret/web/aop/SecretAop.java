package i2f.spring.secret.web.aop;

import i2f.core.security.secret.data.SecretMsg;
import i2f.core.security.secret.util.SecretUtil;
import i2f.spring.secret.web.annotations.SecretParams;
import i2f.spring.secret.web.core.SecretWebCore;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author ltb
 * @date 2022/10/20 16:47
 * @desc
 */
@Slf4j
@Aspect
public class SecretAop {

    public SecretWebCore secretWebCore;

    public SecretAop(SecretWebCore secretWebCore) {
        this.secretWebCore = secretWebCore;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object controllerLog(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        Class clazz = method.getDeclaringClass();

        Parameter[] params = method.getParameters();
        Object[] args = pjp.getArgs();

        SecretMsg header = secretWebCore.secretHeaderHolder.get().convert();
        // 获取aes解密秘钥
        byte[] aesKey = null;
        // 解密url中的参数，也就是非body中的参数
        boolean hasProcess = false;
        for (int i = 0; i < params.length; i++) {
            Parameter parameter = params[i];
            if (!parameter.getType().equals(String.class)) {
                continue;
            }
            String data = (String) args[i];
            if (data == null || "".equals(data)) {
                continue;
            }
            SecretParams pann = SecretWebCore.getAnnotation(parameter, SecretParams.class);
            if (pann == null) {
                continue;
            }
            if (!pann.in()) {
                continue;
            }
            if (aesKey == null) {
                aesKey = secretWebCore
                        .secretProvider
                        .asymmetricalEncryptor
                        .decryptPrivateKey(header.randomKey, secretWebCore.secretProvider.mineKey);
            }
            byte[] bdata = secretWebCore
                    .secretProvider
                    .symmetricalEncryptor
                    .decryptKey(SecretUtil.str2utf8(data), aesKey);
            args[i] = SecretUtil.utf82str(bdata);
            hasProcess = true;
        }
        Object ret = null;
        if (hasProcess) {
            ret = pjp.proceed(args);
        } else {
            ret = pjp.proceed();
        }

        return ret;
    }
}
