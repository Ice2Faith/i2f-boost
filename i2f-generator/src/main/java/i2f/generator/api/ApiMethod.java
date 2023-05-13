package i2f.generator.api;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author ltb
 * @date 2022/7/6 8:50
 * @desc
 */
@Data
public class ApiMethod {
    private List<String> urls;
    private List<String> methods;
    private Method srcMethod;
    private String javaMethod;
    private String javaGenericMethod;
    private String comment;
    private String remark;
    private List<String> consumers;
    private List<String> products;
    private List<ApiLine> args =new ArrayList<>();
    private List<ApiLine> returns=new ArrayList<>();

    public ApiMethod refresh(boolean keepJavaLang,boolean keepAll){
        for(ApiLine item : args){
            item.refresh(keepJavaLang, keepAll);
        }
        for(ApiLine item : returns){
            item.refresh(keepJavaLang, keepAll);
        }
        return this;
    }

    // 使用基数排序方式排序
    public static class ApiLineComparator implements Comparator<ApiLine>{
        @Override
        public int compare(ApiLine o1, ApiLine o2) {
            String os1=String.format("%10s#%10s%s",o1.getOrder()+"",o1.getParent()+"",o1.getName()+"");
            String os2=String.format("%10s#%10s%s",o2.getOrder()+"",o2.getParent()+"",o2.getName()+"");
            return os1.compareTo(os2);
        }
    }
    public ApiMethod sort(){
        args.sort(new ApiLineComparator());
        returns.sort(new ApiLineComparator());
        return this;
    }
    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        builder.append("-->\t").append(this.getClass().getName()).append("\n")
                .append("java:\t").append(javaMethod).append("\n")
                .append("generic:\t").append(javaGenericMethod).append("\n")
                .append("comment:\t").append(comment).append("\n")
                .append("remark:\t").append(remark).append("\n");
        builder.append("urls:\t");
        for (int i = 0; i < urls.size(); i++) {
            if(i!=0){
                builder.append(", ");
            }
            builder.append(urls.get(i));
        }
        builder.append("\n");

        builder.append("methods:\t");
        for (int i = 0; i < methods.size(); i++) {
            if(i!=0){
                builder.append(", ");
            }
            builder.append(methods.get(i));
        }
        builder.append("\n");

        builder.append("consumers:\t");
        for (int i = 0; i < consumers.size(); i++) {
            if(i!=0){
                builder.append(", ");
            }
            builder.append(consumers.get(i));
        }
        builder.append("\n");

        builder.append("products:\t");
        for (int i = 0; i < products.size(); i++) {
            if(i!=0){
                builder.append(", ");
            }
            builder.append(products.get(i));
        }
        builder.append("\n");

        builder.append("args:").append("\n");
        for (int i = 0; i < args.size(); i++) {
            builder.append(args.get(i)).append("\n");
        }

        builder.append("return:").append("\n");
        for (int i = 0; i < returns.size(); i++) {
            builder.append(returns.get(i)).append("\n");
        }

        return builder.toString();
    }
}
