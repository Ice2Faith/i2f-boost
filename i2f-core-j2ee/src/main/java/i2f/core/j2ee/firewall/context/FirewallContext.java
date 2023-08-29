package i2f.core.j2ee.firewall.context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2023/8/28 21:48
 * @desc
 */
public class FirewallContext {
    public static final String[] DEFAULT_BAD_SUFFIX = {
            ".java", ".class", ".jar", ".war", ".yml", ".yaml", ".properties", ".xml",
            ".py", ".php", ".jsp", ".jsf",
            ".log", ".error", ".err", ".warn", ".info",
            ".conf", ".cfg", ".ini", ".json", ".cnf", ".inc", ".config",
            ".sh", ".bat", ".cmd", ".ps", ".exe", ".elf", ".so", ".lib", ".o",
    };

    public static final String[] DEFAULT_BAD_FILENAME = {
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

    public static final char[] DEFAULT_BAD_CRLF_CHARS = {'\r', '\n'};

    public static final char[] DEFAULT_BAD_URL_CHARS = {';', '\\', '\r', '\n', '%', '?', '$', '<', '>', '|', '&', '\'', '"', '{', '}', '!', (char) 0};

    public static final String[] DEFAULT_BAD_URL_STRS = {"./", "../", "//", ".\\", "..\\", "\\\\"};

    public static final char[] DEFAULT_BAD_HEADER_CHARS = {'\\', '\r', '\n', '<', '>', '|', '{', '}', '!', '\'', (char) 0};


    public static boolean enableUrl = true;
    public static boolean enableMethod = true;
    public static boolean enableMultipart = true;
    public static boolean enableParameter = true;
    public static boolean enableQueryString = true;
    public static boolean enableRequestHeader = true;

    public static Set<String> addBadSuffixes = new HashSet<>();
    public static Set<String> removeBadSuffixes = new HashSet<>();

    public static Set<String> addBadFilenames = new HashSet<>();
    public static Set<String> removeBadFilenames = new HashSet<>();


    public static Set<String> getBadSuffixes() {
        Set<String> ret = new LinkedHashSet<>();
        ret.addAll(Arrays.asList(DEFAULT_BAD_SUFFIX));
        for (String suffix : addBadSuffixes) {
            ret.remove(suffix);
        }
        ret.addAll(removeBadSuffixes);
        return ret;
    }

    public static Set<String> getBadFilenames() {
        Set<String> ret = new LinkedHashSet<>();
        ret.addAll(Arrays.asList(DEFAULT_BAD_FILENAME));
        for (String name : addBadFilenames) {
            ret.remove(name);
        }
        ret.addAll(removeBadFilenames);
        return ret;
    }
}
