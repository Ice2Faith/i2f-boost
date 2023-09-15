package i2f.core.j2ee.firewall.std.impl.sql;


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
public class SqlFirewallAsserter implements IStringFirewallAsserter {

    public static final String SPACE_CHARS_PATTEN = "[\\s\\p{Zs}\\u3000]+";
    public static final String DATABASE_NAME_PATTEN = "[0-9a-zA-Z_\\.$@]+";
    public static final String CONST_CONDITION = "('[a-zA-Z0-9\\$@_-]+'|[a-zA-Z0-9\\$@_-]+)[\\s\\p{Zs}\\u3000]*(=|>|<|>=|<=|<>|!=)[\\s\\p{Zs}\\u3000]*('[a-zA-Z0-9\\$@_-]+'|[a-zA-Z0-9\\$@_-]+)";
    public static final String MYSQL_CONDITION_COMMENTS_1 = "\\/\\*[\\s\\p{Zs}\\u3000]*\\[.*\\][\\s\\p{Zs}\\u3000]*\\*\\/";
    public static final String MYSQL_CONDITION_COMMENTS_2 = "\\/\\*\\!.*\\*\\/";
    public static final String ORACLE_CONDITION_COMMENTS_1 = "\\/\\*\\+.*\\*\\/";
    public static final String ORACLE_CONDITION_COMMENTS_2 = "--\\+" + SPACE_CHARS_PATTEN;

