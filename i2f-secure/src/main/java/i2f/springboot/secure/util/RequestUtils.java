package i2f.springboot.secure.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ice2Faith
 * @date 2023/6/13 10:06
 * @desc
 */
public class RequestUtils {
    public static String getPossibleValue(String key, HttpServletRequest request){
        String ret = request.getHeader(key);
        if(StringUtils.isEmpty(ret)){
            ret = request.getParameter(key);
        }
        if(StringUtils.isEmpty(ret)){
            Object val=request.getAttribute(key);
            if(val!=null){
                ret=String.valueOf(val);
            }
        }
        return ret;
    }
}
