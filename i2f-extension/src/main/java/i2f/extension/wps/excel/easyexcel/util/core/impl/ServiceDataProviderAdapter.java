package i2f.extension.wps.excel.easyexcel.util.core.impl;

import i2f.core.api.ApiPage;

import java.util.List;

/**
 * @author ltb
 * @date 2022/2/19 8:38
 * @desc
 * @type VO 一个继承ServiceExportBaseVo的实体类，也就是强制包含一些基础属性
 * @type SERVICE 调用获取数据的Service类型
 * @type DTO 需要返回的数据列表中Bean的类型
 * 使用时必须实现抽象方法 doRequestData，一般来说，在抽象方法中你需要做的事如下
 * List<DTO> data=SERVICE(VO,args);
 */
public abstract class ServiceDataProviderAdapter<PAGE_ARGUMENT extends ServicePageProvider, SERVICE, RETURN_TYPE> extends AbsDataProviderAdapter{
    private Class<RETURN_TYPE> returnBeanClass;
    private PAGE_ARGUMENT reqVo;
    private SERVICE service;
    private Object[] args;

    /**
     * 构造简单的Service数据提供者
     * @param returnBeanClass doRequestData方法返回值的Bean的类型
     * @param reqVo 请求的数据对象
     * @param service 提供数据获取能力的Service对象
     * @param args service可能需要的其他参数或运行需要的其他参数
     */
    public ServiceDataProviderAdapter(Class<RETURN_TYPE> returnBeanClass, PAGE_ARGUMENT reqVo, SERVICE service, Object ... args){
        super(args);
        this.returnBeanClass=returnBeanClass;
        this.reqVo=reqVo;
        this.service=service;
        this.args=args;
    }
    @Override
    public boolean supportPage() {
        PAGE_ARGUMENT post=reqVo;
        String exportMode= post.getExportMode();
        if(ServicePageProvider.EXPORT_MODE_ONLY_PAGE.equals(exportMode)){
            return false;
        }else if(ServicePageProvider.EXPORT_MODE_ALL.equals(exportMode)){
            return true;
        }
        return true;
    }

    @Override
    public List getData(ApiPage page) {
        PAGE_ARGUMENT post=reqVo;
        if(page!=null && page.valid()){
            post.setPage(page);
        }
        List<RETURN_TYPE> data=doRequestData(service,reqVo,args);
        return data;
    }

    @Override
    public Class getDataClass() {
        return returnBeanClass;
    }

    public abstract List<RETURN_TYPE> doRequestData(SERVICE service, PAGE_ARGUMENT reqVo, Object ... args);
}
