package docuemnt;

import com.aspose.words.SaveFormat;
import i2f.extension.wps.word.DocumentExportUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/8/1 15:46
 * @desc
 */
public class TestExportDocument {
    public static void main(String[] args) throws Exception {
        File dir = new File("D:\\IDEA_ROOT\\DevCenter\\i2f-boost\\i2f-extension\\src\\test\\java\\docuemnt");
        File wordFile = new File(dir, "demo-tpl.xml");

        File officeMarkFile = new File(dir, "office-mark.png");


        File pdfFile = new File(dir, "output.pdf");
        File pngFile = new File(dir, "output.png");
        File docxFile = new File(dir, "output.docx");

        Map<String, Object> params = new HashMap<>();
        Map<Integer, File> xsTplImgFileMap = new HashMap<>();

        params.put("dept", "人事");
        params.put("name", "张三");
        params.put("age", "23");
        params.put("tel", "18011112222");
        params.put("job", "软件测试");
        params.put("address", "建军路88号");
        params.put("limit", "2");
        params.put("begin", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        params.put("end", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        params.put("mgt", "行政");
        params.put("recv", "李四");
        params.put("publish", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));


        xsTplImgFileMap.put(1, officeMarkFile);

        DocumentExportUtil.renderXmlWordTemplate(new FileInputStream(wordFile), "UTF-8",
                params, xsTplImgFileMap,
                SaveFormat.PDF, new FileOutputStream(pdfFile));

        DocumentExportUtil.renderXmlWordTemplate(new FileInputStream(wordFile), "UTF-8",
                params, xsTplImgFileMap,
                SaveFormat.PNG, new FileOutputStream(pngFile));

        DocumentExportUtil.renderXmlWordTemplate(new FileInputStream(wordFile), "UTF-8",
                params, xsTplImgFileMap,
                SaveFormat.DOCX, new FileOutputStream(docxFile));
    }

}
