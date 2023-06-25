package com.i2f.demo.modules.export;

import i2f.core.container.map.Maps;
import i2f.extension.wps.excel.easyexcel.util.ExportUtil;
import i2f.extension.wps.excel.easyexcel.util.WebExcelRespData;
import i2f.extension.wps.excel.easyexcel.util.core.impl.ListDataProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/8/11 10:17
 * @desc
 */
@RestController
@RequestMapping("export")
public class ExportTestController {

    @RequestMapping("tpl")
    public void exportWithTemplate(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(Maps.hashMap("no", 1001,
                "name", "zhang",
                "age", 22));
        list.add(Maps.hashMap("no", 1002,
                "name", "li",
                "age", 23));

        WebExcelRespData data = ExportUtil.writeExcelFileWithTemplate(request, false,
                new ListDataProvider(list, Map.class),
                "人员信息导出", "人员信息",
                new File("D:\\IDEA_ROOT\\DevCenter\\i2f-boost\\i2f-test\\src\\main\\resources\\excel\\demo-tpl.xls"));

        File file = ExportUtil.getWebFile(request, data.getFileName());


        System.out.println(file.getAbsolutePath());
    }
}
