package i2f.extension.wps.word;

import com.aspose.words.SaveFormat;
import i2f.extension.template.velocity.VelocityGenerator;
import i2f.extension.wps.formats.word.DocumentUtil;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/5/25 14:24
 * @desc
 */
public class DocumentExportUtil {

    public static final String XS_TPL_IMG_PREFIX = "XSTPLIMG";

    public static String getXsTplImgSpaceHoldString(int idx) {
        return String.format(XS_TPL_IMG_PREFIX + "%04d", idx);
    }

    /**
     * 将xml格式的word文档渲染导出为指定的格式
     * xsTplImgFileMap 中，key就是 XSTPLIMG%04d 的索引，value就是图片文件
     * @param xmlIs xml文档的输入流
     * @param charset 文档的字符集
     * @param params 渲染的参数
     * @param xsTplImgFileMap 图片的索引-图片文件映射
     * @param saveFormat 保存格式，是 com.aspose.words.SaveFormat 中定义的常量
     * @param os 输出流，输出结果
     * @throws Exception
     */
    public static void renderXmlWordTemplate(InputStream xmlIs,
                                             String charset,
                                             Map<String, Object> params,
                                             Map<Integer, File> xsTplImgFileMap,
                                             int saveFormat,
                                             OutputStream os) throws Exception {
        if (StringUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }
        if (saveFormat <= 0) {
            saveFormat = SaveFormat.DOCX;
        }
        String xmlWordContent = StreamUtils.copyToString(xmlIs, Charset.forName(charset));
        String tpl = processXsTplImgParams(xmlWordContent, xsTplImgFileMap);
        String out = VelocityGenerator.render(tpl, params);
        ByteArrayInputStream bis = new ByteArrayInputStream(out.getBytes(charset));
        DocumentUtil.convert(bis, saveFormat, os);
        os.close();
        xmlIs.close();
    }

    public static String processXsTplImgParams(String xmlWordContent, Map<Integer, File> xsTplImgFileMap) {
        for (Map.Entry<Integer, File> entry : xsTplImgFileMap.entrySet()) {
            Integer idx = entry.getKey();
            File file = entry.getValue();
            if (idx == null || idx < 0) {
                continue;
            }
            if (!file.exists()) {
                continue;
            }
            if (!file.isFile()) {
                continue;
            }
            try {
                String b64 = getFileAsBase64(file);
                if (b64 == null) {
                    continue;
                }
                String sh = getXsTplImgSpaceHoldString(idx);
                xmlWordContent = xmlWordContent.replaceAll(sh, b64);
            } catch (Exception e) {

            }
        }
        return xmlWordContent;
    }

    public static String getFileAsBase64(File file) throws IOException {
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            return null;
        }
        if (!file.isFile()) {
            return null;
        }
        byte[] imgBytes = StreamUtils.copyToByteArray(new FileInputStream(file));
        String b64 = Base64.getEncoder().encodeToString(imgBytes);
        return b64;
    }

}
