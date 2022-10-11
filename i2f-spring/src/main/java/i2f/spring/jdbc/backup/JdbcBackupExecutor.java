package i2f.spring.jdbc.backup;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.core.jdbc.core.JdbcProvider;
import i2f.core.jdbc.core.TransactionManager;
import i2f.core.jdbc.data.PageMeta;
import i2f.spring.jdbc.backup.apis.IInputResolver;
import i2f.spring.jdbc.backup.apis.IOutputResolver;
import i2f.spring.jdbc.backup.data.*;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/10/4 9:25
 * @desc
 */
@Slf4j
public class JdbcBackupExecutor {
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        File file = new File("./data.json");

        backup(file);
        recovery(file);
        rollback(file);
        clean(file);
    }


    public static void backup(File file) throws Exception {
        String text = readFileText(file, "UTF-8");
        JdbcBackupMeta meta = mapper.readValue(text, JdbcBackupMeta.class);
        executeBackup(meta);
    }

    public static void recovery(File file) throws Exception {
        String text = readFileText(file, "UTF-8");
        JdbcBackupMeta meta = mapper.readValue(text, JdbcBackupMeta.class);
        executeRecovery(meta);
    }

    public static void rollback(File file) throws Exception {
        String text = readFileText(file, "UTF-8");
        JdbcBackupMeta meta = mapper.readValue(text, JdbcBackupMeta.class);
        executeRollback(meta);
    }

    public static void clean(File file) throws Exception {
        String text = readFileText(file, "UTF-8");
        JdbcBackupMeta meta = mapper.readValue(text, JdbcBackupMeta.class);
        executeClean(meta);
    }


    public static String readFileText(File file, String charset) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
        StringBuilder builder = new StringBuilder((int) file.length());
        String line = null;
        while ((line = reader.readLine()) != null) {
            String tline = line.trim();
            if (!tline.startsWith("#")) {
                builder.append(line).append("\n");
            }
        }
        return builder.toString();
    }

    public static void executeBackup(JdbcBackupMeta meta) throws Exception {
        JdbcProvider jdbc = null;
        try {
            jdbc = new JdbcProvider(new TransactionManager(meta.datasource));
            jdbc.getTransactionManager().keepConnect(true);
            executeSelect(jdbc, meta.select);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jdbc != null) {
                jdbc.getTransactionManager().close();
            }
        }
    }

    public static void executeRecovery(JdbcBackupMeta meta) throws Exception {
        JdbcProvider jdbc = null;
        try {
            jdbc = new JdbcProvider(new TransactionManager(meta.datasource));
            jdbc.getTransactionManager().keepConnect(true);
            executeInsert(jdbc, meta.insert);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jdbc != null) {
                jdbc.getTransactionManager().close();
            }
        }
    }

    public static void executeRollback(JdbcBackupMeta meta) throws Exception {
        JdbcProvider jdbc = null;
        try {
            jdbc = new JdbcProvider(new TransactionManager(meta.datasource));
            jdbc.getTransactionManager().keepConnect(true);
            executeRollback(jdbc, meta.insert);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jdbc != null) {
                jdbc.getTransactionManager().close();
            }
        }
    }

    public static void executeClean(JdbcBackupMeta meta) throws Exception {
        JdbcProvider jdbc = null;
        try {
            jdbc = new JdbcProvider(new TransactionManager(meta.datasource));
            jdbc.getTransactionManager().keepConnect(true);
            executeClean(jdbc, meta.insert);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jdbc != null) {
                jdbc.getTransactionManager().close();
            }
        }
    }

    private static void executeClean(JdbcProvider jdbc, DbInsertMeta insert) throws Exception {
        DbInsertPrepareMeta prepare = insert.prepare;
        if (prepare == null) {
            return;
        }
        if (prepare.backup != null && prepare.backup) {
            boolean dropBak = true;
            if (dropBak) {
                String sql = "drop table " + insert.table + prepare.backupSuffix;
                jdbc.update(sql);
            }
        }
    }

    private static void executeRollback(JdbcProvider jdbc, DbInsertMeta insert) throws Exception {
        DbInsertPrepareMeta prepare = insert.prepare;
        if (prepare == null) {
            return;
        }
        if (prepare.backup != null && prepare.backup) {
            boolean truncOld = true;
            if (truncOld) {
                String sql = "delete from " + insert.table + " where 1=1 ";
                jdbc.update(sql);
            }

            boolean bakOld = true;
            if (bakOld) {
                String sql = "insert into " + insert.table + " select * from " + insert.table + prepare.backupSuffix;
                jdbc.update(sql);
            }
        }
    }

    private static void executeSelect(JdbcProvider jdbc, DbSelectMeta select) throws Exception {
        BasicIoMeta output = select.output;
        IOutputResolver resolver = null;
        String thisName = JdbcBackupExecutor.class.getName();
        String thisPkg = thisName.substring(0, thisName.lastIndexOf("."));
        List<String> resolverNames = new ArrayList<>();
        resolverNames.add(thisPkg + ".resolvers." + output.type + "OutputResolver");
        if (output.packages != null) {
            for (String pkg : output.packages) {
                resolverNames.add(pkg + "." + output.type + "OutputResolver");
            }
        }
        Exception resolverEx = null;
        for (String item : resolverNames) {
            try {
                Class<IOutputResolver> clazz = (Class<IOutputResolver>) Class.forName(item);
                resolver = clazz.newInstance();
                break;
            } catch (Exception e) {
                resolverEx = e;
            }
        }
        if (resolver == null && resolverEx != null) {
            throw resolverEx;
        }
        resolver.begin(output);
        if (select.pageable) {
            int index = 0;
            int size = select.pageSize == null ? 20000 : select.pageSize;
            while (true) {
                String sql = select.sql;
                PageMeta page = new PageMeta((long) index, (long) size);
                sql = sql.replace("${page.index}", page.index + "");
                sql = sql.replace("${page.size}", page.size + "");
                sql = sql.replace("${page.offset}", page.offset + "");
                sql = sql.replace("${page.end}", page.offsetEnd + "");
                try {
                    List<Map<String, Object>> list = jdbc.query(sql);
                    if (list.isEmpty()) {
                        break;
                    }
                    resolver.resolve(list, page);
                } catch (Exception e) {
                    throw e;
                } finally {

                }
                index++;
            }
        } else {
            String sql = select.sql;
            try {
                List<Map<String, Object>> list = jdbc.query(sql);
                if (!list.isEmpty()) {
                    resolver.resolve(list, null);
                }
            } catch (Exception e) {
                throw e;
            } finally {

            }
        }
        resolver.end();
    }

    private static void executeInsert(JdbcProvider jdbc, DbInsertMeta insert) throws Exception {
        BasicIoMeta output = insert.input;
        IInputResolver resolver = null;
        String thisName = JdbcBackupExecutor.class.getName();
        String thisPkg = thisName.substring(0, thisName.lastIndexOf("."));
        List<String> resolverNames = new ArrayList<>();
        resolverNames.add(thisPkg + ".resolvers." + output.type + "InputResolver");
        if (output.packages != null) {
            for (String pkg : output.packages) {
                resolverNames.add(pkg + "." + output.type + "InputResolver");
            }
        }
        Exception resolverEx = null;
        for (String item : resolverNames) {
            try {
                Class<IInputResolver> clazz = (Class<IInputResolver>) Class.forName(item);
                resolver = clazz.newInstance();
                break;
            } catch (Exception e) {
                resolverEx = e;
            }
        }
        if (resolver == null && resolverEx != null) {
            throw resolverEx;
        }

        boolean hasInited = false;

        resolver.begin(output);

        int batchSize = insert.batchCount == null ? 2000 : insert.batchCount;
        while (true) {
            List<Map<String, Object>> list = resolver.resolve();
            if (list == null) {
                break;
            }
            if (!hasInited) {
                prepareInsertAction(jdbc, insert);
            }

            int i = 0;
            int size = list.size();
            List<Map<String, Object>> batch = new LinkedList<>();
            int batchCount = 0;
            while (i < size) {
                batch.add(list.get(i));
                batchCount++;
                if (batchCount == batchSize) {
                    saveBatchData(jdbc, batch, insert);
                    batchCount = 0;
                    batch = new LinkedList<>();
                }
                i++;
            }

            if (batchCount > 0) {
                saveBatchData(jdbc, batch, insert);
                batchCount = 0;
                batch = new LinkedList<>();
            }
        }
    }

    private static void prepareInsertAction(JdbcProvider jdbc, DbInsertMeta insert) throws Exception {
        DbInsertPrepareMeta prepare = insert.prepare;
        if (prepare == null) {
            return;
        }
        if (prepare.backup != null && prepare.backup) {
            boolean dropBak = true;
            if (dropBak) {
                try {
                    String sql = "drop table " + insert.table + prepare.backupSuffix;
                    jdbc.update(sql);
                } catch (Exception e) {

                }
            }

            boolean bakNew = true;
            if (bakNew) {
                String sql = "create table " + insert.table + prepare.backupSuffix + " as select * from " + insert.table;
                jdbc.update(sql);
            }
        }
        if (prepare.truncate) {
            String sql = "delete from " + insert.table + " where 1=1";
            jdbc.update(sql);
        }
    }

    private static void saveBatchData(JdbcProvider jdbc, List<Map<String, Object>> batch, DbInsertMeta insert) throws Exception {
        StringBuilder sql = new StringBuilder();
        LinkedList<Object> params = new LinkedList<>();
        if (insert.mapping == null || insert.mapping.size() == 0) {
            readMappingByTargetTable(jdbc, insert);
        }
        sql.append(" insert into ").append(insert.table).append(" ( ");
        boolean isFirst = true;
        for (ColumnMappingMeta item : insert.mapping) {
            if (!isFirst) {
                sql.append(" , ");
            }
            sql.append(" ").append(item.column).append(" ");
            isFirst = false;
        }
        sql.append(" ) ");
        if (insert.unionMode != null && insert.unionMode) {
            sql.append(" select ");
            boolean isFirstElem = true;
            for (Map<String, Object> item : batch) {
                if (!isFirstElem) {
                    sql.append(" union all select ");
                }
                boolean isFirstColumn = true;
                for (ColumnMappingMeta col : insert.mapping) {
                    if (!isFirstColumn) {
                        sql.append(" , ");
                    }
                    sql.append(" ? ");
                    String name = col.column;
                    if (col.property != null && !"".equals(col.property)) {
                        name = col.property;
                    }
                    Object val = item.get(name);
                    params.add(val);
                    isFirstColumn = false;
                }
                sql.append(" from dual ");
                isFirstElem = false;
            }
        } else {
            sql.append(" values ");
            boolean isFirstElem = true;
            for (Map<String, Object> item : batch) {
                if (!isFirstElem) {
                    sql.append(" , ");
                }
                sql.append(" ( ");
                boolean isFirstColumn = true;
                for (ColumnMappingMeta col : insert.mapping) {
                    if (!isFirstColumn) {
                        sql.append(" , ");
                    }
                    sql.append(" ? ");
                    String name = col.column;
                    if (col.property != null && !"".equals(col.property)) {
                        name = col.property;
                    }
                    Object val = item.get(name);
                    params.add(val);
                    isFirstColumn = false;
                }
                sql.append(" ) ");
                isFirstElem = false;
            }
        }

        String sqlStr = sql.toString();
        jdbc.updateRaw(sqlStr, params);
    }

    private static void readMappingByTargetTable(JdbcProvider jdbc, DbInsertMeta insert) throws Exception {
        String sql = " select * from " + insert.table + " where 1!=1 ";
        try {
            String[] cols = jdbc.query(sql).getHeaders();
            insert.mapping = new ArrayList<>();
            for (String item : cols) {
                ColumnMappingMeta meta = new ColumnMappingMeta();
                meta.column = item;
                insert.mapping.add(meta);
            }
        } catch (Exception e) {
            throw e;
        } finally {

        }
    }


}
