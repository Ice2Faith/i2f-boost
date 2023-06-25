package i2f.extension.wps.excel.easyexcel.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import i2f.core.j2ee.web.ServletFileUtil;
import i2f.core.j2ee.web.ServletResponseUtil;
import i2f.core.std.api.ApiPage;
import i2f.extension.wps.excel.easyexcel.controller.ExportController;
import i2f.extension.wps.excel.easyexcel.util.core.IDataProvider;
import i2f.extension.wps.excel.easyexcel.util.core.IFilesManageProcessor;
import i2f.extension.wps.excel.easyexcel.util.core.impl.DefaultContextPathFilesManageProcessor;
import i2f.extension.wps.excel.easyexcel.util.core.impl.ListDataProvider;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ltb
 * @date 2021/10/18
 */
public class ExportUtil {
    private static IFilesManageProcessor filesManageProcessor=new DefaultContextPathFilesManageProcessor();

    public static final String EXPORT_EXCEL_RELATIVE_PATH="excel/export";
    public static String getFilePathByRequest(HttpServletRequest request,String prefix){
        String url=request.getRequestURI();
        String path="";
        int idx=url.indexOf(prefix);
        if(idx>=0){
            path=url.substring(idx+prefix.length());
        }

        if(path.contains("..")){
            throw new RuntimeException("bad directory found.");
        }
        return path;
    }

    public static File getWebFile(HttpServletRequest request, String path){
        return filesManageProcessor.getFile(path,request);
    }

