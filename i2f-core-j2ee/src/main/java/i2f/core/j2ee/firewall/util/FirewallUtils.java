package i2f.core.j2ee.firewall.util;


import i2f.core.j2ee.firewall.context.FirewallContext;
import i2f.core.j2ee.firewall.exception.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2023/8/28 15:03
 * @desc
 */
public class FirewallUtils {

    public static void assertCrLfXssInject(String errorMsg, String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        value = value.toLowerCase();
        char[] badChars = FirewallContext.DEFAULT_BAD_CRLF_CHARS;
        for (char ch : badChars) {
            String str = ch + "";
            assertCrLfXss(errorMsg, value, str);
            str = String.format("%%%x", (int) ch).toLowerCase();
            assertCrLfXss(errorMsg, value, str);
            str = String.format("%%%02x", (int) ch).toLowerCase();
            assertCrLfXss(errorMsg, value, str);
            str = String.format("0x%x", (int) ch).toLowerCase();
            assertCrLfXss(errorMsg, value, str);
            str = String.format("0x%02x", (int) ch).toLowerCase();
            assertCrLfXss(errorMsg, value, str);
            str = ("\\u" + Integer.toHexString((int) ch)).toLowerCase();
            assertCrLfXss(errorMsg, value, str);
            str = String.format("\\u%04x", (int) ch).toLowerCase();
            assertCrLfXss(errorMsg, value, str);
        }
    }

    public static void assertCrLfXss(String errorMsg, String value, String str) {
        String[] charsets = {"", "UTF-8", "GBK", "ISO-8859-1"};
        for (String charset : charsets) {
            try {
                String item = str;
                if (charset != null && !"".equals(charset)) {
                    item = URLEncoder.encode(str, charset);
                }
                item = item.toLowerCase();
                if (value.contains(item)) {
                    throw new CrLfXssFirewallException(errorMsg + ", " + "[" + value + "] contains [" + item + "]");
                }
            } catch (Exception e) {
                if (e instanceof FirewallException) {
                    throw (FirewallException) e;
                }
            }
        }
    }


    public static void assertUrlInject(String errorMsg, String value, boolean filePath) {
        if (value == null || "".equals(value)) {
            return;
        }
        value = value.toLowerCase();
        if (filePath) {
            value = value.replaceAll("\\\\", "/");
        }


        // bad chars
        char[] badChars = FirewallContext.DEFAULT_BAD_URL_CHARS;
        for (char ch : badChars) {
            String str = ch + "";
            assertUrl(errorMsg, value, str);
            str = String.format("%%%x", (int) ch).toLowerCase();
            assertUrl(errorMsg, value, str);
            str = String.format("%%%02x", (int) ch).toLowerCase();
            assertUrl(errorMsg, value, str);
            str = String.format("0x%x", (int) ch).toLowerCase();
            assertUrl(errorMsg, value, str);
            str = String.format("0x%02x", (int) ch).toLowerCase();
            assertUrl(errorMsg, value, str);
            str = ("\\u" + Integer.toHexString((int) ch)).toLowerCase();
            assertUrl(errorMsg, value, str);
            str = String.format("\\u%04x", (int) ch).toLowerCase();
            assertUrl(errorMsg, value, str);
        }
        // un-visible chars
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if ((ch >= 0x00 && ch < 0x20) || (ch > 0x7e && ch <= 0x7f)) {
                throw new UrlInjectFirewallException(errorMsg + ", " + "[" + value + "] contains [" + String.format("%02x", (int) ch) + "]");
            }
        }
        // bad strs
        String[] badStrs = FirewallContext.DEFAULT_BAD_URL_STRS;
        for (String item : badStrs) {
            String str = item;
            assertUrl(errorMsg, value, str);
        }

