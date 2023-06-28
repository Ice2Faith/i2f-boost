package i2f.springboot.zplugin.file;

import i2f.core.io.stream.StreamUtil;
import i2f.spring.serialize.jackson.JacksonJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;

/**
 * @author ltb
 * @date 2022/5/19 8:48
 * @desc
 */
@Component
public class FileManager  {

    @Autowired
    private FileConfig fileConfig;


    public static final String DEFAULT_MODULE_TYPE_NAME = "DEFAULT";
    public static final String DEFAULT_NAME_SPACE_NAME = "PUBLIC";

    public static final long ENCRYPT_MAX_SIZE=2048;

    public FileInfo saveFile(MultipartFile file, FileInfo dto) throws IOException {
        return saveFile(file, fileConfig.getStorePath(), dto);
    }

    public File getFile(FileInfo dto) {
        return getFile(fileConfig.getStorePath(),dto);
    }

    public File getMetaFile(String serverName){
        return getMetaFile(fileConfig.getStorePath(),serverName);
    }

    public FileInfo getMetaByFile(String serverName) throws IOException {
        return getMetaByFile(fileConfig.getStorePath(),serverName);
    }

    public static String calcSign(String str) {
        long signNum = 177;
        for (int i = 0; i < str.length(); i++) {
            signNum = Math.abs(signNum * 17 + ((i * str.charAt(i)) * 27) % 2077);
        }
        String sign = String.format("%x", signNum);
        sign = sign.toLowerCase();
        return sign;
    }

    public static boolean verifySign(String str) {
        int idx = str.indexOf("-");
        if (idx < 0) {
            return false;
        }
        String sign = str.substring(0, idx);
        String name = str.substring(idx + 1);
        String vsign = calcSign(name);
        if (!vsign.equals(sign)) {
            return false;
        }
        return true;
    }

    public static String generateServerFileName(String fileName) {
        File file = new File(fileName);
        fileName = file.getName();
        String suffix = "";
        int idx = fileName.lastIndexOf(".");
        if (idx >= 0) {
            suffix = fileName.substring(idx);
        }
        suffix = suffix.toLowerCase();
        String basic = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String name = basic + suffix;
        String sign = calcSign(name);
        return sign + "-" + name;
    }

    public static FileInfo prepareFileInfo(FileInfo info) {
        String moduleType = info.getModuleType();
        String nameSpace = info.getNameSpace();
        if (moduleType == null || "".equals(moduleType)) {
            moduleType = DEFAULT_MODULE_TYPE_NAME;
        }
        if (nameSpace == null || "".equals(nameSpace)) {
            nameSpace = DEFAULT_NAME_SPACE_NAME;
        }
        info.setModuleType(moduleType);
        info.setNameSpace(nameSpace);
        return info;
    }

    public static File getExistsDir(String basePath, FileInfo dto) {
        prepareFileInfo(dto);
        File dir = new File(basePath);

        dir = new File(dir, dto.getModuleType());
        dir = new File(dir, dto.getNameSpace());

        if (!dir.exists() && !dir.isDirectory()) {
            // 创建目录
            dir.mkdirs();
        }
        return dir;
    }

    public static void streamCopyXorProxy(InputStream is,OutputStream os,long maxSize) throws IOException {
        long cnt=0;
        byte[] buf = new byte[4096];
        int len = 0;
        long fac=177;
        while ((len = is.read(buf)) > 0) {
            int i=0;
            while(cnt!=maxSize && i<len){
                buf[i]=(byte)((buf[i]^fac)&0x0ff);
                fac=(fac*31)+i;
                i++;
                cnt++;
            }
            os.write(buf, 0, len);
        }
        is.close();
        os.close();
    }

    public static void streamCopyEncrypt(InputStream is,OutputStream os,long maxSize) throws IOException {
        streamCopyXorProxy(is,os,maxSize);
    }

    public static void streamCopyDecrypt(InputStream is, OutputStream os, long maxSize) throws IOException {
        streamCopyXorProxy(is,os,maxSize);
    }

    public static void streamCopy(InputStream is,OutputStream os) throws IOException {
        byte[] buf = new byte[4096];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            os.write(buf, 0, len);
        }
        is.close();
        os.close();
    }

    public static void downloadFile(HttpServletResponse response, File file, String virtualFileName) throws IOException {
        response.setHeader("Content-Disposition","attachment; filename=" +java.net.URLEncoder.encode(virtualFileName, "UTF-8")); // 设置文件名称
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        InputStream is=new FileInputStream(file);
        OutputStream os= response.getOutputStream();
        streamCopyDecrypt(is,os,ENCRYPT_MAX_SIZE);
    }

    public static FileInfo saveFile(MultipartFile file, String basePath, FileInfo dto) throws IOException {
        File dir = getExistsDir(basePath, dto);

        File fname = new File(file.getOriginalFilename());
        String originName = fname.getName();
        String serverName = generateServerFileName(originName);
        dto.setStatus("1");
        dto.setServerName(serverName);
        dto.setFileName(originName);
        dto.setFileType(file.getContentType());

        File sfile = new File(dir, serverName);
        InputStream is = file.getInputStream();
        OutputStream os = new FileOutputStream(sfile);
        CheckedOutputStream cos = new CheckedOutputStream(os, new Adler32());
        streamCopyEncrypt(is,cos,ENCRYPT_MAX_SIZE);
        long cksum = cos.getChecksum().getValue();
        dto.setFileChecksum(String.format("%x", cksum));

        dto.setFileSize(sfile.length() + "");

        writeMetaFile(basePath,dto);

        return dto;
    }

    public static File getMetaFile(String basePath,String serverName){
        File metaFile=new File(basePath,serverName+".meta");
        return metaFile;
    }

    public static File writeMetaFile(String basePath,FileInfo dto) throws IOException {
        File metaFile=new File(basePath,dto.getServerName()+".meta");
        OutputStream fos=new FileOutputStream(metaFile);
        fos.write(JacksonJsonSerializer.INSTANCE.serialize(dto).getBytes("UTF-8"));
        fos.close();
        return metaFile;
    }

    public static FileInfo getMetaByFile(String basePath,String serverName) throws IOException {
        File metaFile=getMetaFile(basePath, serverName);
        if(metaFile.exists() && metaFile.isFile()){
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            FileInputStream fis=new FileInputStream(metaFile);
            StreamUtil.streamCopy(fis,bos);
            String json=new String(bos.toByteArray(),"UTF-8");
            return (FileInfo) JacksonJsonSerializer.INSTANCE.deserialize(json, FileInfo.class);
        }
        return null;
    }

    public static File getFile(String basePath, FileInfo dto) {
        File dir = getExistsDir(basePath, dto);
        File file = new File(dir, dto.getServerName());
        return file;
    }
}
