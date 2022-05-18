package test.batch;

import i2f.core.batch.BatchProvider;
import i2f.core.batch.IBatchProcessor;
import i2f.core.batch.IBatchReader;
import i2f.core.batch.IBatchWriter;
import i2f.core.batch.impl.BatchWriterReaderAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/14 14:10
 * @desc
 */
public class TestBatchProvider {
    public static void main(String[] args){
        basicBatch();
    }

    private static void basicBatch() {
        BatchWriterReaderAdapter<String> step1=new BatchWriterReaderAdapter<String>();
        BatchProvider.batch(new IBatchReader<String>() {
                                private BufferedReader is;

                                @Override
                                public void create() {
                                    String file="D:\\IDEA_ROOT\\DevCenter\\i2f-boost\\i2f-extension\\pom.xml";
                                    try{
                                        is=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                                    }catch(Exception e){
                                        e.printStackTrace();
                                        System.out.println(e.getMessage());
                                    }
                                }

                                @Override
                                public void destroy() {
                                    if(is!=null){
                                        try{
                                            is.close();
                                        }catch(Exception e){
                                            e.printStackTrace();
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                }

                                @Override
                                public Collection<String> read() {
                                    String line=null;
                                    try{
                                        line=is.readLine();
                                        if(line==null){
                                            return null;
                                        }
                                        List<String> list=new ArrayList<>();
                                        list.add(line);
                                        return list;
                                    }catch(Exception e){
                                        return null;
                                    }
                                }
                            },
                10,
                new IBatchProcessor<String, String>() {
                    private int line=0;

                    @Override
                    public boolean beforeFilter(String val) {
                        return val!=null && !"".equals(val.trim());
                    }

                    @Override
                    public String process(String val) {
                        line++;
                        return "line:"+line+":\t"+val;
                    }

                    @Override
                    public boolean afterFilter(String val) {
                        return !val.contains("<!--") && (val.contains("dependency>") || val.contains("artifactId") || val.contains("groupId") || val.contains("version"));
                    }
                },
                step1);

        BatchProvider.batch(step1,5,null,new IBatchWriter<String>() {
            private int count=0;
            @Override
            public void write(Collection<String> col) {
                if(col==null){
                    return;
                }
                count++;
                for(String item : col){
                    System.out.println("batch:"+count+":\t"+item);
                }
            }
        });
    }
}
