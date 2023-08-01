package i2f.extension.wps.excel.easyexcel.complex;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import i2f.extension.wps.excel.easyexcel.complex.core.EasyExcelUtil;

import java.io.File;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2023/8/1 15:14
 * @desc
 */
public class ExcelExportUtil {
    /**
     * 复杂模板导出支持
     * 拓展模板语法说明
     * 这是基于easyexcel解析进行的拓展
     * 因此easyexcel默认的用法都支持
     * 也就是默认的 {name} {.age}
     * 特别的，当入参为Map或者Bean的类型时
     * 可能会有需求多个层次的数据进行模板渲染
     * easyexcel默认是不支持多层次的
     * 而是需要自己定义FillWrapper进行拓展
     * 这里也是基于这个原理
     * 场景说明：
     * 入参是一个Map，对应的JSON如下
     * data={
     *     name: 'admin',
     *     age: 22,
     *     role: {
     *         roleKey: 'root',
     *         name: '超级管理员',
     *         parent: {
     *             roleKey: 'sys',
     *             name: '系统'
     *         },
     *         perms:[
     *              {
     *                  permKey: 'home',
     *                  name: '主页'
     *              }
     *         ]
     *     },
     *     addressList:[
     *          {
     *              name: '建军路',
     *              address: '建军路338号'
     *          }
     *     ]
     * }
     * 现在，需要在一个excel中模板导出这些内容
     * 则，对应的模板表达式如下：
     * {name}
     * {age}
     * {role.roleKey}
     * {role.name}
     * {role$parent.roleKey}
     * {role$parent.name}
     * {role$perms.permKey}
     * {role$perms.name}
     * {addressList.name}
     * {addressList.address}
     * 也就是说，出去最后一级之前的层级，就是之前层级的路径
     * 比如：role.parent.roleKey 是实际的路径
     * 对应的解析路径，就是将.变为$即可
     * role$parent.roleKey
     * 那这有什么用呢？
     * 适用于，同一个sheet内，同时渲染多个部分的内容时
     * 比如举例中，同一个sheet中，展示了一个用户的多方面信息，
     * 有的是单个值，有的则是一个列表值
     * @param outputFile 输出文件
     * @param templateFile 模板文件
     * @param sheetNo 操作的sheet编号
     * @param sheetName 操作的sheet名称
     * @param data 数据，支持Map,Bean,Collection等
     */
    public static void exportComplex(File outputFile,
                                     File templateFile,
                                     int sheetNo,
                                     String sheetName,
                                     Object data){
        ExcelWriter excelWriter = EasyExcel.write(outputFile)
                .withTemplate(templateFile)
                .build();
        WriteSheet writeSheet = EasyExcel.write().sheet(sheetNo, sheetName).build();
        List<Object> list = EasyExcelUtil.recursiveFoundFillData(data);
        for (Object item : list) {
            excelWriter.fill(item,writeSheet);
        }

        excelWriter.finish();
    }

}
