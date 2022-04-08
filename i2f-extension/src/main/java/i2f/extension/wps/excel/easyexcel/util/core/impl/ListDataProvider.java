package i2f.extension.wps.excel.easyexcel.util.core.impl;


import i2f.core.api.ApiPage;

import java.util.List;

/**
 * @author ltb
 * @date 2021/10/19
 */
public class ListDataProvider extends AbsDataProviderAdapter{
    private List data;
    private Class clazz;

    @Override
    public boolean supportPage() {
        return false;
    }

    public ListDataProvider(List data, Class clazz){
        this.data=data;
        this.clazz=clazz;
    }
    @Override
    public List getData(ApiPage page) {
        return data;
    }

    @Override
    public Class getDataClass() {
        return clazz;
    }
}
