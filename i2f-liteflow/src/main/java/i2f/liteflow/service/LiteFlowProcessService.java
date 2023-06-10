package i2f.liteflow.service;

import i2f.liteflow.data.vo.LiteFlowProcessVo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/7 11:43
 * @desc
 */
public interface LiteFlowProcessService {
    LiteFlowProcessVo deploy(LiteFlowProcessVo webVo,String oper);

    LiteFlowProcessVo deployByJson(String json,String oper);

    LiteFlowProcessVo deployByJson(File file,String oper) throws IOException;

    LiteFlowProcessVo deployByJson(InputStream is,String oper) throws IOException;

    LiteFlowProcessVo process(Long processId);

    String exportAsJson(Long processId);

    void exportAsJson(Long processId, OutputStream os) throws IOException;

    void exportAsJson(Long processId,File file) throws IOException;

    Long processIdByPkey(String pkey);

    LiteFlowProcessVo processByPkey(String pkey);

    String exportAsJsonByPkey(String pkey);

    void exportAsJsonByPkey(String pkey, OutputStream os) throws IOException;

    void exportAsJsonByPkey(String pkey,File file) throws IOException;

    void dropHistoryVersions(String pkey);
}
