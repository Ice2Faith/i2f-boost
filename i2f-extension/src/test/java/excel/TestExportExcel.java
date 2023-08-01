package excel;

import i2f.core.container.map.MapUtil;
import i2f.extension.wps.excel.easyexcel.complex.ExcelExportUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2023/8/1 10:20
 * @desc
 */
public class TestExportExcel {
    public static void main(String[] args){
        File dir=new File("D:\\IDEA_ROOT\\DevCenter\\i2f-boost\\i2f-extension\\src\\test\\java\\excel");
        File tplFile=new File(dir,"demo-tpl.xlsx");
        File outFile=new File(dir,"output.xlsx");
        String sheetName="导出测试";

        Map<String,Object> originData=new HashMap<>();
        originData.put("export",MapUtil.collect(new HashMap<>(),
                "name","管理员",
                "date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                ));
        originData.put("total",1024);
        originData.put("size",3);

        List<Map<String,Object>> list=new ArrayList<>();
        list.add(MapUtil.collect(new HashMap<>(), "name","张三","age",12,"tel","18011112222"));
        list.add(MapUtil.collect(new HashMap<>(), "name","李四","age",15,"tel","18011113333"));
        list.add(MapUtil.collect(new HashMap<>(), "name","王五","age",14,"tel","18011114444"));

        originData.put("list",list);

        TestDeptVo root=new TestDeptVo();
        root.setId(1L);
        root.setDeptKey("company");
        root.setDeptName("集团");
        root.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        root.setCreateBy("系统");

        TestDeptVo dept=new TestDeptVo();
        dept.setId(1001L);
        dept.setDeptKey("center");
        dept.setDeptName("总部中心");
        dept.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        dept.setCreateBy("系统");
        dept.setParent(root);


        originData.put("user",MapUtil.collect(new HashMap<>(),
                "nickname","管理员",
                "role",MapUtil.collect(new HashMap<>(),
                        "roleKey","admin",
                        "roleName","管理员"
                        ),
                "dept",dept,
                "list",list
        ));




        Object data=new ArrayList<>();
        data=originData;


        ExcelExportUtil.exportComplex(outFile,tplFile,0,sheetName,data);
    }


}
