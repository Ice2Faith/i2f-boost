package i2f.core.j2ee.firewall.std.impl.path;

import i2f.core.codec.str.code.UCodeStringCodec;
import i2f.core.codec.str.code.XCodeStringCodec;
import i2f.core.codec.str.html.HtmlStringStringCodec;
import i2f.core.codec.str.url.UrlStringStringCodec;
import i2f.core.j2ee.firewall.std.common.FirewallAsserterUtils;
import i2f.core.j2ee.firewall.std.impl.xxe.XxeFirewallException;
import i2f.core.j2ee.firewall.std.str.IStringFirewallAsserter;
import lombok.Data;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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


    public static void main(String[] args){
        INSTANCE.doAssert("test","%2524");
    }

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
    public static final PathFirewallAsserter INSTANCE=new PathFirewallAsserter();

    public static class Rules{
        public char[] includeBadChars;
        public char[] excludeBadChars;
        public char[] replaceBadChars;

        public String[] includeBadStrs;
        public String[] excludeBadStrs;
        public String[] replaceBadStrs;

        public String[] includeBadSuffixes;
        public String[] excludeBadSuffixes;
        public String[] replaceBadSuffixes;

        public String[] includeBadFilenames;
        public String[] excludeBadFilenames;
        public String[] replaceBadFilenames;
    }

    public static class Context{
        public char[] badChars;
        public List<String> badStrs;
        public List<String> badSuffixes;
        public List<String> badFilenames;
        public static Context defaultInstance(){
            Context ret = new Context();
            ret.badChars= FirewallAsserterUtils.merge(BAD_CHARS,null,null,null);
            ret.badStrs=FirewallAsserterUtils.merge(Arrays.asList(BAD_STRS),null,null,null);
            ret.badSuffixes=FirewallAsserterUtils.merge(Arrays.asList(BAD_SUFFIXES),null,null,null);
            ret.badFilenames=FirewallAsserterUtils.merge(Arrays.asList(BAD_FILENAMES),null,null,null);
            return ret;
        }
    }

    private Context context;

    public PathFirewallAsserter(){
        this.applyRules(null);
    }

    public PathFirewallAsserter(Rules rules){
        this.applyRules(rules);
    }

    public PathFirewallAsserter applyRules(Rules rules){
        this.context=Context.defaultInstance();
        if(rules!=null){
            this.context.badChars=FirewallAsserterUtils.merge(
                    BAD_CHARS,
                    rules.includeBadChars,
                    rules.excludeBadChars,
                    rules.replaceBadChars);
            this.context.badStrs=FirewallAsserterUtils.merge(
                    Arrays.asList(BAD_STRS),
                    Arrays.asList(rules.includeBadStrs),
                    Arrays.asList(rules.excludeBadStrs),
                    Arrays.asList(rules.replaceBadStrs));
            this.context.badSuffixes=FirewallAsserterUtils.merge(
                    Arrays.asList(BAD_SUFFIXES),
                    Arrays.asList(rules.includeBadSuffixes),
                    Arrays.asList(rules.excludeBadSuffixes),
                    Arrays.asList(rules.replaceBadSuffixes));
            this.context.badFilenames=FirewallAsserterUtils.merge(
                    Arrays.asList(BAD_FILENAMES),
                    Arrays.asList(rules.includeBadFilenames),
                    Arrays.asList(rules.excludeBadFilenames),
                    Arrays.asList(rules.replaceBadFilenames));

        }
        return this;
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

    public static String containsInjectFormByEncode(String targetStr, String testStr) {
        List<Function<String, String>> singleWrappers = Arrays.asList(
                (str) -> str,
                UrlStringStringCodec.INSTANCE::encode,
                (str)->str2form(str, null, (ch) -> String.format("0x%02x", (int) ch)),
                (str)->str2form(str, null, (ch) -> String.format("%%%02x", (int) ch)),
                (str)->str2form(str, null, (ch) -> String.format("\\x%02x", (int) ch)),
                (str)->str2form(testStr, null, (ch) -> String.format("\\u%04x", (int) ch)),
                UrlStringStringCodec.INSTANCE::encode,
                HtmlStringStringCodec.INSTANCE::encode,
                UCodeStringCodec.INSTANCE::encode,
                XCodeStringCodec.INSTANCE::encode
        );
        int size = singleWrappers.size();

        List<Function<String, String>> wrappers = new LinkedList<>(singleWrappers);
        boolean useCombine = true;
        if (useCombine) {
            List<Function<String, String>> groupWrappers = new LinkedList<>();
            List<List<Integer>> groups = FirewallAsserterUtils.getAllCombinations(size);
            for (List<Integer> group : groups) {
                Function<String, String> groupWrapper = (str) -> {
                    String ret = str;
                    for (Integer idx : group) {
                        Function<String, String> func = singleWrappers.get(idx);
                        ret = func.apply(ret);
                    }
                    return ret;
                };
                groupWrappers.add(groupWrapper);
            }
            wrappers = groupWrappers;
        }
        for (Function<String, String> wrapper : wrappers) {
            String text = wrapper.apply(testStr);
            text = text.toLowerCase();
            if (targetStr.contains(text)) {
                return text;
            }
        }

        return null;
    }

    public static String containsInjectFormByDecode(String targetStr, String testStr) {
        List<Function<String, String>> singleWrappers = Arrays.asList(
                (str) -> str,
                UrlStringStringCodec.INSTANCE::decode,
                UrlStringStringCodec.INSTANCE::decode,
                HtmlStringStringCodec.INSTANCE::decode,
                UCodeStringCodec.INSTANCE::decode,
                XCodeStringCodec.INSTANCE::decode
        );
        int size = singleWrappers.size();

        List<Function<String, String>> wrappers = new LinkedList<>(singleWrappers);
        boolean useCombine = true;
        if (useCombine) {
            List<Function<String, String>> groupWrappers = new LinkedList<>();
            List<List<Integer>> groups = FirewallAsserterUtils.getAllCombinations(size);
            for (List<Integer> group : groups) {
                Function<String, String> groupWrapper = (str) -> {
                    String ret = str;
                    for (Integer idx : group) {
                        Function<String, String> func = singleWrappers.get(idx);
                        ret = func.apply(ret);
                    }
                    return ret;
                };
                groupWrappers.add(groupWrapper);
            }
            wrappers = groupWrappers;
        }
        for (Function<String, String> wrapper : wrappers) {
            String text = wrapper.apply(targetStr);
            text = text.toLowerCase();
            if (text.contains(testStr)) {
                return testStr;
            }
        }
        return null;
    }

    public static String containsInjectForm(String filePath, String str) {
        String vstr = containsInjectFormByEncode(filePath, str);
        if(vstr!=null){
            return vstr;
        }
        return containsInjectFormByDecode(filePath,str);
    }


    @Override
    public void doAssert(String errorMsg, String value) {
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


        char[] badChars = context.badChars;
        if(badChars!=null){
            for (char ch : badChars) {
                String str = ch + "";
                String vstr = containsInjectForm(filePath, str);
                if (vstr != null) {
                    throw new PathFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
                }
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


        List<String> badStrs = context.badStrs;
        if(badStrs!=null){
            for (String badStr : badStrs) {
                String str = badStr;
                String vstr = containsInjectForm(filePath, str);
                if (vstr != null) {
                    throw new PathFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
                }
            }
        }


        List<String> badSuffixes = context.badSuffixes;
        if(badSuffixes!=null){
            int idx = filePath.lastIndexOf(".");
            if (idx >= 0) {
                String suffix = filePath.substring(idx);
                for (String badSuffix : badSuffixes) {
                    if (suffix.equals(badSuffix)) {
                        throw new PathFirewallException(errorMsg + ", " + " contains illegal str [" + suffix + "]");
                    }
                }
            }
        }


        List<String> badFilenames = context.badFilenames;
        if (badFilenames != null) {
            int idx = filePath.lastIndexOf("/");
            if (idx >= 0) {
                String filename = filePath.substring(idx + 1);
                for (String badFilename : badFilenames) {
                    if (filename.equals(badFilename)) {
                        throw new PathFirewallException(errorMsg + ", " + " contains illegal str [" + filename + "]");
                    }
                }
            }
        }
    }

}