    private static volatile ExecutorService threadPool=null;
    public static ExecutorService getThreadPool() {
        if(threadPool==null){
            synchronized (ExportUtil.class){
                if(threadPool==null){
                    threadPool=Executors.newFixedThreadPool(5);
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(threadPool!=null){
                                if(!threadPool.isShutdown()){
                                    threadPool.shutdown();
                                }
                            }
                        }
                    }));
                }
            }
        }
        return threadPool;
    }

    public static void submitTask(Runnable task) {
        getThreadPool().submit(task);
    }

    /**
     * 直接按照ExcelProperty的注解生成导出excel
     *
     * @param request
     * @param useSync
     * @param provider
     * @param excelName
     * @param sheetName
     * @return
     */
    public static WebExcelRespData writeExcelFile(HttpServletRequest request, boolean useSync, IDataProvider provider, String excelName, String sheetName) {
        String fileName = EXPORT_EXCEL_RELATIVE_PATH + "/" + excelName + ("-" + System.currentTimeMillis()) + ".xls";
        File file = getWebFile(request, fileName);
        File tmpFile = getWebFile(request, fileName + ".tmp");
        ExportTask task = new ExportTask(provider, file, tmpFile, sheetName);
        provider.preProcess();
        if (useSync) {
            submitTask(task);
        } else {
            task.run();
        }
        return genWebExcelRespData(fileName);
    }

    /**
     * 按照给定的模板Excel生成导出excel
     * 模板编写规则：
     * 填值的地方，使用{.fieldName}形式占位
     * 例如，
     * 导出行：
     * 编号 姓名 年龄
     * 对应的模板占位：
     * {.no} {.name} {.age}
     * 则传入的数据中，每个实体的no,name,age属性将会用来进行自动填充
     * 这种方式适用于，复杂表头的情况做导出
     * 可以参考：./tpl/demo-tpl.xlsx
     *
     * @param request
     * @param useSync
     * @param provider
     * @param excelName
     * @param sheetName
     * @param templateFile
     * @return
     */
    public static WebExcelRespData writeExcelFileWithTemplate(HttpServletRequest request, boolean useSync, IDataProvider provider, String excelName, String sheetName, File templateFile) {
        String fileName = EXPORT_EXCEL_RELATIVE_PATH + "/" + excelName + ("-" + System.currentTimeMillis()) + ".xls";
        File file = getWebFile(request, fileName);
        File tmpFile = getWebFile(request, fileName + ".tmp");
        ExportTask task = new ExportTask(provider, file, tmpFile, sheetName).setTemplateFile(templateFile);
        provider.preProcess();
        if (useSync) {
            submitTask(task);
        } else {
            task.run();
        }
        return genWebExcelRespData(fileName);
    }

    public static WebExcelRespData writeExcelFile(HttpServletRequest request, boolean useSync, List data, Class clazz, String excelName, String sheetName) {
        IDataProvider provider = new ListDataProvider(data, clazz);
        return writeExcelFile(request, useSync, provider, excelName, sheetName);
    }

    public static void exportAndResponse(HttpServletRequest request, HttpServletResponse response, IDataProvider provider, String excelName, String sheetName) throws IOException {
        WebExcelRespData excelData = ExportUtil.writeExcelFile(request, false, provider, excelName, sheetName);
        downloadFileByPath(excelData.getFileName(), request, response);
    }

    public static void downloadFileByPath(String path,HttpServletRequest request,HttpServletResponse response) throws IOException{
        File file=ExportUtil.getWebFile(request,path);

        if(!file.exists()){
            ServletFileUtil.responseNotFileFound(path,response);
            return;
        }

        response.reset();
        ServletResponseUtil.supportCors(response);

        ServletFileUtil.responseFileAttachment(file,MediaType.APPLICATION_OCTET_STREAM_VALUE,response);

        // TODO 考虑删除已经下载的Excel文件，并考虑过期时长
        long lastModify=file.lastModified();

        file.delete();
    }

    public static WebExcelRespData genWebExcelRespData(String fileName){
        String checkPrefix="/"+ ExportController.EXPORT_CHECK_URL_PREFIX +"/";
        String downloadPrefix="/"+ExportController.EXPORT_DOWNLOAD_URL_PREFIX+"/";

        String checkUrl=checkPrefix+fileName;
        String downloadUrl=downloadPrefix+fileName;
        return new WebExcelRespData(fileName,checkUrl,downloadUrl);
    }

    public static class ExportTask implements Runnable{
        private IDataProvider provider;
        private File file;
        private File tmpFile;
        private String sheetName;
        private File templateFile;

        public ExportTask(IDataProvider provider, File file, File tmpFile, String sheetName) {
            this.provider = provider;
            this.file = file;
            this.tmpFile = tmpFile;
            this.sheetName = sheetName;
        }

        public ExportTask setTemplateFile(File templateFile) {
            this.templateFile = templateFile;
            return this;
        }

        @Override
        public void run() {
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            boolean useTemplate = (templateFile != null && templateFile.exists() && templateFile.isFile());

            File parentDir = tmpFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            boolean isSupportPage = provider.supportPage();
            System.out.println("pageSupport:" + isSupportPage);
            int pageSize = 65535;
            if (!isSupportPage) {
                Class clazz = provider.getDataClass();
                List data=provider.getData(null);
                int size=data.size();
                if(size <= pageSize) {
                    if (useTemplate) {
                        ExcelWriter excelWriter = EasyExcel.write(tmpFile)
                                .withTemplate(templateFile)
                                .build();
                        WriteSheet writeSheet = EasyExcel.write().sheet(0, sheetName).build();
                        excelWriter.fill(data, writeSheet);
                        excelWriter.finish();
                    } else {
                        EasyExcel.write(tmpFile, clazz)
                                .sheet(sheetName)
                                .doWrite(data);
                    }
                } else {
                    ExcelWriter excelWriter = null;
                    // 处理使用模板的情况
                    if (useTemplate) {
                        excelWriter = EasyExcel.write(tmpFile)
                                .withTemplate(templateFile)
                                .build();
                    } else {
                        excelWriter = EasyExcel.write(tmpFile, clazz)
                                .build();
                    }
                    int curPageIndex = 0;
                    int count = 0;
                    List curData = new ArrayList(pageSize);
                    for (Object item : data) {
                        if (count == pageSize) {

                            WriteSheet writeSheet = EasyExcel.writerSheet(curPageIndex, sheetName + curPageIndex)
                                    .build();
                            if (useTemplate) {
                                excelWriter.fill(curData, writeSheet);
                            } else {
                                excelWriter.write(curData, writeSheet);
                            }

                            count = 0;
                            curPageIndex++;
                            curData.clear();
                        }
                        curData.add(item);
                        count++;
                    }
                    if (count > 0) {
                        WriteSheet writeSheet = EasyExcel.writerSheet(curPageIndex, sheetName + curPageIndex)
                                .build();
                        if (useTemplate) {
                            excelWriter.fill(curData, writeSheet);
                        } else {
                            excelWriter.write(curData, writeSheet);
                        }
                    }

                    excelWriter.finish();
                }
            } else {
                int curPageIndex = 0;

                Class clazz = provider.getDataClass();

                ExcelWriter excelWriter = null;
                if (useTemplate) {
                    excelWriter = EasyExcel.write(tmpFile)
                            .withTemplate(templateFile)
                            .build();
                } else {
                    excelWriter = EasyExcel.write(tmpFile, clazz)
                            .build();
                }

                while (true) {
                    ApiPage page = new ApiPage(curPageIndex, pageSize);
                    List data = provider.getData(page);
                    int dsize = data.size();
                    if (data == null || dsize == 0) {
                        break;
                    }

                    WriteSheet writeSheet = EasyExcel.writerSheet(curPageIndex, sheetName + curPageIndex)
                            .build();
                    if (useTemplate) {
                        excelWriter.fill(data, writeSheet);
                    } else {
                        excelWriter.write(data, writeSheet);
                    }

                    curPageIndex++;

                    if (dsize < pageSize) {
                        break;
                    }
                }

                excelWriter.finish();
            }
            if(file.exists()){
                file.delete();
            }
            parentDir=file.getParentFile();
            if(!parentDir.exists()){
                parentDir.mkdirs();
            }
            tmpFile.renameTo(file);
            System.out.println("exportDone: "+tmpFile.getAbsolutePath()+"\n\t-> "+file.getAbsolutePath());
        }

    }
}
