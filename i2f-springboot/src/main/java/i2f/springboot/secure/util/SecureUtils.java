package i2f.springboot.secure.util;


import i2f.core.codec.CodecUtil;
import i2f.core.digest.Base64Obfuscator;
import i2f.core.digest.Base64Util;
import i2f.core.j2ee.web.ServletContextUtil;
import i2f.core.reflection.reflect.Reflects;
import i2f.spring.mapping.MappingUtil;
import i2f.spring.matcher.MatcherUtil;
import i2f.springboot.secure.SecureConfig;
import i2f.springboot.secure.annotation.SecureParams;
import i2f.springboot.secure.consts.SecureErrorCode;
import i2f.springboot.secure.crypto.SignatureUtil;
import i2f.springboot.secure.data.SecureCtrl;
import i2f.springboot.secure.data.SecureHeader;
import i2f.springboot.secure.exception.SecureException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2023/6/13 8:41
 * @desc
 */
public class SecureUtils {

    public static SecureParams getSecureAnnotation(Member member) {
        return Reflects.annotations(member, SecureParams.class, true, false);
    }

    public static String combinePath(String prefix, String suffix, String separator) {
        if (prefix == null) {
            return suffix;
        }
        if (suffix == null) {
            return prefix;
        }
        if (prefix.endsWith(separator)) {
            if (suffix.startsWith(separator)) {
                return prefix + suffix.substring(separator.length());
            } else {
                return prefix + suffix;
            }
        } else {
            if (suffix.startsWith(separator)) {
                return prefix + suffix;
            } else {
                return prefix + separator + suffix;
            }
        }
    }

