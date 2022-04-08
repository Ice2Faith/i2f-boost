package i2f.extension.mybatis.interceptor.impl;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/4 15:59
 * @desc
 */
public class SqlLogStatementProxyHandler extends AbstractSqlLogStatementProxyHandler {

    @Override
    protected void writeLog(Method method, String sql, List<MybatisSqlLogData> params) {
        StringBuilder builder=new StringBuilder();
        builder.append("-----> method: ").append(method.getDeclaringClass().getName()).append(".").append(method.getName()).append("\n");
        builder.append("\t---> sql:\n\t").append(sql).append("\n");
        for(MybatisSqlLogData item : params){
            builder.append("\t---> param: ").append(item.property)
                    .append("(").append(item.javaType.getSimpleName()).append(")")
                    .append(item.value)
                    .append("\n");
        }
        System.out.println(builder.toString());
    }
}
