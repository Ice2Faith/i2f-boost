package i2f.extension.wps.excel.easyexcel.controller;


import i2f.core.api.ApiResp;
import i2f.extension.spring.mvc.SpringMvcUtil;
import i2f.extension.wps.excel.easyexcel.util.ExportUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * @author ltb
 * @date 2021/10/18
 */
@Controller
@RequestMapping("export")
public class ExportController {
    public static final String EXPORT_CHECK_URL_PREFIX="export/checkFileIsOK";
    public static final String EXPORT_DOWNLOAD_URL_PREFIX="export/download";
    public static final long EXPORT_FILE_MAX_SAVE_TIME=1000*60*20;

    @ResponseBody
    @PostMapping(value = "checkFileIsOK/**")
    public void checkFileIsOK(HttpServletRequest request) throws Exception {

        String path= ExportUtil.getFilePathByRequest(request,EXPORT_CHECK_URL_PREFIX);
        path= URLDecoder.decode(path,"UTF-8");

        File file=ExportUtil.getWebFile(request,path);
        boolean exist=file.exists();
        SpringMvcUtil.respJsonObj(ApiResp.success(exist));
    }

    @GetMapping("download/**")
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path= ExportUtil.getFilePathByRequest(request,EXPORT_DOWNLOAD_URL_PREFIX);
        path= URLDecoder.decode(path,"UTF-8");

        ExportUtil.downloadFileByPath(path,request,response);

    }


}
