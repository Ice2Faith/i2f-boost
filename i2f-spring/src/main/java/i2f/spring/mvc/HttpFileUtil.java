package i2f.spring.mvc;

import i2f.core.io.file.core.FileMime;
import i2f.core.j2ee.web.ServletFileUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class HttpFileUtil {
    /**
     * 支持断点续传的下载方式
     * @param request 需要从请求中解析得到断点的请求范围
     * @param response 响应数据
     * @param responseFile 要响应回去的真实文件
     * @param virtualFileName 响应回去的文件名
     * @param mimeType 可为null的MIME类型，用于指定Content-Type,为null时将根据virtualFileName的后缀自动识别映射
     * @param allowCache 是否允许客户端缓存
     * @param useAttachment 是否指定为附件下载形式，否则就直接打开
     * @throws IOException
     */
    public static void downloadFileRangeSupport(HttpServletRequest request,
                                                HttpServletResponse response,
                                                File responseFile,
                                                String virtualFileName,
                                                String mimeType,
                                                boolean allowCache,
                                                boolean useAttachment
    ) throws IOException {
        ServletFileUtil.downloadFileRangeSupport(request, response, responseFile,
                virtualFileName, mimeType, allowCache, useAttachment);
    }

    /**
     * 使用流式下载文件方式（能够减少内存资源暂用）
     * @param is
     * @param virtualFileName
     * @return
     * @throws IOException
     */
    public static ResponseEntity responseFileInStreamMode(InputStream is, String virtualFileName,String mimeType) throws IOException {
        InputStreamResource inputStreamResource=new InputStreamResource(is);
        HttpHeaders httpHeaders=new HttpHeaders();
        if(null!=mimeType){
            httpHeaders.set("Content-Type",mimeType+";charset=UTF-8");
        }else{
            httpHeaders.set("Content-Type", FileMime.getMimeType(virtualFileName) +";charset=UTF-8");
        }

        httpHeaders.set("Content-Disposition","attachment; filename=" +java.net.URLEncoder.encode(virtualFileName, "UTF-8"));

        return new ResponseEntity(inputStreamResource,httpHeaders, HttpStatus.OK);
    }

    public static void responseNotFileFound(String fileName, HttpServletResponse response) throws IOException {
        ServletFileUtil.responseNotFileFound(fileName, response);
    }

    /**
     * 普通文件直接下载方式
     * @param is
     * @param closeStream
     * @param virtualFileName
     * @param response
     * @throws IOException
     */
    public static void responseAsFileAttachment(InputStream is,boolean closeStream,String virtualFileName,String mimeType, HttpServletResponse response) throws IOException {
        ServletFileUtil.responseAsFileAttachment(is, closeStream, virtualFileName, mimeType, response);
    }

    public static void responseFileAttachment(File filePath,String mimeType, HttpServletResponse response) throws IOException {
        ServletFileUtil.responseFileAttachment(filePath, mimeType, response);
    }

    public static String getMultipartFileName(MultipartFile multipartFile){
        return new File(multipartFile.getOriginalFilename()).getName();
    }
    public static File saveMultipartFile2ContextPath(MultipartFile multipartFile, HttpServletRequest request,String relativePath) throws IOException {
        return saveMultipartFile2ContextPath(multipartFile,request.getSession(),relativePath);
    }
    public static File saveMultipartFile2ContextPath(MultipartFile multipartFile, HttpSession session, String relativePath) throws IOException {
        File saveFile=ServletFileUtil.saveAsFile2ContextPath(multipartFile.getInputStream(), getMultipartFileName(multipartFile),session,relativePath);
        return saveFile;
    }
}