    public static String getTrimContextPathRequestUri(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StringUtils.isEmpty(contextPath)) {
            contextPath = "/";
        }
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        if (!contextPath.endsWith("/")) {
            contextPath = contextPath + "/";
        }
        if (!requestUrl.startsWith("/")) {
            requestUrl = "/" + requestUrl;
        }
        if (requestUrl.startsWith(contextPath)) {
            requestUrl = requestUrl.substring(contextPath.length());
        } else {
            String tmp = requestUrl + "/";
            if (contextPath.equals(tmp)) {
                requestUrl = "/";
            }
        }
        if (!requestUrl.startsWith("/")) {
            requestUrl = "/" + requestUrl;
        }
        return requestUrl;
    }

    public static SecureCtrl parseSecureCtrl(HttpServletRequest request, SecureConfig secureConfig, MappingUtil mappingUtil) {
        Method method = mappingUtil.getRequestMappingMethod(request);
        SecureCtrl ctrl = null;
        if (method != null) {
            SecureParams ann = SecureUtils.getSecureAnnotation(method);
            if (ann != null) {
                ctrl = new SecureCtrl();
            }
            SecureUtils.parseAnnotation(ctrl, ann);
        }

        if (ctrl == null) {
            SecureConfig.SecureWhiteListConfig whiteList = secureConfig.getWhiteList();
            if (whiteList != null) {
                ctrl = new SecureCtrl(true, true);
            }
            String requestUrl = getTrimContextPathRequestUri(request);
            SecureUtils.parseUrlWhiteList(ctrl, requestUrl, whiteList);
        }

        if (ctrl == null) {
            ctrl = secureConfig.getDefaultControl();
        }

        String value = ServletContextUtil.getPossibleValue(secureConfig.getHeaderName(), request);
        if (!StringUtils.isEmpty(value)) {
            ctrl.in = true;
        }


        return ctrl;
    }

    public static SecureCtrl parseAnnotation(SecureCtrl ctrl, SecureParams ann) {
        if (ann != null) {
            if (!ctrl.in) {
                ctrl.in = ann.in();
            }
            if (!ctrl.out) {
                ctrl.out = ann.out();
            }
        }
        return ctrl;
    }

    public static SecureCtrl parseUrlWhiteList(SecureCtrl ctrl, String requestUrl, SecureConfig.SecureWhiteListConfig whiteList) {
        if (whiteList == null) {
            return ctrl;
        }
        Set<String> bothPattens = whiteList.getBothPattens();
        if (bothPattens != null && bothPattens.iterator().hasNext()) {
            if (MatcherUtil.antUrlMatchedAny(requestUrl, bothPattens)) {
                ctrl.in = false;
                ctrl.out = false;
            }
        }
        if (ctrl.in) {
            Set<String> inPattens = whiteList.getInPattens();
            if (inPattens != null && inPattens.iterator().hasNext()) {
                if (MatcherUtil.antUrlMatchedAny(requestUrl, inPattens)) {
                    ctrl.in = false;
                }
            }
        }
        if (ctrl.out) {
            Set<String> outPattens = whiteList.getOutPattens();
            if (outPattens != null && outPattens.iterator().hasNext()) {
                if (MatcherUtil.antUrlMatchedAny(requestUrl, outPattens)) {
                    ctrl.out = false;
                }
            }
        }
        return ctrl;
    }

    public static SecureHeader parseSecureHeader(String key, String separator, HttpServletRequest request) {
        String value = ServletContextUtil.getPossibleValue(key, request);
        return decodeSecureHeader(value, separator);
    }

    public static SecureHeader decodeSecureHeader(String str, String separator) {
        if (StringUtils.isEmpty(str)) {
            throw new SecureException(SecureErrorCode.SECURE_HEADER_EMPTY, "空安全头");
        }
        str = CodecUtil.ofUtf8(Base64Util.decode(Base64Obfuscator.decode(str)));
        String[] arr = str.split(separator);
        if (arr.length < 5) {
            throw new SecureException(SecureErrorCode.SECURE_HEADER_STRUCTURE, "不正确的安全头结构");
        }
        SecureHeader ret = new SecureHeader();
        ret.sign = arr[0];
        ret.nonce = arr[1];
        ret.randomKey = arr[2];
        ret.serverAsymSign = arr[3];
        ret.digital = arr[4];
        if (StringUtils.isEmpty(ret.sign)) {
            throw new SecureException(SecureErrorCode.SECURE_HEADER_SIGN_EMPTY, "空安全头签名");
        }
        if (StringUtils.isEmpty(ret.nonce)) {
            throw new SecureException(SecureErrorCode.SECURE_HEADER_NONCE_EMPTY, "空安全头一次性标记");
        }
        if (StringUtils.isEmpty(ret.randomKey)) {
            throw new SecureException(SecureErrorCode.SECURE_HEADER_RANDOM_KEY_EMPTY, "空安全头随机秘钥");
        }
        if (StringUtils.isEmpty(ret.serverAsymSign)) {
            throw new SecureException(SecureErrorCode.SECURE_HEADER_ASYM_SIGN_EMPTY, "空安全头秘钥签名");
        }
        if (StringUtils.isEmpty(ret.digital)) {
            throw new SecureException(SecureErrorCode.SECURE_HEADER_DIGITAL_EMPTY, "空安全头数字签名");
        }
        return ret;
    }

    public static String encodeSecureHeader(SecureHeader header, String separator) {
        StringBuilder builder = new StringBuilder();
        builder.append(header.sign);
        builder.append(separator);
        builder.append(header.nonce);
        builder.append(separator);
        builder.append(header.randomKey);
        builder.append(separator);
        builder.append(header.serverAsymSign);
        builder.append(separator);
        builder.append(header.digital);
        String str = builder.toString();
        return Base64Obfuscator.encode(Base64Util.encode(CodecUtil.toUtf8(str)), true);
    }

    public static String makeSecureSign(String body, SecureHeader header) {
        if (body == null) {
            body = "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(header.nonce);
        builder.append(header.randomKey);
        builder.append(header.serverAsymSign);
        builder.append(header.clientAsymSign);
        builder.append(body);
        String text = builder.toString();
        String sign = SignatureUtil.sign(text);
        return sign;
    }

    public static boolean verifySecureHeader(String body, SecureHeader header) {
        String sign = makeSecureSign(body, header);
        return sign.equals(header.sign);
    }

    public static String decodeEncTrueUrl(String encodeUrl) {
        encodeUrl = CodecUtil.ofUrl(encodeUrl);
        String text = CodecUtil.ofUtf8(Base64Util.decode(encodeUrl));
        text = Base64Obfuscator.decode(text);

        String[] arr = text.split(";");
        if (arr.length != 2) {
            throw new SecureException(SecureErrorCode.BAD_SECURE_REQUEST, "不正确的URL请求");
        }
        String sign = arr[0];
        String url = arr[1];
        String usign = SignatureUtil.sign(url);
        if (!usign.equals(sign)) {
            throw new SecureException(SecureErrorCode.BAD_SIGN, "签名验证失败");
        }
        byte[] data = Base64Util.decodeUrl(Base64Obfuscator.decode(url));
        String trueUrl = CodecUtil.ofUtf8(data);
        return trueUrl;
    }

    public static String encodeEncTrueUrl(String trueUrl) {
        String url = Base64Obfuscator.encode(Base64Util.encode(CodecUtil.toUtf8(trueUrl)), false);
        String sign = SignatureUtil.sign(url);
        String text = Base64Obfuscator.encode(sign + ";" + url, false);
        text = Base64Util.encode(CodecUtil.toUtf8(text));
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
