package i2f.core.zplugin.dependence;

import i2f.core.io.file.FileUtil;
import i2f.core.lang.functional.common.IFilter;
import i2f.core.lang.functional.common.IMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/4/1 9:53
 * @desc
 */
public class ClassImportsUtil {

    public static void copyClasses(String sourceCodeLocation,String outputLocation,String ... classNames) throws IOException {
        Set<String> list=new HashSet<>();
        for(String item : classNames){
            Set<String> next= getImports(item,sourceCodeLocation,true,true);
            list.addAll(next);
        }
        for(String item : list){
            File srcFile=getClassFile(item,sourceCodeLocation);
            File dstFile=getClassFile(item,outputLocation);
            FileUtil.copy(dstFile,srcFile,null);
        }
    }

    /**
     * 获取指定类的所有导入类
     * onlyFile指定则只保留源代码路径下能找到对应文件的导入
     * flatMultiply指定则解析导入包时出现的.*;型导入，解析为这个包单层的所有依赖包
     * className支持指定包下面的所有.*;形式
     * @param className
     * @param sourceCodeLocation
     * @param onlyFile
     * @param flatMultiply
     * @return
     * @throws IOException
     */
    public static Set<String> getImports(String className, String sourceCodeLocation, boolean onlyFile, boolean flatMultiply) throws IOException {
        Set<String> set=new HashSet<>();
        if(className.endsWith("*")){
            String pkg=className.substring(0,className.length()-2);
            File file=getPackageFile(className.substring(0,className.length()-2),sourceCodeLocation);
            if(file.exists() && file.isDirectory()){
                File[] files=file.listFiles();
                for(File item : files){
                    if(item.isFile() && item.getName().endsWith(".java")){
                        Set<String> next= getImports(pkg+"."+item.getName().substring(0,item.getName().length()-5),sourceCodeLocation);
                        set.addAll(next);
                    }
                }
            }
        }else{
            set= getImports(className, sourceCodeLocation);
        }
        if(!onlyFile){
            return set;
        }
        Set<String> ret=new HashSet<>();
        for(String item : set){
            if(item.endsWith("*")){
                if(flatMultiply){
                    String pkg=item.substring(0,item.length()-2);
                    File file=getClassFile(item,sourceCodeLocation);
                    File dir=file.getParentFile();
                    if(!dir.exists() || !dir.isDirectory()){
                        continue;
                    }
                    File[] files=dir.listFiles();
                    for(File pfile : files){
                        if(pfile.isDirectory()){
                            continue;
                        }
                        if(!pfile.getName().endsWith(".java")){
                            continue;
                        }
                        ret.add(pkg+"."+pfile.getName().substring(0,pfile.getName().length()-5));
                    }
                }else{
                    File file=getClassFile(item,sourceCodeLocation);
                    if(file.exists()){
                        ret.add(item);
                    }
                }
            }else{
                File file=getClassFile(item,sourceCodeLocation);
                if(file.exists()){
                    ret.add(item);
                }
            }
        }
        return ret;
    }

    /**
     * 获取指定类源代码文件所导入的包
     * className不支持.*;形式
     * @param className
     * @param sourceCodeLocation
     * @return
     * @throws IOException
     */
    public static Set<String> getImports(String className, String sourceCodeLocation) throws IOException {
        Set<String> ret = new HashSet<>();
        File file = getClassFile(className, sourceCodeLocation);
        if (!file.exists() || !file.isFile()) {
            return ret;
        }
        List<String> imports = FileUtil.readTxtFile(file, 0, -1, true, true, new IFilter<String>() {
            @Override
            public boolean test(String val) {
                return val.matches("^import\\s+.+;$");
            }
        }, new IMapper<String, String>() {
            @Override
            public String get(String val) {
                val = val.substring("import".length()).trim();
                val = val.substring(0, val.length() - 1);
                return val;
            }
        });
        ret.addAll(imports);
        for (String item : imports) {
            Set<String> next = getImports(item, sourceCodeLocation);
            ret.addAll(next);
        }
        return ret;
    }

    public static String getClassFileName(String className, String sourceCodeLocation) {
        return getClassFile(className, sourceCodeLocation).getAbsolutePath();
    }

    public static File getClassFile(String className, String sourceCodeLocation) {
        return new File(sourceCodeLocation, getClassFileName(className));
    }

    public static String getClassFileName(String className) {
        return className.replaceAll("\\.", "/") + ".java";
    }

    public static File getPackageFile(String pkg, String sourceCodeLocation) {
        return new File(sourceCodeLocation,pkg.replaceAll("\\.", "/"));
    }
}