        assertFileName(errorMsg, value);
    }

    public static void assertUrl(String errorMsg, String value, String str) {
        if (value.contains(str)) {
            throw new UrlInjectFirewallException(errorMsg + ", " + "[" + value + "] contains [" + str + "]");
        }
        String[] charsets = {null, "UTF-8", "GBK", "ISO-8859-1"};
        for (String charset : charsets) {
            try {
                String item = str;
                if (charset != null && !"".equals(charset)) {
                    item = URLEncoder.encode(str, charset);
                }
                item = item.toLowerCase();
                if (value.contains(item)) {
                    throw new UrlInjectFirewallException(errorMsg + ", " + "[" + value + "] contains [" + item + "]");
                }
            } catch (Exception e) {

            }
        }
    }

    public static void assertContentDispositionHeader(String headerName, String value) {
        if (!"content-disposition".equalsIgnoreCase(headerName)) {
            return;
        }
        boolean isFilePart = false;
        String fileName = parseContentDispositionFileName(value);
        if (fileName != null && !"".equals(fileName)) {
            isFilePart = true;
        }

        if (!isFilePart) {
            return;
        }
        assertUrlInject("content disposition", fileName, true);
    }

    public static String parseContentDispositionFileName(String value) {
        if (value == null || "".equals(value)) {
            return "";
        }
        String fileName = "";
        String[] arr = value.split(";");
        for (String item : arr) {
            String name = item.trim();
            if (name.startsWith("filename=")) {
                fileName = name.substring("filename=".length());
                fileName = fileName.trim();
                if (fileName.startsWith("\"")) {
                    fileName = fileName.substring(1);
                }
                if (fileName.endsWith("\"")) {
                    fileName = fileName.substring(0, fileName.length() - 1);
                }
                break;
            }
        }
        return fileName;
    }

    public static void assertFileName(String errorMsg, String filePath) {
        if (filePath == null || "".equals(filePath)) {
            return;
        }
        filePath = filePath.replaceAll("\\\\", "/");
        String[] charsets = {"", "UTF-8", "GBK", "ISO-8859-1"};
        for (String charset : charsets) {
            String fileName = filePath;
            if (charset != null && !"".equals(charset)) {
                try {
                    fileName = URLDecoder.decode(filePath, charset);
                } catch (Exception e) {
                    continue;
                }
            }
            String path = "";
            String name = fileName;
            String suffix = "";
            int idx = fileName.lastIndexOf("/");
            if (idx >= 0) {
                path = fileName.substring(0, idx);
                name = fileName.substring(idx + 1);
            }
            idx = name.lastIndexOf(".");
            if (idx >= 0) {
                suffix = name.substring(idx);
            }

            assertFileSuffix(errorMsg, suffix);

            assertMatchedFilename(errorMsg, name);
        }
    }

    private static void assertMatchedFilename(String errorMsg, String name) {
        // bad filenames
        Set<String> badFilenames = FirewallContext.getBadFilenames();
        if (name != null && !"".equalsIgnoreCase(name)) {
            for (String badName : badFilenames) {
                if (badName.equalsIgnoreCase(name)) {
                    throw new FileSuffixFirewallException(errorMsg + ", " + "bad file name [" + badName + "]");
                }
            }
        }
    }

    private static void assertFileSuffix(String errorMsg, String suffix) {
        // bad suffixes
        Set<String> badSuffixes = FirewallContext.getBadSuffixes();

        if (suffix != null && !"".equalsIgnoreCase(suffix)) {
            for (String badSuffix : badSuffixes) {
                if (badSuffix.equalsIgnoreCase(suffix)) {
                    throw new FileSuffixFirewallException(errorMsg + ", " + "bad file suffix [" + suffix + "]");
                }
            }
        }
    }

    public static void assertHttpMethod(String errorMsg, String method) {
        if ("trace".equalsIgnoreCase(method)) {
            throw new HttpMethodFirewallException(errorMsg + ", " + "bad http method [" + method + "]");
        }
    }


    public static void assertPossiblePathParameter(String errorMsg, String parameterValue) {
        char[] pathSeparators = {'/', '\\'};
        if (parameterValue == null || "".equals(parameterValue)) {
            return;
        }
        boolean isPath = false;
        for (char ch : pathSeparators) {
            String str = ch + "";
            if (parameterValue.contains(str)) {
                isPath = true;
                break;
            }
            str = String.format("%%%x", (int) ch).toLowerCase();
            if (parameterValue.contains(str)) {
                isPath = true;
                break;
            }
            str = String.format("%%%02x", (int) ch).toLowerCase();
            if (parameterValue.contains(str)) {
                isPath = true;
                break;
            }
            str = String.format("0x%x", (int) ch).toLowerCase();
            if (parameterValue.contains(str)) {
                isPath = true;
                break;
            }
            str = String.format("0x%02x", (int) ch).toLowerCase();
            if (parameterValue.contains(str)) {
                isPath = true;
                break;
            }
            str = ("\\u" + Integer.toHexString((int) ch)).toLowerCase();
            if (parameterValue.contains(str)) {
                isPath = true;
                break;
            }
            str = String.format("\\u%04x", (int) ch).toLowerCase();
            if (parameterValue.contains(str)) {
                isPath = true;
                break;
            }
        }
        if (!isPath) {
            return;
        }
        assertUrlInject(errorMsg, parameterValue, true);
    }
}
