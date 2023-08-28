package i2f.core.j2ee.firewall.util;


import i2f.core.j2ee.firewall.exception.CrLfXssFirewallException;
import i2f.core.j2ee.firewall.exception.FileSuffixFirewallException;
import i2f.core.j2ee.firewall.exception.FirewallException;
import i2f.core.j2ee.firewall.exception.UrlInjectFirewallException;

import java.net.URLDecoder;
import java.net.URLEncoder;

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
        char[] badChars = {'\r', '\n'};
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


    public static void assertUrlInject(String errorMsg, String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        value = value.toLowerCase();

        // bad chars
        char[] badChars = {';', '\\', '\r', '\n', '%', '?', '$', '<', '>', '|', '&', '\'', '"', '{', '}', '!', (char) 0};
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
        String[] badStrs = {"./", "../", "//"};
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
        assertUrlInject("content disposition", fileName);
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
                fileName = name.substring("filename=".length(), name.length());
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
        String[] badFilenames = {
                // linux
                "passwd", "shadow", "group", "hosts", "crontab", "fstab", "sudoers", "shells", "sysctl.conf", "ld.so.preload",
                "host.conf", "hostname", "resolv.conf",
                "sshd_config", "id_rsa", "id_rsa.pub", "authorized_keys", "identity", "identity.pub",
                ".bashrc", "profile", "root",
                // windows
                "sam", "system", "ntuser.dat", "pagefile.sys", "hiberfil.sys", "boot.ini", "win.ini", "msdos.sys", "user.dat",
                "explorer.exe", "cmd.exe", "regedit.exe", "notepad.exe", "winver.exe", "rundll32.exe",
                // common
                "password", "password.txt", "pwd.txt", "passwd.txt", "httpd.conf", "nginx.conf", "config.xml", "ssl.conf",
                "user.myd", "redis.conf",
        };
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
        String[] badSuffixes = {
                ".java", ".class", ".jar", ".war", ".yml", ".yaml", ".properties", ".xml",
                ".py", ".php", ".jsp", ".jsf",
                ".log", ".error", ".err", ".warn", ".info",
                ".conf", ".cfg", ".ini", ".json", ".cnf", ".inc", ".config",
                ".sh", ".bat", ".cmd", ".ps", ".exe", ".elf", ".so", ".lib", ".o",
        };

        if (suffix != null && !"".equalsIgnoreCase(suffix)) {
            for (String badSuffix : badSuffixes) {
                if (badSuffix.equalsIgnoreCase(suffix)) {
                    throw new FileSuffixFirewallException(errorMsg + ", " + "bad file suffix [" + suffix + "]");
                }
            }
        }
    }
}
