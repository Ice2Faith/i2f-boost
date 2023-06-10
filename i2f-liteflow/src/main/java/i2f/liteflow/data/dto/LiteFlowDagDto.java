package i2f.liteflow.data.dto;

import i2f.liteflow.consts.LiteFlowNodeType;
import i2f.liteflow.data.dom.LiteFlowDagDo;
import i2f.liteflow.data.dom.LiteFlowNodeDo;
import i2f.liteflow.data.vo.LiteFlowDagVo;
import i2f.liteflow.data.vo.LiteFlowNodeVo;
import i2f.liteflow.data.vo.LiteFlowProcessVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2023/6/7 17:46
 * @desc
 */
@Data
@NoArgsConstructor
public class LiteFlowDagDto {
    /**
     * 流程
     */
    protected LiteFlowProcessVo process;
    /**
     * 节点
     */
    protected Map<String, LiteFlowNodeDo> nodeMap=new HashMap<>();
    /**
     * 节点的入度
     * 当前节点是终点
     */
    protected Map<String, Set<String>> inMap=new HashMap<>();
    /**
     * 节点的出度
     * 当前节点是起点
     */
    protected Map<String,Set<String>> outMap=new HashMap<>();

    public static LiteFlowDagDto parse(LiteFlowProcessVo process){
        LiteFlowDagDto ret=new LiteFlowDagDto();
        ret.process=process;
        for (LiteFlowNodeVo item : process.getNodes()) {
            String pkey = item.getPkey();
            ret.nodeMap.put(pkey,item);
        }
        for (LiteFlowNodeVo node : process.getNodes()) {
            String pkey = node.getPkey();
            if(!ret.inMap.containsKey(pkey)){
                ret.inMap.put(pkey,new HashSet<>());
            }
            if(!ret.outMap.containsKey(pkey)){
                ret.outMap.put(pkey,new HashSet<>());
            }
        }
        for (LiteFlowDagDo item : process.getDags()) {
            String bkey = item.getBeginNodePkey();
            String ekey = item.getEndNodePkey();

            ret.inMap.get(ekey).add(bkey);

            ret.outMap.get(bkey).add(ekey);
        }
        return ret;
    }

    public String beginKey(){
        for (Map.Entry<String, LiteFlowNodeDo> entry : nodeMap.entrySet()) {
            if(entry.getValue().getNodeType()== LiteFlowNodeType.BEGIN.code()){
                return entry.getKey();
            }
        }
        return null;
    }

    public LiteFlowDagVo getDag(String beginKey,String endKey){
        for (LiteFlowDagVo dag : process.getDags()) {
            String bkey = dag.getBeginNodePkey();
            String ekey = dag.getEndNodePkey();
            if(bkey.equals(beginKey) && ekey.equals(endKey)){
                return dag;
            }
        }
        return null;
    }
}
