package i2f.extension.mybatis.page;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import i2f.core.std.api.ApiPage;

import java.util.List;

/**
 * @author ltb
 * @date 2021/8/10
 */
public class PageUtil {
    public static void beginPage(int pageNum,int pageSize){
        PageHelper.startPage(pageNum, pageSize);
    }
    public static PageInfo getPageData(List<?> list){
        return new PageInfo(list);
    }
    public static<T> ApiPage<T> getData(List<T> list){
        PageInfo<T> pageInfo=new PageInfo<T>(list);
        ApiPage<T> data=new ApiPage<T>(pageInfo.getPageNum()-1,
                pageInfo.getPageSize());
        data.prepare();
        data.data(pageInfo.getTotal(), pageInfo.getList());
        return data;
    }

    public static void beginPage(ApiPage<?> page){
        page.beginPage();
        beginPage(page.getIndex(),page.getSize());
    }
    public static<T> void getData(List<T> list,ApiPage<T> page){
        ApiPage<T> resp=getData(list);
        page.setIndex(resp.getIndex());
        page.setSize(resp.getSize());
        page.setOffset(resp.getOffset());
        page.setTotal(resp.getTotal());
        page.setList(resp.getList());
    }
}
