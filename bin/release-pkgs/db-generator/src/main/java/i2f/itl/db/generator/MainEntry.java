package i2f.itl.db.generator;

import i2f.core.container.map.MapUtil;
import i2f.core.database.db.core.DbResolver;
import i2f.core.database.db.data.TableMeta;
import i2f.core.database.jdbc.core.JdbcMetaAdapter;
import i2f.core.database.jdbc.core.JdbcProvider;
import i2f.core.io.file.FileUtil;
import i2f.core.resource.ResourceUtil;
import i2f.extension.serialize.json.gson.GsonJsonSerializer;
import i2f.generator.GeneratorDriver;
import i2f.generator.data.JavaCodeContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2023/11/30 10:20
 * @desc
 */
public class MainEntry {
    public static final String CONF_PATH="application.properties";

    public static final String DATASOURCE_DRIVER="datasource.driver";
    public static final String DATASOURCE_URL="datasource.url";
    public static final String DATASOURCE_USERNAME="datasource.username";
    public static final String DATASOURCE_PASSWORD="datasource.password";

    public static final String GEN_TABLES="generate.tables";
    public static final String GEN_OUTPUT="generate.output";
    public static final String GEN_TYPE="generate.type";
    public static final String GEN_SINGLE="generate.is-single";
    public static final String GEN_TEMPLATE="generate.template";

