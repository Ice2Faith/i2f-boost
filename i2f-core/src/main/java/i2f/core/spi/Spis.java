package i2f.core.spi;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

public class Spis {

    public static List<Object> loadSpiComponents(){
        List<Object> ret=new LinkedList<>();
        List<SpiComponent> components = load(SpiComponent.class);
        List<SpiComponentFactory> factories = load(SpiComponentFactory.class);
        for (SpiComponent item : components) {
            ret.add(item);
        }
        for (SpiComponentFactory item : factories) {
            ret.add(item);
        }
        return ret;
    }

    public static final String SPI_PATH="META-INF/services/";
    public static<T> List<T> load(Class<T> clazz){
        return load(clazz,true);
    }
    public static<T> List<T> load(Class<T> clazz,boolean ignoreFailure){
        List<T> ret=new LinkedList<>();
        ServiceLoader<T> services = ServiceLoader.load(clazz);
        Iterator<T> iterator = services.iterator();
        while(true){
            try {
                boolean ok = iterator.hasNext();
                if(!ok){
                    break;
                }
                T item = iterator.next();
                ret.add(item);
            }catch (Error e){
                if(!ignoreFailure){
                    throw e;
                }
            }
        }
        return ret;
    }

    public static<T> void makeSpiFile(Class<T> interfaceClass,Class<? extends T> ... implementsClass) throws IOException {
        makeSpiFile(".",interfaceClass,implementsClass);
    }
    public static<T> void makeSpiFile(String path,Class<T> interfaceClass,Class<? extends T> ... implementsClass) throws IOException {
        File dir=new File(path,SPI_PATH);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String fileName=interfaceClass.getName();
        File file=new File(dir,fileName);
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
        for (Class<? extends T> clazz : implementsClass) {
            String line= clazz.getName();
            writer.write(line);
            writer.write("\n");
        }
        writer.flush();
        writer.close();

    }
}
