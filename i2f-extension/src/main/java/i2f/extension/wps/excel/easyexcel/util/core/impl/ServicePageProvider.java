package i2f.extension.wps.excel.easyexcel.util.core.impl;

import i2f.core.api.ApiPage;

/**
 * @author ltb
 * @date 2022/2/19 8:39
 * @desc
 */
public interface ServicePageProvider {
    String EXPORT_MODE_ONLY_PAGE="1";
    String EXPORT_MODE_ALL="2";
//    protected ApiPage page;
//    protected String exportMode; // 1 仅分页数据，2 全量数据

    void setPage(ApiPage<?> page);
    String getExportMode();
}