    public static void main(String[] args) throws Exception {
        String conf = ResourceUtil.getClasspathResourceAsString(CONF_PATH, "UTF-8");
        ByteArrayInputStream is = new ByteArrayInputStream(conf.getBytes());
        Properties properties = new Properties();
        properties.load(is);
        is.close();

        String driver = properties.getProperty(DATASOURCE_DRIVER, "com.mysql.cj.jdbc.Driver");
        String url = properties.getProperty(DATASOURCE_URL, "jdbc:mysql://localhost:3306/sys?useAffectedRows=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
        String username = properties.getProperty(DATASOURCE_USERNAME, "root");
        String password = properties.getProperty(DATASOURCE_PASSWORD, "123456");

        System.out.println("---------------------------------------------------------");
        System.out.println("==== datasource");
        System.out.println("driver:"+driver);
        System.out.println("url:"+url);
        System.out.println("username:"+username);
        System.out.println("password:"+password);

        String tables = properties.getProperty(GEN_TABLES, "");

        String[] arr=tables.split(",|，");
        List<String> tableNames=new ArrayList<>();
        for (String item : arr) {
            String name=item.trim();
            if("".equals(name)){
                continue;
            }
            tableNames.add(name);
        }

        System.out.println("---------------------------------------------------------");
        System.out.println("==== tables");
        for(int i=0;i<tableNames.size();i+=1){
            System.out.println(i+": "+tableNames.get(i));
        }
        if(tableNames.isEmpty()){
            System.err.println("error, "+GEN_TABLES+" not config or is empty string.");
            return;
        }

        String fileName = properties.getProperty(GEN_OUTPUT, "./output/result.txt");
        File saveFile=new File(fileName);
        System.out.println("---------------------------------------------------------");
        System.out.println("==== output");
        System.out.println("file: "+saveFile.getAbsolutePath());

        String templateFileName = properties.getProperty(GEN_TEMPLATE,"default.vm");
        File templateFile = new File(templateFileName);
        System.out.println("---------------------------------------------------------");
        System.out.println("==== template");
        System.out.println("isFile:"+templateFile.isFile());
        System.out.println("file: "+templateFile.getAbsolutePath());

        JdbcMetaAdapter meta = new JdbcMetaAdapter(driver,url,username,password);

        String isSingle = properties.getProperty(GEN_SINGLE, "false");
        boolean isSingleMode=("true".equalsIgnoreCase(isSingle));
        System.out.println("---------------------------------------------------------");
        System.out.println("==== mode");
        System.out.println("is-single: "+isSingleMode);


        if(isSingleMode){
            File saveDir = saveFile.getParentFile();
            String name = saveFile.getName();
            if(!templateFile.isFile()){
                System.err.println("error, is-single mode must setting "+GEN_TEMPLATE);
                return;
            }
            String codeJson = ResourceUtil.getClasspathResourceAsString("/code.json", "utf-8");
            JavaCodeContext codeContext = (JavaCodeContext)GsonJsonSerializer.INSTANCE.deserialize(codeJson, JavaCodeContext.class);
            String tpl=FileUtil.loadTxtFile(templateFile);
            JdbcProvider jdbc = new JdbcProvider(meta);
            List<File> saveFileList=new ArrayList<>();
            try(Connection conn = jdbc.getConnection()) {
                for (String tableName : tableNames) {
                    TableMeta table = DbResolver.getTableMeta(conn, tableName);
                    if (table != null) {
                        String result = GeneratorDriver.generate(table,tpl,codeContext);

                        String fname=null;
                        int idx=result.indexOf("\n");
                        if(idx>=0){
                            String headLine = result.substring(0, idx).trim();
                            if(headLine.startsWith("#filename ")){
                                fname=headLine.substring("#filename ".length()).trim();
                                result=result.substring(idx);
                            }
                        }

                        if(fname==null || "".equals(fname)) {
                            fname = tableName + "+" + name;
                        }

                        idx=fname.lastIndexOf(".");
                        if(idx<0){
                            String tname=name;
                            if(tname.endsWith(".vm")){
                                tname=tname.substring(0,tname.length()-".vm".length());
                            }
                            idx=tname.lastIndexOf(".");
                            if(idx>=0){
                                String suffix=tname.substring(idx);
                                fname=fname+suffix;
                            }
                        }

                        File dstFile=new File(saveDir,fname);
                        FileUtil.useParentDir(dstFile);
                        FileUtil.save(result, dstFile);
                        saveFileList.add(dstFile);
                    }
                }
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("==== result");
            for (int i = 0; i < saveFileList.size(); i++) {
                System.out.println("outfile["+i+"]: "+saveFileList.get(i).getAbsolutePath());
            }
        }else{
            BatchTableRender originRender=new BatchTableRender() {
                @Override
                public String generate(List<TableMeta> tables) throws Exception {
                    String tpl = ResourceUtil.getClasspathResourceAsString("/tpl/doc/doc.html.vm", "UTF-8");
                    return GeneratorDriver.doc(tables,tpl);
                }
            };
            BatchTableRender customRender=originRender;
            if(templateFile.isFile()){
                String tpl=FileUtil.loadTxtFile(templateFile);
                customRender=new BatchTableRender() {
                    @Override
                    public String generate(List<TableMeta> tables) throws Exception {
                        return GeneratorDriver.doc(tables,tpl);
                    }
                };
            }

            Map<String, BatchTableRender> handlerMap= new HashMap<>();
            MapUtil.collect(handlerMap,
                    "drawio-er",GeneratorDriver::drawioEr,
                    "tables-doc",GeneratorDriver::tablesDoc,
                    "db-tables",originRender,
                    "custom-doc",customRender
            );

            String type = properties.getProperty(GEN_TYPE, "drawio.er");
            System.out.println("---------------------------------------------------------");
            System.out.println("==== type");
            if(!handlerMap.containsKey(type)){
                System.err.println("error, not support type ["+type+']');
                Set<String> set = handlerMap.keySet();
                System.out.println("type supports: "+ set);
            }
            System.out.println("type:"+type);

            BatchTableRender render=handlerMap.get(type);

            renderDbAsFile(meta, tableNames, saveFile,render);

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("==== result");
            System.out.println("outfile: "+saveFile.getAbsolutePath());
        }

        System.out.println("ok");
    }

    @FunctionalInterface
    public static interface BatchTableRender {
        String generate(List<TableMeta> tables) throws Exception;
    }
    private static void renderDbAsFile(JdbcMetaAdapter meta, List<String> tableNames, File saveFile, BatchTableRender render) throws Exception {
        List<TableMeta> tables=new ArrayList<>();

        JdbcProvider jdbc = new JdbcProvider(meta);
        try(Connection conn = jdbc.getConnection()) {

            for (String tableName : tableNames) {
                TableMeta table = DbResolver.getTableMeta(conn, tableName);
                if (table != null) {
                    tables.add(table);
                }
            }
        }

        String result = render.generate(tables);

        FileUtil.useParentDir(saveFile);
        FileUtil.save(result, saveFile);
    }

}
