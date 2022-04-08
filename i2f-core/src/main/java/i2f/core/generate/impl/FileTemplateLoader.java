package i2f.core.generate.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.file.FileUtil;
import i2f.core.interfaces.IMap;

import java.io.File;

/**
 * @author ltb
 * @date 2021/10/26
 * 文件模板加载器
 * 支持classpath写法
 */
@Author("i2f")
public class FileTemplateLoader implements IMap<String,String> {
    @Override
    public String map(String val) {
        if(val==null){
            return null;
        }
        val=val.trim();
        try{
            File file= FileUtil.getFileWithClasspath(val);
            if(file!=null){
                return FileUtil.loadTxtFile(file);
            }
        }catch(Exception e){

        }
        return null;
    }
}
