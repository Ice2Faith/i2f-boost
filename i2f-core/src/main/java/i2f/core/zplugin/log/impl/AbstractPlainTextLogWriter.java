package i2f.core.zplugin.log.impl;

import i2f.core.annotations.notice.Name;
import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.core.zplugin.log.ILogWriter;
import i2f.core.zplugin.log.data.LogData;
import i2f.core.zplugin.log.enums.LogType;

import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ltb
 * @date 2022/3/29 9:00
 * @desc
 */
public abstract class AbstractPlainTextLogWriter implements ILogWriter {
    protected SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void write(LogData data) {
        StringBuilder builder=new StringBuilder();
        builder.append(formatDate(data.getDate()));

        builder.append(String.format(" [%5s]",String.valueOf(data.getLevel())));
        builder.append(String.format(" (%6s)",data.getType()));
        builder.append(String.format(" @%5s@%5s@%5s",data.getSystem(),data.getModule(),data.getLabel()));

        builder.append(String.format(" %30s",data.getClazzName()));
        if(data.getMethod()!=null){
            builder.append(String.format(".%s(...)",data.getMethod().getName()));
        }else if(data.getMethodName()!=null){
            builder.append(String.format(".%s(...)",data.getMethodName()));
        }
        if(data.getNativeMethod()!=null){
            builder.append(" native:"+data.getNativeMethod());
        }
        if(data.getLine()!=null){
            builder.append(String.format(" line:%3d",data.getLine()));
        }
        if(data.getFileName()!=null){
            builder.append(String.format(" file:%20s",data.getFileName()));
        }
        if(data.getUseTime()!=null){
            builder.append(String.format(" use:%3dms",data.getUseTime()));
        }
        if(LogType.BEFORE==data.getType()){
            builder.append(" arguments:");
            Parameter[] parameters=null;
            if(data.getMethod()!=null){
                parameters=data.getMethod().getParameters();
            }
            for(int i=0;i<data.getArgs().length;i++){
                String paramName="";
                String typeName="";
                String valueStr=String.valueOf(data.getArgs()[i]);
                if(parameters!=null){
                    paramName=parameters[i].getName();
                    Name name= ReflectResolver.findAnnotation(parameters[i], Name.class,false);
                    if(name!=null){
                        paramName=name.value();
                    }
                }
                if(data.getArgs()[i]!=null){
                    typeName=data.getArgs()[i].getClass().getName();
                }
                if(parameters!=null){
                    typeName=parameters[i].getType().getName();
                }
                typeName=simpleType(typeName);
                builder.append(String.format("\n\t%s(%s)%s",paramName,typeName,valueStr));
            }
        }else if(LogType.AFTER == data.getType()){
            builder.append(" return:");
            String typeName="";
            String valueStr=String.valueOf(data.getRet());
            if(data.getRet()!=null){
                typeName=data.getRet().getClass().getName();
            }
            if(data.getMethod()!=null){
                typeName=data.getMethod().getReturnType().getName();
            }
            typeName=simpleType(typeName);
            builder.append(String.format("(%s)%s",typeName,valueStr));
        }else if(LogType.EXCEPT==data.getType()){
            builder.append(" exception:");
            builder.append(data.getEx().getClass().getName());
            builder.append(" : ");
            builder.append(data.getEx().getMessage());
        }else if(LogType.DIRECT==data.getType()){
            builder.append(" content:");
            if(data.getContent()!=null){
                for(int i=0;i<data.getContent().length;i++){
                    String typeName="";
                    String valueStr=String.valueOf(data.getContent()[i]);
                    if(data.getContent()[i]!=null){
                        typeName=data.getContent()[i].getClass().getName();
                    }
                    if(data.getContent()[i]!=null){
                        typeName=data.getContent()[i].getClass().getName();
                    }
                    typeName=simpleType(typeName);
                    builder.append(String.format("\n\t(%s)%s",typeName,valueStr));
                }
            }else{
                builder.append("null");
            }
        }

        String text=builder.toString();

        writeTextLog(text,data);
    }

    public abstract void writeTextLog(String text,LogData data);

    public String formatDate(Date date){
        return dateFormat.format(date);
    }

    public String simpleType(String type){
        if(type.startsWith("java.lang.")){
            type=type.substring("java.lang.".length());
        }
        else if(type.startsWith("java.util.")){
            type=type.substring("java.util.".length());
        }
        else if(type.startsWith("java.math.")){
            type=type.substring("java.math.".length());
        }
        return type;
    }
}
