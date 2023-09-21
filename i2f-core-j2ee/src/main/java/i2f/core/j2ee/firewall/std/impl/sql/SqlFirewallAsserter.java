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
    public static final String CONST_CONDITION_NUM = "([+|-]?\\d+)[\\s\\p{Zs}\\u3000]*(=|>|<|>=|<=|<>|!=)[\\s\\p{Zs}\\u3000]*([+|-]?\\d+)";
    public static final String CONST_CONDITION_STR = "('[^']*')[\\s\\p{Zs}\\u3000]*(=|>|<|>=|<=|<>|!=)[\\s\\p{Zs}\\u3000]*('[^']*')";
    public static final String CONST_CONDITION_COL = "([a-zA-Z_\\\\$@][0-9a-zA-Z_\\\\$@]*)[\\s\\p{Zs}\\u3000]*(=|>|<|>=|<=|<>|!=)[\\s\\p{Zs}\\u3000]*([a-zA-Z_\\\\$@][0-9a-zA-Z_\\\\$@]*)";

    public static final String MYSQL_COMMENT_HINT_1 = "\\/\\*[\\s\\p{Zs}\\u3000]*\\[.*\\][\\s\\p{Zs}\\u3000]*\\*\\/";
    public static final String MYSQL_COMMENT_HINT_2 = "\\/\\*\\!.*\\*\\/";
    public static final String ORACLE_COMMENT_HINT_1 = "\\/\\*\\+.*\\*\\/";
    public static final String ORACLE_COMMENT_HINT_2 = "--\\+" + SPACE_CHARS_PATTEN;

    public static final char[] BAD_CHARS = {'\'', ';', (char) 0};
    public static final char[] STRICT_CHARTS = {'"', '`', '\\'};
    public static final String[] BAD_STRS = {"extractvalue", "updatexml", "geohash",
            "gtid_subset", "gtid_subtract",
            " exec ", "count ", "chr ", "mid ", "call ",
            " union ", " union all ",
            " declare ",
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
            "wscript.shell", "sp_oamethod", "sp_oacreate", "sp_dropextendedproc",
            "xp_dirtree", "xp_fileexist", "sp_addextendedproc",
            " pg_description ", " pg_class ", " pg_user ", " pg_proc ", " pg_shadow ",
            " pg_tables ", " pg_attribute ",
            "fn_listextendedproperty", "is_srvrolemember", "sys.",
            "master..", "master.dbo.", "sysdatabases", "sysobjects", "syscolumns",
            "sqlite_master",
            "MSysObjects",
    };
    public static final String[] STRICT_STRS = {
            " and ", " or ", " -- ", " && ", " || ",
            " insert ", "select ", " delete ", " update ", " drop ", " create ", " replace ",
            " grant ", " revoke ", " alter ", " event ", " trigger ",
            "master ", " truncate ", "char ",
            "/etc/", "/root/", "/usr/", "/var/log/", "/var/www/", "/proc/", "/var/db/",
            "/logs/", "/mysql/", "/php/", "/windows/", "/winnt/", "\\windows\\", "\\winnt\\",

    };
    public static final String[] BAD_MATCHES = {
            // common

            CONST_CONDITION_NUM, // 1=1 , 1!=1 ...
            CONST_CONDITION_STR, //  '1'='2' ...
            MYSQL_COMMENT_HINT_1, // /*[xxx]*/
            MYSQL_COMMENT_HINT_2, // /*!xxx*/
            ORACLE_COMMENT_HINT_1, // /*+xxx*/
            ORACLE_COMMENT_HINT_2,  // // --+

            "exec" + SPACE_CHARS_PATTEN + "\\(", // exec(
            "exec" + SPACE_CHARS_PATTEN + DATABASE_NAME_PATTEN, // exec ***
            "shell" + SPACE_CHARS_PATTEN + "\\(", // shell(
            "cmd" + SPACE_CHARS_PATTEN + "\\(", // cmd(
            "run" + SPACE_CHARS_PATTEN + "\\(", // run(
            "command" + SPACE_CHARS_PATTEN + "\\(", // command(
            "execute" + SPACE_CHARS_PATTEN + "\\(", // execute(
            "execute" + SPACE_CHARS_PATTEN + DATABASE_NAME_PATTEN, // execute ***
            "call" + SPACE_CHARS_PATTEN + DATABASE_NAME_PATTEN, // call ***

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
//            "sys_" + DATABASE_NAME_PATTEN,// sys_***
            "file_" + DATABASE_NAME_PATTEN,// sys_***
            "utl_" + DATABASE_NAME_PATTEN,// sys_***
            "xmltype" + SPACE_CHARS_PATTEN + "\\(", // xmltype(***
            "sleep" + SPACE_CHARS_PATTEN + "\\(", // sleep(***
            "benchmark" + SPACE_CHARS_PATTEN + "\\(", // benchmark(***
            "receive_message" + SPACE_CHARS_PATTEN + "\\(", // receive_message(***
            // postgre
            "pg_" + DATABASE_NAME_PATTEN, // pg_***
            "pg_sleep" + SPACE_CHARS_PATTEN + "\\(", // pg_sleep(***
            "generate_series" + SPACE_CHARS_PATTEN + "\\(", // generate_series(***
            "waitfor" + SPACE_CHARS_PATTEN + "delay" + SPACE_CHARS_PATTEN, // waitfor delay
            // mssql
            "master(\\.)+" + DATABASE_NAME_PATTEN, // master.***
            "reconfigure" + SPACE_CHARS_PATTEN + DATABASE_NAME_PATTEN, // reconfigure ***
            "openrowset" + SPACE_CHARS_PATTEN + "\\(", // openrowset(
            // db2
            "sysibm\\." + DATABASE_NAME_PATTEN, // sysibm.***
            "syscat\\." + DATABASE_NAME_PATTEN, // syscat.***
            "sysstat\\." + DATABASE_NAME_PATTEN, // sysstat.***
            "randomblob" + SPACE_CHARS_PATTEN + "\\(", // randomblob(
            // firebird
            "rdb\\$" + DATABASE_NAME_PATTEN, // rdb$***
            // sap maxdb
            "domain\\." + DATABASE_NAME_PATTEN, // domain.***
            // hsqldb
            "regexp_substring" + SPACE_CHARS_PATTEN + "\\(", // regexp_substring(
            "crypt_key" + SPACE_CHARS_PATTEN + "\\(", // crypt_key(
            // informix
            "sysuser:" + DATABASE_NAME_PATTEN, // sysuser:***
            "sysmaster:" + DATABASE_NAME_PATTEN,// sysmaster:***
            // monetdb
            "from" + SPACE_CHARS_PATTEN + "environment" + SPACE_CHARS_PATTEN, // from environment
            "from" + SPACE_CHARS_PATTEN + "schemas" + SPACE_CHARS_PATTEN, // from schemas
            "from" + SPACE_CHARS_PATTEN + "columns" + SPACE_CHARS_PATTEN, // from columns
            "from" + SPACE_CHARS_PATTEN + "tables" + SPACE_CHARS_PATTEN, // from tables
            // vertica
            "v_catalog\\." + DATABASE_NAME_PATTEN, // v_catalog.***
            "v_monitor\\." + DATABASE_NAME_PATTEN, // v_monitor.***
            // altibase
            "system_\\." + DATABASE_NAME_PATTEN, // system_.***
            // mimersql
            "system\\." + DATABASE_NAME_PATTEN, // system.***
            // cubrid
            "from" + SPACE_CHARS_PATTEN + "db_class" + SPACE_CHARS_PATTEN, // from tables
            "from" + SPACE_CHARS_PATTEN + "db_attribute" + SPACE_CHARS_PATTEN, // from tables
            "from" + SPACE_CHARS_PATTEN + "db_method" + SPACE_CHARS_PATTEN, // from tables
            "from" + SPACE_CHARS_PATTEN + "db_user" + SPACE_CHARS_PATTEN, // from tables
            "from" + SPACE_CHARS_PATTEN + "db_auth" + SPACE_CHARS_PATTEN, // from tables
            //
            "dm_" + DATABASE_NAME_PATTEN,// dm_***
            "trace_" + DATABASE_NAME_PATTEN,// trace_***
            "xml_" + DATABASE_NAME_PATTEN,// xml_***
            "log_" + DATABASE_NAME_PATTEN,// log_***
            "sysmail_" + DATABASE_NAME_PATTEN,// sysmail_***
            "master_" + DATABASE_NAME_PATTEN,// master_***
            "master\\.\\." + DATABASE_NAME_PATTEN,// master..***
            "replicate_" + DATABASE_NAME_PATTEN,// replicate_***
            "secondary_" + DATABASE_NAME_PATTEN,// secondary_***
            "source_" + DATABASE_NAME_PATTEN,// source_***
            "sql_" + DATABASE_NAME_PATTEN,// sql_***
            "current_" + DATABASE_NAME_PATTEN,// current_***

    };
    public static final String[] STRICT_MATCHES = {
            "--" + SPACE_CHARS_PATTEN, // --
            "#" + SPACE_CHARS_PATTEN, // #
            SPACE_CHARS_PATTEN + "escape" + SPACE_CHARS_PATTEN + "'.'", // escape '*'

            CONST_CONDITION_COL, //  user=user ...
    };

    public static void assertEntry(String errorMsg, String value) {
        assertEntry(false, errorMsg, value);
    }

    public static void assertEntry(boolean strict, String errorMsg, String value) {
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
        if (strict) {
            badChars = STRICT_CHARTS;
            for (char ch : badChars) {
                String str = ch + "";
                String vstr = containsInjectForm(sql, str);
                if (vstr != null) {
                    throw new SqlFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
                }
            }
        }

        for (int i = 0; i < 32; i++) {
            char ch = (char) i;
            if(ch=='\n'||ch=='\r'||ch=='\t'){
                continue;
            }
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
        if (strict) {
            badStrs = STRICT_STRS;
            for (String badStr : badStrs) {
                String str = badStr;
                String vstr = containsInjectForm(sql, str);
                if (vstr != null) {
                    throw new SqlFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
                }
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
        if (strict) {
            badMatches = STRICT_MATCHES;
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
        assertEntry(false, errorMsg, value);
    }

}