    public static final char[] BAD_CHARS = {'`', '\'', '\"', '\\', ';', (char) 0};
    public static final String[] BAD_STRS = {"extractvalue", "updatexml", "geohash",
            "gtid_subset", "gtid_subtract",
            " and ", " or ", " -- ",
            " exec ", " insert ", "select ", " delete ", " update ", " drop ", " create ", " replace ", "count ", "chr ", "mid ",
            " union ", " union all ", " grant ", " revoke ", " alter ",
            "master ", " truncate ", "char ", " declare ",
            "user()", "database()", "version()", "load_file", "save_file",
            " dumpfile ", " outfile ", " load data ", " infile ", " mysql.", " information_schema.",
            "show databases", "show tables", "show variables", "show users",
            "set global ", "set persist ", "shell(",
            "dbms_export_extension", "get_domain_index_tables", "linxruncmd", "dbms_jvm_exp_perms",
            "temp_java_policy", "import_jvm_perms", "dbms_output", "grant_permission", "dbms_java",
            "dbms_scheduler", "create_program", "create_job", "java source named",
            "kupp$proc", "create_master_process", "dbms_xmlquery", "newcontext",
            "ctxsys", "slow_query_log", "slow_query_log_file",
            "execute(", "sp_oacreate", "sp_oamethod", "utl_file",
            ".write(", ".read(", ".store(", ".load(", ".start(", ".run(", ".shell(", ".exec(", ".execute(",
            "@@datadir", "@@basedir", "@@version_compile_os", "@@global", "@@session", "@@local",
    };
    public static final String[] BAD_MATCHES = {
            // common
            "--" + SPACE_CHARS_PATTEN, // --
            "#" + SPACE_CHARS_PATTEN, // #
            CONST_CONDITION, // 1=1 , 1!=1 '1'='2' user=user ...
            MYSQL_CONDITION_COMMENTS_1,
            MYSQL_CONDITION_COMMENTS_2,
            ORACLE_CONDITION_COMMENTS_1,
            ORACLE_CONDITION_COMMENTS_2,


            "exec" + SPACE_CHARS_PATTEN + "\\(", // exec(
            "shell" + SPACE_CHARS_PATTEN + "\\(", // shell(
            "cmd" + SPACE_CHARS_PATTEN + "\\(", // cmd(
            "run" + SPACE_CHARS_PATTEN + "\\(", // run(
            "command" + SPACE_CHARS_PATTEN + "\\(", // command(
            "execute" + SPACE_CHARS_PATTEN + "\\(", // execute(

            // mysql
            "user" + SPACE_CHARS_PATTEN + "\\(" + SPACE_CHARS_PATTEN + "\\)", // user()
            "current_user" + SPACE_CHARS_PATTEN + "\\(" + SPACE_CHARS_PATTEN + "\\)", // current_user()
            "system_user" + SPACE_CHARS_PATTEN + "\\(" + SPACE_CHARS_PATTEN + "\\)", // system_user()
            "session_user" + SPACE_CHARS_PATTEN + "\\(" + SPACE_CHARS_PATTEN + "\\)", // session_user()
            "schema" + SPACE_CHARS_PATTEN + "\\(" + SPACE_CHARS_PATTEN + "\\)", // schema()
            "database" + SPACE_CHARS_PATTEN + "\\(" + SPACE_CHARS_PATTEN + "\\)", // database()
            "version" + SPACE_CHARS_PATTEN + "\\(" + SPACE_CHARS_PATTEN + "\\)", // version()
            "connection_id" + SPACE_CHARS_PATTEN + "\\(" + SPACE_CHARS_PATTEN + "\\)", // connection_id()
            "load_file" + SPACE_CHARS_PATTEN + "\\(", // load_file(
            "save_file" + SPACE_CHARS_PATTEN + "\\(", // save_file(
            "dump_file" + SPACE_CHARS_PATTEN + "\\(", // dump_file(
            "extractvalue" + SPACE_CHARS_PATTEN + "\\(", // extractvalue(
            "updatexml" + SPACE_CHARS_PATTEN + "\\(", // updatexml(
            "geohash" + SPACE_CHARS_PATTEN + "\\(", // geohash(
            "gtid_subset" + SPACE_CHARS_PATTEN + "\\(", // gtid_subset(
            "gtid_subtract" + SPACE_CHARS_PATTEN + "\\(", // gtid_subtract(
            "show" + SPACE_CHARS_PATTEN + DATABASE_NAME_PATTEN, // show ***
            "use" + SPACE_CHARS_PATTEN + DATABASE_NAME_PATTEN, // use ***
            "mysql." + DATABASE_NAME_PATTEN, // mysql.***
            "information_schema\\." + DATABASE_NAME_PATTEN, // information_schema.***
            "set" + SPACE_CHARS_PATTEN + "(" + DATABASE_NAME_PATTEN + ")+" + SPACE_CHARS_PATTEN + "=", // set *** *** =
            "kill" + SPACE_CHARS_PATTEN + DATABASE_NAME_PATTEN, // kill ***
            "performance_schema\\." + DATABASE_NAME_PATTEN, // performance_schema。***
            "security\\." + DATABASE_NAME_PATTEN, // security。***
            "sys\\." + DATABASE_NAME_PATTEN, // sys。***
            "load" + SPACE_CHARS_PATTEN + "data" + SPACE_CHARS_PATTEN + "(" + DATABASE_NAME_PATTEN + ")*" + SPACE_CHARS_PATTEN + "infile" + SPACE_CHARS_PATTEN, // load data *** infile
            "into" + SPACE_CHARS_PATTEN + "outfile" + SPACE_CHARS_PATTEN, // into outfile
            "desc" + SPACE_CHARS_PATTEN + DATABASE_NAME_PATTEN, // desc ***
            "describe" + SPACE_CHARS_PATTEN + DATABASE_NAME_PATTEN, // describe ***
            // oracle
            "from" + SPACE_CHARS_PATTEN + "all_" + DATABASE_NAME_PATTEN, // from all_***
            "from" + SPACE_CHARS_PATTEN + "user_" + DATABASE_NAME_PATTEN, // from user_***
            "from" + SPACE_CHARS_PATTEN + "dba_" + DATABASE_NAME_PATTEN, // from dba_***
            "from" + SPACE_CHARS_PATTEN + "v\\$" + DATABASE_NAME_PATTEN, // from v$***
            "from" + SPACE_CHARS_PATTEN + "gv\\$" + DATABASE_NAME_PATTEN, // from gv$***
            "from" + SPACE_CHARS_PATTEN + "x\\$" + DATABASE_NAME_PATTEN, // from x$***
            "from" + SPACE_CHARS_PATTEN + "v_\\$" + DATABASE_NAME_PATTEN, // from v_$***
            "from" + SPACE_CHARS_PATTEN + "gv_\\$" + DATABASE_NAME_PATTEN, // from gv_$***
            "from" + SPACE_CHARS_PATTEN + "x_\\$" + DATABASE_NAME_PATTEN, // from x_$***
            "sys_context" + SPACE_CHARS_PATTEN + "\\(", // sys_context(
            "dbms_" + DATABASE_NAME_PATTEN,// dbms_***
            "dbns_" + DATABASE_NAME_PATTEN,// dbns_***
            "sys_" + DATABASE_NAME_PATTEN,// sys_***
            "file_" + DATABASE_NAME_PATTEN,// sys_***
            "utl_" + DATABASE_NAME_PATTEN,// sys_***
    };


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
                throw new SqlFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }

        for (int i = 0; i < 32; i++) {
            char ch = (char) i;
            String str = ch + "";
            String vstr = containsInjectForm(sql, str);
            if (vstr != null) {
                throw new SqlFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }
        for (int i = 127; i < 128; i++) {
            char ch = (char) i;
            String str = ch + "";
            String vstr = containsInjectForm(sql, str);
            if (vstr != null) {
                throw new SqlFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }


        String[] badStrs = BAD_STRS;
        for (String badStr : badStrs) {
            String str = badStr;
            String vstr = containsInjectForm(sql, str);
            if (vstr != null) {
                throw new SqlFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }

        String[] badMatches = BAD_MATCHES;
        for (String badMatch : badMatches) {
            Pattern p = Pattern.compile(badMatch);
            Matcher m = p.matcher(sql);
            if (m.find()) {
                MatchResult rs = m.toMatchResult();
                String vstr = sql.substring(rs.start(), rs.end());
                throw new SqlFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
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
