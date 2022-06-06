package i2f.springboot.zplugin.file;

import i2f.core.j2ee.web.ServletContextUtil;
import i2f.core.str.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/5/18 14:58
 * @desc 通用文件管理
 */
@Slf4j
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private FileManager fileManager;

    public static final String FORWARD_PATH = "/file/forward";
    public static final String FORWARD_DATA_KEY = "forwardData";
    public static final String FORWARD_HTTP_STATUS_KEY ="forwardHttpStatus";

    @RequestMapping("/forward")
    public Object forward(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setStatus((int)request.getAttribute(FORWARD_HTTP_STATUS_KEY));
        Object obj = request.getAttribute(FORWARD_DATA_KEY);

        return obj;
    }

    public static void forward(HttpServletRequest request, HttpServletResponse response, int httpStatus, Object data) throws ServletException, IOException {
        request.setAttribute(FORWARD_DATA_KEY,data);
        request.setAttribute(FORWARD_HTTP_STATUS_KEY,httpStatus);
        request.getRequestDispatcher(FORWARD_PATH).forward(request,response);
    }

    @ApiOperation(value = "通用文件上传接口", notes = "通用文件上传接口")
    @PostMapping("/upload")
    public FileInfo upload(MultipartFile file,
                           @RequestParam(value = "moduleType", required = false) String moduleType,
                           @RequestParam(value = "nameSpace", required = false) String nameSpace,
                           HttpServletRequest request) throws IOException {
        initFileConfig(request);
        FileInfo dto = new FileInfo();
        dto.setModuleType(moduleType);
        dto.setNameSpace(nameSpace);

        dto = fileManager.saveFile(file, dto);

        return dto;
    }

    @ApiOperation(value = "通用文件上传接口", notes = "通用文件上传接口")
    @GetMapping("/download/{serverName}")
    public void download(@PathVariable("serverName") String serverName,
                         @RequestParam(value="fileName",required = false) String fileName,
                         @RequestParam(value = "moduleType", required = false) String moduleType,
                         @RequestParam(value = "nameSpace", required = false) String nameSpace,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        initFileConfig(request);
        FileInfo dto = new FileInfo();
        dto.setModuleType(moduleType);
        dto.setNameSpace(nameSpace);
        dto.setServerName(serverName);

        boolean ok = FileManager.verifySign(serverName);
        if (!ok) {
            forward(request,response,404,"非法的文件下载请求");
            return;
        }

        File file = fileManager.getFile(dto);

        String virtualFileName=file.getName();
        FileInfo meta= fileManager.getMetaByFile(serverName);
        if(meta!=null){
            String originFileName=meta.getFileName();
            if(originFileName!=null && !"".equals(originFileName)){
                virtualFileName=originFileName;
            }
        }
        if(fileName!=null && !"".equals(fileName)){
            virtualFileName=fileName;
        }

        if(!file.exists() || !file.isFile()){
            forward(request,response,404,"找不到文件");
            return;
        }

        FileManager.downloadFile(response, file, virtualFileName);
    }

    // 解析classpath写法的配置
    public void initFileConfig(HttpServletRequest request){
        String storePath= fileConfig.getStorePath();
        if(!storePath.startsWith("classpath:")){
            return;
        }
        String path=storePath.substring("classpath:".length());
        File contextPath = ServletContextUtil.getContextPath(request.getSession().getServletContext(), "");
        File relativePath=new File(StringUtil.pathGen(contextPath.getAbsolutePath(),path));
        String absPath=relativePath.getAbsolutePath();
        fileConfig.setStorePath(absPath);
    }

}
