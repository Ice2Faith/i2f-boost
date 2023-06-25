package i2f.springboot.zplugin.config.service.impl;


import i2f.core.convert.TreeConvertUtil;
import i2f.core.std.api.ApiPage;
import i2f.springboot.zplugin.config.dto.ConfigDto;
import i2f.springboot.zplugin.config.mapper.ConfigMapper;
import i2f.springboot.zplugin.config.service.ConfigService;
import i2f.springboot.zplugin.config.vo.ConfigVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/06/06 15:48
 * @desc 配置项 ServiceImpl
 */
@Service
@Transactional
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigMapper configMapper;

    /**
     * 查询配置项列表
     * @param configVo
     * @return
     */
    @Override
    public ApiPage<ConfigDto> qryConfig(ConfigVo configVo) {
        ApiPage ret=new ApiPage<>();
        ApiPage page=configVo.getPage();
        if(page!=null){
            page.beginPage();
            ret=page;
        }
        String filterType= configVo.getFilterType();
        String parentId=configVo.getParentEntryId();
        if(parentId!=null && !"".equals(parentId)){
            ConfigVo fstVo=new ConfigVo();
            fstVo.setEntryId(parentId);
            List<ConfigDto> list= configMapper.qryConfig(fstVo);
            List<ConfigDto> childs=getParentDicts(list);
            list.addAll(childs);
            ret.data(null,list);
        }else{
            List<ConfigDto> list=configMapper.qryConfig(configVo);
            if(page!=null){
                page.data(list);
            }else{
                ret.data(null,list);
            }
        }

        if("3".equals(filterType)){
            List list= TreeConvertUtil.list2Tree(ret.getList());
            ret.setList(list);
        }
        return ret;
    }

    private List<ConfigDto> getParentDicts(List<ConfigDto> list){
        List<ConfigDto> ret=new ArrayList<>();
        if(list==null || list.size()==0){
            return ret;
        }
        StringBuilder builder=new StringBuilder();
        for(ConfigDto item : list){
            builder.append(",")
                    .append(item.getEntryId());
        }
        String entryIds=builder.toString();
        if(entryIds.length()>0){
            entryIds=entryIds.substring(1);
        }
        ConfigVo vo=new ConfigVo();
        vo.setParentEntryId(entryIds);
        ret=configMapper.qryConfig(vo);
        List nlist=getParentDicts(ret);
        ret.addAll(nlist);
        return ret;
    }
}
