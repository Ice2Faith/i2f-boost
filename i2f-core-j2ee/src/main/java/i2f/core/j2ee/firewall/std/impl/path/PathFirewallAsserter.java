package i2f.core.j2ee.firewall.std.impl.path;

import i2f.core.j2ee.firewall.std.str.IStringFirewallAsserter;

import java.net.URLEncoder;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:21
 * @desc 路径漏洞
 * 用于检测文件路径访问控制
 * 避免路径绕过
 * 路径截断
 * 敏感路径访问等
 * 可应用于文件的上传和下载控制
 */
public class PathFirewallAsserter implements IStringFirewallAsserter {

    public static final char[] BAD_CHARS = {'|', '`', '\'', '\"', '\\', '$', '>', '<', ';', '{', '}', (char) 0};
    public static final String[] BAD_STRS = {"./", "../", "//", "~/", "-/"};
    public static final String[] BAD_SUFFIXES = {".jar", ".war", ".java", ".class", ".jsp", ".jsf",
            ".xml", ".properties", ".json", ".yml", ".yaml", ".drl", ".bpmn", ".bpmn2", ".tpl", ".vm",
            ".cfg", ".conf", ".config", ".cnf", ".ini", ".inf", ".inc", ".service", ".json",
            ".elf", ".exe", ".lib", ".so", ".o", ".msi", ".cmd", ".bat", ".sh", ".ps",
            ".php", ".py", ".pyc", ".pyx", ".go", ".inc", ".php3", ".php4", ".php5", ".phtml",
            ".asp", ".aspx", ".cs", ".asa", ".cdx", ".cer",
            ".pub", ".key", ".crt", ".der", ".ssl", ".bash", ".bashrc", ".ssh", ".git",
            ".pem", ".oca", ".rca", ".pfx", ".cer", ".crt", ".p8",
            ".bak", ".log", ".error", ".err", ".warn", ".info", ".trace", ".debug",
            ".access", ".allow", ".deny", ".myd", ".bin",
            ".lnk", ".sys", ".dat",
    };
    public static final String[] BAD_FILENAMES = {
            "passwd", "password", "shadow", "pass", "password.txt", "pwd", "pwd.txt",
            "ssh_config", "root", "authorized_keys", "id_rsa", "id_rsa.pub",
            "identity", "identity.pub", "sshd_config",
            "ssh_host_dsa_key", "ssh_host_dsa_key.pub",
            "ssh_host_key", "ssh_host_key.pub",
            ".bashrc", ".bash_history", "profile", ".profile",
            "crontab", "httpd", "mysql", "cron.allow", "cron.deny",
            "anacrontab", "host.conf", "hosts", "fstab", "resolv.conf",
            "access_log", "error_log", "warn_log",
            "motd", "interfaces", "networks", "network",
            "sam", "system", "ntuser.dat", "pagefile.sys",
            "hiberfil.sys", "boot.ini", "win.ini", "msdoc.sys",
            "user.dat", "explorer.exe", "cmd.exe", "regedit.exe",
            "notepad.exe", "winver.exe", "rundll32.exe",
            "group", "sudousers", "sudoers", "shells", "sysctl.conf",
            "ld.so.preload"
    };

    public static void assertEntry(String errorMsg, String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        String filePath = value;

        filePath = filePath.trim();
        if ("".equals(filePath)) {
            return;
        }

        filePath = filePath.replaceAll("\\\\", "/");

        filePath = filePath.toLowerCase();


        char[] badChars = BAD_CHARS;
        for (char ch : badChars) {
            String str = ch + "";
            String vstr = containsInjectForm(filePath, str);
            if (vstr != null) {
                throw new PathFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }


        for (int i = 0; i < 32; i++) {
            char ch = (char) i;
            String str = ch + "";
            String vstr = containsInjectForm(filePath, str);
            if (vstr != null) {
                throw new PathFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }
        for (int i = 127; i < 128; i++) {
            char ch = (char) i;
            String str = ch + "";
            String vstr = containsInjectForm(filePath, str);
            if (vstr != null) {
                throw new PathFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }


        String[] badStrs = BAD_STRS;
        for (String badStr : badStrs) {
            String str = badStr;
            String vstr = containsInjectForm(filePath, str);
            if (vstr != null) {
                throw new PathFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }


        String[] badSuffixes = BAD_SUFFIXES;
        int idx = filePath.lastIndexOf(".");
        if (idx >= 0) {
            String suffix = filePath.substring(idx);
            for (String badSuffix : badSuffixes) {
                if (suffix.equals(badSuffix)) {
                    throw new PathFirewallException(errorMsg + ", " + " contains illegal str [" + suffix + "]");
                }
            }
        }


        String[] badFilenames = BAD_FILENAMES;
        idx = filePath.lastIndexOf("/");
        if (idx >= 0) {
            String filename = filePath.substring(idx + 1);
            for (String badFilename : badFilenames) {
                if (filename.equals(badFilename)) {
                    throw new PathFirewallException(errorMsg + ", " + " contains illegal str [" + filename + "]");
                }
            }
        }

    }


    public static String str2form(String str, String separator, Function<Character, String> chMapper) {
        if (str == null) {
            return str;
        }
        if ("".equals(str)) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        char[] chars = str.toCharArray();
        boolean isFirst = true;
        for (char ch : chars) {
            if (!isFirst) {
                if (separator != null) {
                    builder.append(separator);
                }
            }
            builder.append(chMapper.apply(ch));
            isFirst = false;
        }
        return builder.toString();
    }

    public static String containsInjectForm(String filePath, String str) {
        // direct contains
        if (filePath.contains(str)) {
            return str;
        }
        // vary form
        String vstr = str;
        try {
            // url contains
            vstr = URLEncoder.encode(str, "UTF-8");
            if (filePath.contains(vstr)) {
                return vstr;
            }
        } catch (Exception e) {

        }
        // 0x hex form contains
        vstr = str2form(str, null, (ch) -> String.format("0x%02x", (int) ch));
        if (filePath.contains(vstr)) {
            return vstr;
        }
        // % hex form
        vstr = str2form(str, null, (ch) -> String.format("%%%02x", (int) ch));
        if (filePath.contains(vstr)) {
            return vstr;
        }
        // \ x hex form
        vstr = str2form(str, null, (ch) -> String.format("\\x%02x", (int) ch));
        if (filePath.contains(vstr)) {
            return vstr;
        }
        // \ u hex form
        vstr = str2form(str, null, (ch) -> String.format("\\u%04x", (int) ch));
        if (filePath.contains(vstr)) {
            return vstr;
        }
        return null;
    }


    @Override
    public void doAssert(String errorMsg, String value) {
        assertEntry(errorMsg, value);
    }

}
