package i2f.stream.test;

import i2f.stream.Streaming;
import i2f.stream.impl.GeneratorIterator;
import i2f.stream.impl.Reference;
import i2f.stream.impl.SimpleEntry;

import java.io.File;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/2/23 17:22
 * @desc
 */
public class TestStreaming {
    public static void main(String[] args) throws Exception {
        String filePath="E:\\MySystemDefaultFiles\\Desktop\\30OfficeCenter\\103-NytRoot\\Agri131\\SampleSurvey\\SPSV-SVC\\spsv-biz\\src\\main\\java\\i2f\\stream\\Streaming.java";
        Streaming.of(new File(filePath),"UTF-8")

                .<String>process((item,collector)->{
                    String[] arr = item.split("\\s+");
                    for (String it : arr) {
                        if("".equals(it)){
                            continue;
                        }
                        collector.accept(it);
                    }
                })
                .filter((e)->e.matches("[a-zA-Z0-9]+"))
                .parallel()
                .map(String::toLowerCase)
                .forEach((item,index)->{
                    System.out.println(index+": "+item);
                });


        Streaming.of(0,1,2,3,4,5,6,7,8,9)
//                .mixed(Streaming.of(11,12,13,14,15,16,17,18,19))
                .viewWindow(2,3)
                .forEach((entry)->{
                    List<Integer> key = entry.getKey();
                    Map.Entry<Integer, Integer> value = entry.getValue();
                    System.out.println(key.get(value.getKey())+"="+key+":"+value);
                });

        Streaming.of(1,2,3,4,5,6,7,8,9)
                .slideWindow(3,2)
                .forEach((entry)->{
                    System.out.println("slide:"+entry.getKey()+"="+entry.getValue());
                });

        Streaming.of(1,2,3,4,5,6,7,8,9)
                .countWindow(3)
                .forEach((entry)->{
                    System.out.println("count:"+entry.getKey()+"="+entry.getValue());
                });

        Streaming.of(1,2,3,4,5,6,7,8,9)
                .conditionWindow(()->0,
                        val->(val-1)/4,
                        (old,val)->old==(int)val)
                .forEach((entry)->{
                    System.out.println("cond:"+entry.getKey()+"="+entry.getValue());
                });

        Streaming.ofDir(new File("E:\\MySystemDefaultFiles\\Desktop\\30OfficeCenter\\103-NytRoot\\Agri131\\SampleSurvey\\SPSV-SVC\\spsv-biz\\src\\main\\java\\i2f\\stream"))
                .recursive((file, collector)->{
                    if(file.isFile()){
                        return;
                    }
                    for (File item : file.listFiles()) {
                        collector.accept(item);
                    }
                })
                .ring((item,index)->{
                    System.out.println("c1:"+index+"="+item);
                },(item,index)->{
                    System.out.println("c2:"+index+"="+item);
                },(item,index)->{
                    System.out.println("c3:"+index+"="+item);
                });

        Streaming.of(new GeneratorIterator<>((collector)->{
            SecureRandom random=new SecureRandom();
            for (int i = 0; i < 10; i++) {
                collector.accept(Reference.of(random.nextInt(10)));
                try{
                    Thread.sleep(random.nextInt(300));
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
            collector.accept(Reference.finish());
        })).sort().print("origin:").countBy()
                .sort((v1,v2)->0-Long.compare(v1.getValue(),v2.getValue()))
                .forEach(System.out::println);

        Streaming.ofMethod(Streaming.class)
                .sysout();


        Streaming.ofGenerator((collector)->{
            SecureRandom random=new SecureRandom();
            long cnt=0;
            while (true) {
                collector.accept(Reference.of(new SimpleEntry<>(random.nextInt(100),++cnt)));
                try{
                    Thread.sleep(random.nextInt(10));
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }).sysout("value:");
    }
}
