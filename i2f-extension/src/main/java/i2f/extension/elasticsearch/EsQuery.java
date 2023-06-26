package i2f.extension.elasticsearch;

import i2f.core.container.array.ArrayUtil;
import i2f.core.std.api.ApiPage;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/5/8 18:22
 * @desc
 */
public class EsQuery {
    protected static final int MUST=1;
    protected static final int MUST_NOT=2;
    protected static final int SHOULD=3;
    protected static final int FILTER=4;

    protected int state=MUST;

    protected String indexName;
    protected List<String> includesCols=new ArrayList<>();
    protected List<String> excludesCols=new ArrayList<>();
    protected Integer index;
    protected Integer size;
    protected Integer offset;
    protected LinkedHashMap<String,Boolean> orders=new LinkedHashMap<>();

    protected BoolQueryBuilder boolQueryBuilder;

    protected SearchSourceBuilder builder;

    protected EsManager manager;
    protected EsBeanManager beanManager;

    public EsQuery(){

    }
    public EsQuery(EsManager manager){
        this.manager=manager;
    }
    public EsQuery(EsBeanManager beanManager){
        this.manager=beanManager.getManager();
        this.beanManager=beanManager;
    }
    public static EsQuery query(){
        return new EsQuery();
    }
    public static EsQuery query(EsManager manager){
        return new EsQuery(manager);
    }
    public static EsQuery queryBean(EsBeanManager beanManager){
        return new EsQuery(beanManager);
    }
    //////////////////////////////////////////////////

    public EsQuery select(String ... names){
        return cols(names);
    }
    public EsQuery from(String indexName){
        return index(indexName);
    }
    public EsQuery index(String indexName){
        this.indexName=indexName;
        return this;
    }
    public EsQuery cols(String ... names){
        for(String item : names){
            includesCols.add(item);
        }
        return this;
    }

    public EsQuery colsExclude(String ... names){
        for(String item : names){
            excludesCols.add(item);
        }
        return this;
    }


    //////////////////////////////////////////////////
    public EsQuery page(Integer index,Integer size){
        this.index=index;
        this.size=size;
        return this;
    }
    public EsQuery page(ApiPage<?> page){
        this.index=page.getIndex();
        this.size=page.getSize();
        return this;
    }
    //////////////////////////////////////////////////
    public EsQuery order(String name,boolean asc){
        this.orders.put(name,asc);
        return this;
    }
    public EsQuery asc(String ... names){
        for(String item : names){
            this.orders.put(item,true);
        }
        return this;
    }
    public EsQuery desc(String ... names){
        for(String item : names){
            this.orders.put(item,false);
        }
        return this;
    }

    /////////////////////////////////////////////
    public EsQuery where(){
        boolQueryBuilder= QueryBuilders.boolQuery();
        return this;
    }
    public EsQuery and(){
        this.state=MUST;
        return this;
    }
    public EsQuery or(){
        this.state=SHOULD;
        return this;
    }
    public EsQuery not(){
        this.state=MUST_NOT;
        return this;
    }
    public EsQuery must(){
        this.state=MUST;
        return this;
    }
    public EsQuery mustNot(){
        this.state=MUST_NOT;
        return this;
    }
    public EsQuery should(){
        this.state=SHOULD;
        return this;
    }
    public EsQuery filter(){
        this.state=FILTER;
        return this;
    }
    protected EsQuery stateProxy(QueryBuilder item){
        switch (this.state){
            case MUST:
                boolQueryBuilder.must(item);
                break;
            case MUST_NOT:
                boolQueryBuilder.mustNot(item);
                break;
            case SHOULD:
                boolQueryBuilder.should(item);
                break;
            case FILTER:
                boolQueryBuilder.filter(item);
                break;
            default:
                break;
        }
        return this;
    }
    /////////////////////////////////////////////

