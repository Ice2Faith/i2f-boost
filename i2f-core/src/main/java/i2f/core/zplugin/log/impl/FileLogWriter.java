package i2f.core.zplugin.log.impl;

import i2f.core.zplugin.log.data.LogData;
import i2f.core.zplugin.log.enums.LogLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/3/28 19:57
 * @desc
 */
public class FileLogWriter extends AbstractPlainTextLogWriter {
    private ExecutorService pool;
    private File file;
    private boolean useDiffLogFile;
    public FileLogWriter(File file,boolean useDiffLogFile){
        this.file=file;
        this.useDiffLogFile=useDiffLogFile;
        pool= new ThreadPoolExecutor(2, 30,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }
    @Override
    public void writeTextLog(String text, LogData data) {
        WriteLog2FileTask task=new WriteLog2FileTask();
        task.file=file;
        task.useDiffLogFile=useDiffLogFile;
        task.text=text;
        task.data=data;
        pool.submit(task);
    }

    public static class WriteLog2FileTask implements Runnable{
        public File file;
        public boolean useDiffLogFile;
        public String text;
        public LogData data;
        @Override
        public void run() {
            String fileName=file.getName();
            int idx=fileName.lastIndexOf(".");
            if(idx>0){
                fileName=fileName.substring(0,idx);
            }
            File dir=file.getParentFile();
            if(!dir.exists()){
                dir.mkdirs();
            }
            File allFile=new File(dir,file.getName());
            write2File(text,allFile);
            if(useDiffLogFile){
                LogLevel level=data.getLevel();
                if(level==null || level.level()>=LogLevel.INFO.level()){
                    File infoFile=new File(dir,fileName+"-info.log");
                    write2File(text,infoFile);
                }else if(level.level()>=LogLevel.WARN.level()){
                    File warnFile=new File(dir,fileName+"-warn.log");
                    write2File(text,warnFile);
                }else if(level.level()>=LogLevel.ERROR.level()){
                    File errorFile=new File(dir,fileName+"-error.log");
                    write2File(text,errorFile);
                }else if(level.level()>=LogLevel.FATAL.level()){
                    File fatalFile=new File(dir,fileName+"-fatal.log");
                    write2File(text,fatalFile);
                }
            }
        }
        public synchronized void write2File(String text,File file){
            try{
                FileOutputStream fos=new FileOutputStream(file,true);
                fos.write(text.getBytes());
                fos.write("\n".getBytes());
                fos.flush();
                fos.close();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }


}
