package i2f.translate.impl;

import i2f.core.database.jdbc.data.DBResultList;
import i2f.core.io.file.FileUtil;
import i2f.core.resource.ResourceUtil;
import i2f.core.type.str.Strings;
import i2f.core.type.str.data.RegexFindPartMeta;
import i2f.core.type.tuple.impl.Tuple2;

import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2023/5/12 10:07
 * @desc
 */
public class WordTranslator {
    public static final String DB_DRIVER_NAME = "org.sqlite.JDBC";
    public static final String DB_FILE_PATH = "./sqlite3/words.db";
    public static AtomicBoolean INITIALED = new AtomicBoolean(false);

    public static void initDb() throws Exception {
        if (INITIALED.get()) {
            return;
        }
        File dbFile = new File(DB_FILE_PATH);
        if (dbFile.exists()) {
            return;
        }
        if (!dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }
        InputStream is = ResourceUtil.getClasspathResourceAsStream("sqlite3/words.db");
        FileUtil.save(is, dbFile);
    }

    public static String getDriver() {
        return DB_DRIVER_NAME;
    }

    public static String getUrl() throws SQLException {
        try {
            initDb();
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        }
        File dbFile = new File(DB_FILE_PATH);
        String path = dbFile.getAbsolutePath();
        String urlPath = path.replaceAll("\\\\", "/");
        return "jdbc:sqlite:" + urlPath;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(getDriver());
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        }
        return DriverManager.getConnection(getUrl());
    }

    public static String en2cn(String en) throws SQLException {
        return en2cn(en, true, true);
    }

    public static String en2cn(String en, boolean checkMerge) throws SQLException {
        return en2cn(en, true, checkMerge);
    }

    /**
     * 将一段文本按照单词进行直接翻译
     * 因此单词顺序就是结果顺序
     *
     * @param en         字符串
     * @param checkMerge 是否检查连接的合并情况，例如camel，pascal,snake等命名
     * @return
     */
    public static String en2cn(String en, boolean keepSpace, boolean checkMerge) throws SQLException {
        StringBuilder builder = new StringBuilder();
        Connection conn = null;
        String[] lines = en.split("\n");
        try {
            boolean isFirst = true;
            for (String line : lines) {
                if (!isFirst) {
                    builder.append("\n");
                }
                isFirst = false;
                List<Tuple2<String, Boolean>> letters = splitLetter(line, keepSpace, checkMerge);
                for (Tuple2<String, Boolean> letter : letters) {
                    if (conn == null) {
                        conn = getConnection();
                    }
                    if (!letter.t2) {
                        builder.append(letter.t1);
                        continue;
                    }
                    if (letter.t1.length() == 1) {
                        builder.append(letter.t1.toUpperCase());
                        continue;
                    }
                    String search = letter.t1.toLowerCase();
                    boolean ownerType = false;
                    if (search.endsWith("'s")) {
                        search = search.substring(0, search.length() - 2);
                        ownerType = true;
                    }
                    DBResultList rs = null;
                    int halfLen = search.length() - search.length() / 2;
                    for (int i = 0; i < Math.max(halfLen, 3); i++) {
                        String sql = (
                                "select id,word,trans_pos,trans_cn,book_id,odr\n" +
                                        "from (\n" +
                                        "select id,word,trans_pos,trans_cn,book_id,0 odr\n" +
                                        "from ce_dict \n" +
                                        "where word match '*${keyword}*'\n" +
                                        "and book_id ='SoftwareEnglish'\n" +
                                        "\n" +
                                        "union\n" +
                                        "select id,word,trans_pos,trans_cn,book_id,1 odr\n" +
                                        "from ce_dict \n" +
                                        "where word match '*${keyword}*'\n" +
                                        "and book_id ='ComputerEnglish'\n" +
                                        "\n" +
                                        "UNION \n" +
                                        "select id,word,trans_pos,trans_cn,book_id,2 odr\n" +
                                        "from ce_dict \n" +
                                        "where word match '*${keyword}*'\n" +
                                        "and book_id !='ComputerEnglish'\n" +
                                        ") a order by odr,length(word),length(trans_cn)\n" +
                                        "limit 1"
                        ).replaceAll("\\$\\{keyword\\}", search.replaceAll("\'", "\'\'"));
                        PreparedStatement stat = conn.prepareStatement(sql);
                        ResultSet set = stat.executeQuery();
                        rs = DBResultList.of(set);
                        set.close();
                        stat.close();
                        Long count = rs.getCount();
                        if (count > 0) {
                            String cn = rs.getStringValue(0, "trans_cn");
                            if (cn != null && !"".equals(cn.trim())) {
                                break;
                            }
                        }
                        if (search.length() <= 1) {
                            break;
                        }
                        search = search.substring(0, search.length() - 1);

                    }

                    if (rs != null && rs.getCount() > 0) {
                        String cn = rs.getStringValue(0, "trans_cn");
                        if (cn != null && !"".equals(cn.trim())) {
                            cn = getCnStr(cn);
                            builder.append(cn);
                            if (ownerType) {
                                builder.append("的");
                            }
                        } else {
                            builder.append(letter.t1);
                        }
                    } else {
                        builder.append(letter.t1);
                    }
                }
            }
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return builder.toString();
    }

    public static String getCnStr(String cn) {
        if (cn == null) {
            return cn;
        }
        if (cn.matches("[a-zA-Z0-9]+")) {
            return cn;
        }
        StringBuilder builder = new StringBuilder();
        boolean open = false;
        int len = cn.length();
        for (int i = 0; i < len; i++) {
            char ch = cn.charAt(i);
            if (isChinese(ch)) {
                open = true;
                builder.append(ch);
            } else {
                if (open) {
                    break;
                }
            }
        }
        return builder.toString();
    }

    public static boolean isChinese(char ch) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
        if (Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS == ub
                || Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS == ub
                || Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS == ub
                || Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT == ub
                || Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A == ub
                || Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B == ub) {

            return true;
        }
        return false;
    }

    public static List<Tuple2<String, Boolean>> splitLetter(String text, boolean keepSpace, boolean checkMerge) {
        List<Tuple2<String, Boolean>> ret = new LinkedList<>();
        List<RegexFindPartMeta> parts = Strings.regexFindParts(text, "([-|_]?[a-zA-Z']+)+");
        for (RegexFindPartMeta item : parts) {
            if (!keepSpace) {
                String trim = item.part.trim();
                if ("".equals(trim)) {
                    continue;
                }
            }
            if (item.isMatch) {
                if (checkMerge) {
                    String str = item.part.replaceAll("-", "_");
                    str = Strings.toUnderScore(str);
                    String[] arr = str.split("_");
                    for (String let : arr) {
                        if ("".equals(let)) {
                            continue;
                        }
                        ret.add(new Tuple2<>(let, true));
                    }
                } else {
                    ret.add(new Tuple2<>(item.part, true));
                }
            } else {
                ret.add(new Tuple2<>(item.part, false));
            }
        }
        return ret;
    }
}
