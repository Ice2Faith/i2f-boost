package i2f.extension.mybatis.interceptor.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/4 15:59
 * @desc
 */
public class SqlLogStatementProxyHandler extends AbstractSqlLogStatementProxyHandler {

    @Override
    protected void writeFinallyLog(String seqId, Method method, String sql, List<MybatisSqlLogData> params, Throwable ex, Integer retCount, Number retNum, long useMillSecond) {
        StringBuilder builder=new StringBuilder();
        builder.append("-----> method: ").append(method.getDeclaringClass().getName()).append(".").append(method.getName()).append("\n");
        builder.append("\t---> seq-id:"+seqId).append("\n");
        builder.append("\t---> sql:\n\t").append(sql.replaceAll("\\n\\s*\\n","\n")).append("\n");
        for(MybatisSqlLogData item : params){
            builder.append("\t---> param: ").append(item.property)
                    .append("(").append(item.javaType.getSimpleName()).append(")")
                    .append(item.value)
                    .append("\n");
        }
        builder.append("\t---> use-mill-second:\t").append(useMillSecond).append("ms\n");
        if(retNum!=null){
            builder.append("\t---> return-num:\t").append(retNum).append("\n");
        }
        if(retCount!=null){
            builder.append("\t---> return-size:\t").append(retCount).append("\n");
        }
        if(ex!=null){
            String message=ex.getMessage();
            Throwable pex=ex;
            while(message==null){
                pex=pex.getCause();
                if(pex==null){
                    break;
                }
                message= pex.getMessage();
            }
            builder.append("\t---> exception-message:\t").append(message).append("\n");
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            PrintWriter writer=new PrintWriter(bos);
            ex.printStackTrace(writer);
            writer.flush();
            writer.close();
            builder.append("\t---> exception-trace-stack:\n").append(new String(bos.toByteArray())).append("\n");
        }
        writeStringLog(builder.toString());
    }

    protected void writeStringLog(String str){
        System.out.println(str);
    }
}