    public EsQuery eq(String name,Object val){
        return stateProxy(QueryBuilders.termQuery(name, val));
    }
    public EsQuery eqs(String name,Object ... vals){
        return stateProxy(QueryBuilders.termsQuery(name,vals));
    }
    public EsQuery like(String name,Object val){
        return stateProxy(QueryBuilders.fuzzyQuery(name, val));
    }
    public EsQuery likes(String name,Object ... vals){
        BoolQueryBuilder query=new BoolQueryBuilder();
        for(Object item : vals){
            query.should(QueryBuilders.fuzzyQuery(name,item));
        }
        return stateProxy(query);
    }
    public EsQuery gt(String name,Object val){
        return stateProxy(QueryBuilders.rangeQuery(name).gt(val));
    }
    public EsQuery lt(String name,Object val){
        return stateProxy(QueryBuilders.rangeQuery(name).lt(val));
    }
    public EsQuery gte(String name,Object val){
        return stateProxy(QueryBuilders.rangeQuery(name).gte(val));
    }
    public EsQuery lte(String name,Object val){
        return stateProxy(QueryBuilders.rangeQuery(name).lte(val));
    }
    public EsQuery range(String name,Object form,Object to){
        return stateProxy(QueryBuilders.rangeQuery(name).from(form).to(to));
    }
    public EsQuery match(String name,Object val){
        return stateProxy(QueryBuilders.matchQuery(name,val));
    }
    public EsQuery matchMulti(Object val,String ... names){
        return stateProxy(QueryBuilders.multiMatchQuery(val,names));
    }
    public EsQuery regex(String name,String regex){
        return stateProxy(QueryBuilders.regexpQuery(name,regex));
    }
    public EsQuery cond(QueryBuilder item){
        return stateProxy(item);
    }

    /////////////////////////////////////////////
    protected EsQuery applyPage(){
        if(index==null && size==null){
            return this;
        }
        if(index==null){
            index=0;
        }
        if(index!=null && size!=null){
            offset=index*size;
            builder.from(offset).size(size);
        }
        return this;
    }

    protected EsQuery applyOrder(){
        for(Map.Entry<String,Boolean> item : orders.entrySet()){
            builder.sort(item.getKey(),(item.getValue()? SortOrder.ASC:SortOrder.DESC));
        }
        return this;
    }

    protected EsQuery applyCols() {
        String[] includes = ArrayUtil.collect(includesCols, String[].class);
        String[] excludes = ArrayUtil.collect(excludesCols, String[].class);
        builder.fetchSource(includes, excludes);
        return this;
    }

    protected EsQuery applyWhere(){
        if(boolQueryBuilder!=null){
            builder.query(boolQueryBuilder);
        }
        return this;
    }

    public SearchSourceBuilder done(){
        builder=new SearchSourceBuilder();
        applyCols();
        applyWhere();
        applyOrder();
        applyPage();
        return builder;
    }

    //////////////////////////////////////////

    public SearchResponse search(EsManager manager) throws IOException {
        return manager.search(indexName,done());
    }

    public SearchHits searchHits(EsManager manager) throws IOException {
        return manager.searchHits(indexName,done());
    }

    public ApiPage<Map<String, Object>> searchAsMap(EsManager manager) throws IOException {
        return manager.searchAsMap(indexName,done());
    }

    public<T> ApiPage<T> searchAsBean(Class<T> beanClass,EsBeanManager beanManager) throws IOException {
        return beanManager.search(beanClass,done());
    }

    public SearchResponse search() throws IOException {
        return manager.search(indexName,done());
    }

    public SearchHits searchHits() throws IOException {
        return manager.searchHits(indexName,done());
    }

    public ApiPage<Map<String, Object>> searchAsMap() throws IOException {
        ApiPage<Map<String, Object>> ret= manager.searchAsMap(indexName,done());
        ret.page(index,size);
        ret.prepare();
        return ret;
    }

    public<T> ApiPage<T> searchAsBean(Class<T> beanClass) throws IOException {
        ApiPage<T> ret= beanManager.search(beanClass,done());
        ret.page(index,size);
        ret.prepare();
        return ret;
    }

    //////////////////////////////////
    public SpringEsQuery spring(){
        return new SpringEsQuery(this);
    }
}
