package i2f.core.j2ee.firewall.std.impl.xss;


import i2f.core.j2ee.firewall.std.str.IStringFirewallAsserter;

import java.net.URLEncoder;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2023/9/15 11:25
 * @desc SQL注入漏洞
 * 用于检测潜在的SQL注入问题
 * 避免因为SQL注入导致的getshell等严重后果
 */
public class XssFirewallAsserter implements IStringFirewallAsserter {

    public static final String SPACE_CHARS_PATTEN = "[\\s\\p{Zs}\\u3000]+";

    public static final char[] BAD_CHARS = {(char) 0};
    public static final String[] BAD_STRS = {
            "alert(",
            "alert`",
            "console.",
            "eval(",
            "eval`",
            "javascript:",
            "javascript.:",
            "document.cookie",
            "vbscript:",
            "vbscript.:",
            "expression(",
            "expression`",
            "document.",
            "window.",
            "prompt(",
            "prompt`",
            "data:text/html;base64,",
            "setinterval(",
            "setinterval`",
            "settimeout(",
            "settimeout`",
            "<script",
            "<iframe",
            "</script",
            "</iframe",
            "<meta",
            "</meta",
            "confirm(",
            "confirm`",
            "localstorage.",
            "sessionstorage.",
            "open(",
            "open`",
            "print(",
            "print`",
            "parse(",
            "parse`",
            "url(",
            "url`",
            "function",
            "=>",
            ".constructor",
            "['constructor']",
            "[\"constructor\"]",
            "['alert']",
            "[\"alert\"]",
            "(alert)",
    };

    public static final String[] BAD_MATCHES = {
            "=" + SPACE_CHARS_PATTEN + "alert",// let a=alert
            "=" + SPACE_CHARS_PATTEN + "eval", // let a=eval
            "=" + SPACE_CHARS_PATTEN + "prompt", // let a=promp
            "=" + SPACE_CHARS_PATTEN + "confirm", // let a=confirm
            "=" + SPACE_CHARS_PATTEN + "console", // let a=console
            "=" + SPACE_CHARS_PATTEN + "window", // let a=window
            "=" + SPACE_CHARS_PATTEN + "document", // let a=document
            "alert" + SPACE_CHARS_PATTEN + "\\(", // alert(
            "alert" + SPACE_CHARS_PATTEN + "`", // alert`
            "eval" + SPACE_CHARS_PATTEN + "\\(", // eval(
            "eval" + SPACE_CHARS_PATTEN + "`", // eval`
            "javascript" + SPACE_CHARS_PATTEN + ":", // javascript:
            "javascript" + SPACE_CHARS_PATTEN + "\\." + SPACE_CHARS_PATTEN + ":", // javascript.:
            "vbscript" + SPACE_CHARS_PATTEN + ":", // vbscript:
            "vbscript" + SPACE_CHARS_PATTEN + "\\." + SPACE_CHARS_PATTEN + ":", // vbscript.:
            "expression" + SPACE_CHARS_PATTEN + "\\(", // expression(
            "expression" + SPACE_CHARS_PATTEN + "`", // expression`
            "prompt" + SPACE_CHARS_PATTEN + "\\(", // prompt(
            "prompt" + SPACE_CHARS_PATTEN + "`", // prompt`
            "data" + SPACE_CHARS_PATTEN + ":" + SPACE_CHARS_PATTEN
                    + "(?!image).*" + SPACE_CHARS_PATTEN + "/" + SPACE_CHARS_PATTEN + "[a-zA-Z0-9-]*" + SPACE_CHARS_PATTEN + ";" + SPACE_CHARS_PATTEN
                    + "base64" + SPACE_CHARS_PATTEN + ",",  // data:text/html;base64,
            "setinterval" + SPACE_CHARS_PATTEN + "\\(", // setInterval(
            "setinterval" + SPACE_CHARS_PATTEN + "`", // setInterval`
            "settimeout" + SPACE_CHARS_PATTEN + "\\(", // setTimeout(
            "settimeout" + SPACE_CHARS_PATTEN + "`", // setTimeout`
            "<" + SPACE_CHARS_PATTEN + "script", // <script
            "<" + SPACE_CHARS_PATTEN + "iframe", // <iframe
            "<" + SPACE_CHARS_PATTEN + "/" + SPACE_CHARS_PATTEN + "script", // </script
            "<" + SPACE_CHARS_PATTEN + "/" + SPACE_CHARS_PATTEN + "iframe", // <?iframe
            "confirm" + SPACE_CHARS_PATTEN + "\\(", // confirm(
            "confirm" + SPACE_CHARS_PATTEN + "`", // confirm`
            "open" + SPACE_CHARS_PATTEN + "\\(", // open(
            "open" + SPACE_CHARS_PATTEN + "`", // open`
            "print" + SPACE_CHARS_PATTEN + "\\(", // print(
            "print" + SPACE_CHARS_PATTEN + "`", // print`
            "parse" + SPACE_CHARS_PATTEN + "\\(", // parse(
            "parse" + SPACE_CHARS_PATTEN + "`", // parse`
            "url" + SPACE_CHARS_PATTEN + "\\(", // url(
            "url" + SPACE_CHARS_PATTEN + "`", // url`
            "function" + SPACE_CHARS_PATTEN + "\\(", // function(
            "[" + SPACE_CHARS_PATTEN + "'constructor'" + SPACE_CHARS_PATTEN + "]", // ['constructor']
            "[" + SPACE_CHARS_PATTEN + "\"constructor\"" + SPACE_CHARS_PATTEN + "]", // ["constructor"]
            "[" + SPACE_CHARS_PATTEN + "'alert'" + SPACE_CHARS_PATTEN + "]", // ['alert']
            "[" + SPACE_CHARS_PATTEN + "\"alert\"" + SPACE_CHARS_PATTEN + "]", // ["alert"]
    };

    public static XssFirewallAsserter INSTANCE = new XssFirewallAsserter();


    public static void assertEntry(String errorMsg, String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        String sql = value;

        sql = sql.trim();
        if ("".equals(sql)) {
            return;
        }

        sql = " " + sql + " ";
        sql = sql.replaceAll(SPACE_CHARS_PATTEN, " ");
        sql = sql.replaceAll("''", "");
        sql = sql.toLowerCase();


        char[] badChars = BAD_CHARS;
        for (char ch : badChars) {
            String str = ch + "";
            String vstr = containsInjectForm(sql, str);
            if (vstr != null) {
                throw new XssFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }

        for (int i = 0; i < 32; i++) {
            char ch = (char) i;
            if (ch == '\n' || ch == '\r' || ch == '\t') {
                continue;
            }
            String str = ch + "";
            String vstr = containsInjectForm(sql, str);
            if (vstr != null) {
                throw new XssFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }
        for (int i = 127; i < 128; i++) {
            char ch = (char) i;
            String str = ch + "";
            String vstr = containsInjectForm(sql, str);
            if (vstr != null) {
                throw new XssFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }


        String[] badStrs = BAD_STRS;
        for (String badStr : badStrs) {
            String str = badStr;
            String vstr = containsInjectForm(sql, str);
            if (vstr != null) {
                throw new XssFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }

        String[] badMatches = BAD_MATCHES;
        for (String badMatch : badMatches) {
            Pattern p = Pattern.compile(badMatch);
            Matcher m = p.matcher(sql);
            if (m.find()) {
                MatchResult rs = m.toMatchResult();
                String vstr = sql.substring(rs.start(), rs.end());
                throw new XssFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
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
