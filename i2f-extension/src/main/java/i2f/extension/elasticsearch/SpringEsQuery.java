package i2f.extension.elasticsearch;

import i2f.core.api.ApiPage;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/5/8 21:18
 * @desc
 */
public class SpringEsQuery {
    protected EsQuery esQuery;
    protected NativeSearchQueryBuilder builder;
    public SpringEsQuery(EsQuery esQuery){
        this.esQuery=esQuery;

    }
    public SpringEsQuery inflate(){
        this.builder=new NativeSearchQueryBuilder()
                .withQuery(esQuery.boolQueryBuilder);
        if(esQuery.index!=null && esQuery.size!=null){
            Pageable pageable= PageRequest.of(esQuery.index,esQuery.size);
            this.builder.withPageable(pageable);
        }
        for(Map.Entry<String,Boolean> item : esQuery.orders.entrySet()){
            order(item.getKey(), (item.getValue()?SortOrder.ASC:SortOrder.DESC));
        }
        return this;
    }
    public SpringEsQuery order(String fieldName, SortOrder direct){
        FieldSortBuilder sortBuilder= SortBuilders.fieldSort(fieldName)
                .order(direct);
        this.builder.withSort(sortBuilder);
        return this;
    }
    public NativeSearchQueryBuilder done(){
        return this.builder;
    }
    public<T> Page<T> doSearch(ElasticsearchRepository repDao){
        return repDao.search(builder.build());
    }

    public<T> ApiPage<T> respPage(ElasticsearchRepository repDao){
        ApiPage<T> ret=new ApiPage<>();
        Page<T> page=doSearch(repDao);
        Pageable pageInfo=page.getPageable();
        if(pageInfo!=null) {
            //Pageable 实现中，有一个org.springframework.data.domain.Unpaged是无法获取分页参数信息的，
            //也就是在不分页的时候，这时候会抛出UnsupportedOperationException，这里直接忽视异常即可
            try{
                ret.page(page.getPageable().getPageNumber(),page.getPageable().getPageSize());
            }catch(UnsupportedOperationException e){

            }
        }else{
            ret.page(this.esQuery.index,this.esQuery.size);
        }
        ret.data(page.getTotalElements(),page.getContent());
        return ret;
    }
}
