package i2f.extension.wps.excel.easyexcel.util.core;

import i2f.core.api.ApiPage;

import java.util.List;

/**
 * @author ltb
 * @date 2021/10/19
 */
public interface IDataProvider {
    void preProcess();
    boolean supportPage();
    List getData(ApiPage page);
    Class getDataClass();
}
