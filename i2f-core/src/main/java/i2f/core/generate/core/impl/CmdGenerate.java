package i2f.core.generate.core.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.command.CmdLineExecutor;
import i2f.core.functional.common.IMapper;
import i2f.core.generate.RegexGenerator;
import i2f.core.generate.core.IGenerate;
import lombok.Data;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/10/28
 */
@Author("i2f")
@Data
public class CmdGenerate implements IGenerate {
    public IMapper<String, Object> mapper;
    public Object root;
    public Object data;
    public List<String> basePackages;
    public String command;
    public String show;
    public String charset;

    @Override
    public String gen() {
        String cmdLine=getCmdline();
        boolean isShow=true;
        if(show!=null){
            if("true".equals(show)){
                isShow=true;
            }
            if("false".equals(show)){
                isShow=false;
            }
        }
        if(!isShow){
            return "";
        }
        if(charset==null || "".equals(charset)){
            charset="UTF-8";
        }
        return getCmdlineExecuteResult(cmdLine,charset);
    }

    private String getCmdlineExecuteResult(String cmdLine,String charset) {
        try {
            String rs= CmdLineExecutor.runLine(cmdLine, Charset.forName(charset));
            return rs;
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
        return "";
    }

    public String getCmdline(){
        Map<String,Object> param=new HashMap<>(16);
        param.put("_root",root);
        param.put("_item",data);
        Map<String,Object> ctx=new HashMap<>(16);

        param.put("_ctx",ctx);
        String cmdLine= RegexGenerator.render(command,param,mapper,basePackages);
        return cmdLine;
    }

}
