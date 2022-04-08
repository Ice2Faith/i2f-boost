package i2f.agent;

import i2f.core.str.StringMatcher;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author ltb
 * @date 2022/4/3 12:24
 * @desc
 */
public class InvokeTimeSysPrintClassesTransformer implements ClassFileTransformer {
    private String[] classNamePattens;

    public InvokeTimeSysPrintClassesTransformer(String... classNamePattens) {
        this.classNamePattens = classNamePattens;
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        // 转换为类名
        className = className.replaceAll("/", ".");
//        if(className.contains("$") || className.startsWith("java.")){
//            System.out.println("jump class:"+className);
//            return classfileBuffer;
//        }


        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = null;
        try {
//            System.out.println("aop "+className);
            // 获取可修改的类示例
            ctClass = pool.get(className);

            // 获取类方法
            CtMethod[] methods = ctClass.getMethods();

            for (CtMethod item : methods) {
//                if(item.isEmpty()){
//                    System.out.println("jump method empty:"+item.getName());
//                    continue;
//                }
//                if(CheckUtil.isIn(item.getName(),
//                        "equals","hashCode","clone",
//                        "wait","notify","finalize","notifyAll",
//                        "getClass","toString")){
//                    System.out.println("jump method in:"+item.getName());
//                    continue;
//                }

                String fullName = className + "." + item.getName();
                if (classNamePattens != null && classNamePattens.length > 0) {
                    boolean match = false;
                    for (String str : classNamePattens) {
                        if (StringMatcher.match(str, fullName)) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) {
                        continue;
                    }
                }

                try {
//                    System.out.println("aop "+item.getName());
                    // 给方法体添加局部变量
                    item.addLocalVariable("zMethodName", pool.get("java.lang.String"));
                    item.addLocalVariable("zClassName", pool.get("java.lang.String"));
                    item.addLocalVariable("zBeginTime", CtClass.longType);
                    item.addLocalVariable("zRunTime", pool.get("java.lang.Runtime"));
                    // 给方法体添加一行代码
                    item.insertBefore("{ zMethodName = (String)\"" + item.getName() + "\";"
                            + "zClassName = (String)\"" + className + "\";"
                            + "zBeginTime = System.currentTimeMillis();"
                            + "zRunTime = Runtime.getRuntime(); }");

                    String outputInvokeTimeCodes = "{ System.out.println(\"-->invoke:\"+zClassName+\".\"+zMethodName" +
                            "+\"\\n\\t use time:\"+(System.currentTimeMillis()-zBeginTime)+\"ms\"" +
                            "+\"\\n\\t free:\"+(zRunTime.freeMemory()*1.0/1024/1024)" +
                            "+\"mb\\n\\t total:\"+(zRunTime.totalMemory()*1.0/1024/1024)" +
                            "+\"mb\\n\\t use rate:\"+(100.0-(zRunTime.freeMemory()*100.0/zRunTime.maxMemory()))" +
                            "+\"%\\n\\t max:\"+(zRunTime.maxMemory()*1.0/1024/1024)" +
                            "+\"mb\\n\\t processor:\"+zRunTime.availableProcessors()); }";

                    item.insertAfter(outputInvokeTimeCodes);
                    item.addCatch("{ System.out.println($zex); $zex.printStackTrace(); " + outputInvokeTimeCodes + " throw $zex; }", pool.get("java.lang.Throwable"), "$zex");


                } catch (Exception e) {
//                    System.err.println("bad method:"+item.getName()+" aop:"+e.getMessage());
                }

            }
            return ctClass.toBytecode();
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (ctClass != null) {
                ctClass.detach();
            }
        }

        return classfileBuffer;
    }
}
