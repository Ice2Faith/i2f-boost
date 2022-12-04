package i2f.extension.elasticsearch;

import i2f.core.api.ApiPage;
import i2f.core.array.Arrays;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/5/8 16:59
 * @desc
 */
public class EsManager {
    protected RestHighLevelClient client;
    public EsManager(RestHighLevelClient client){
        this.client=client;
    }

    public RestHighLevelClient getClient() {
        return client;
    }

    public EsManager setClient(RestHighLevelClient client) {
        this.client = client;
        return this;
    }

    /**
     * 连接字符串：
     * localhost:9200,192.168.1.53:9200
     * 可以多个主机，之间逗号分隔，每一个主机需要ip和端口
     * @param connectString
     * @return
     */
    public static RestHighLevelClient getClient(String connectString) {
        String[] hosts=connectString.split(",");
        List<HttpHost> list=new ArrayList<>();
        for(String item : hosts){
            String[] host=item.split(":",2);
            String ip=null;
            Integer port=null;
            if(host.length>=1){
                ip=host[0];
            }
            if(host.length>=2){
                try{
                    port=Integer.parseInt(host[1]);
                }catch(Exception e){

                }
            }
            if(port==null){
                port=9200;
            }
            if(ip!=null){
                list.add(new HttpHost(ip,port));
            }
        }
        HttpHost[] arr = Arrays.collect(list, HttpHost[].class);
        return getClient(arr);
    }
    public static RestHighLevelClient getClient(String host,int port){
        return getClient(new HttpHost(host,port));
    }
    public static RestHighLevelClient getClient(HttpHost... hosts){
        RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(hosts));
        return client;
    }
    public static void closeClient(RestHighLevelClient client) throws IOException {
        if(client!=null){
            client.close();
        }
    }
    ////////////////////////////////////////////
    public static EsManager manager(String connectString){
        return new EsManager(getClient(connectString));
    }
    public static EsManager manager(String host,int port){
        return new EsManager(getClient(host, port));
    }
    public static EsManager manager(HttpHost ... hosts){
        return new EsManager(getClient(hosts));
    }
    ////////////////////////////////////////////
    public boolean indexCreate(String indexName) throws IOException {
        CreateIndexRequest request=new CreateIndexRequest(indexName);
        CreateIndexResponse response=getClient().indices().create(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }
    public GetIndexResponse indexSearch(String indexName) throws IOException {
        GetIndexRequest request=new GetIndexRequest(indexName);
        GetIndexResponse response = getClient().indices().get(request, RequestOptions.DEFAULT);
        return response;
    }

    public boolean indexDelete(String indexName) throws IOException {
        DeleteIndexRequest request=new DeleteIndexRequest(indexName);
        AcknowledgedResponse response = getClient().indices().delete(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }
    public boolean indexExists(String indexName) throws IOException {
        GetIndexRequest request=new GetIndexRequest(indexName);
        return getClient().indices().exists(request, RequestOptions.DEFAULT);
    }
    ////////////////////////////////////////////
    public boolean recordsInsert(String indexName, String id, Map<String,Object> map) throws IOException {
        IndexRequest request=new IndexRequest();
        request.index(indexName);
        if(id!=null){
            request.id(id);
        }
        request.source(map, XContentType.JSON);
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        IndexResponse response = getClient().index(request, RequestOptions.DEFAULT);
        return response.status()== RestStatus.CREATED;
    }
    public boolean recordsInsert(String indexName,String id,String json) throws IOException {
        IndexRequest request=new IndexRequest();
        request.index(indexName);
        if(id!=null){
            request.id(id);
        }
        request.source(json, XContentType.JSON);
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        IndexResponse response = getClient().index(request, RequestOptions.DEFAULT);
        return response.status()== RestStatus.CREATED;
    }
    public boolean recordsUpdate(String indexName,String id,Map<String,Object> map) throws IOException {
        UpdateRequest request=new UpdateRequest();
        request.index(indexName).id(id);
        request.doc(map, XContentType.JSON);
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        UpdateResponse response = getClient().update(request, RequestOptions.DEFAULT);
        return response.status()== RestStatus.OK;
    }
    public boolean recordsUpdate(String indexName,String id,String json) throws IOException {
        UpdateRequest request=new UpdateRequest();
        request.index(indexName).id(id);
        request.doc(json, XContentType.JSON);
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        UpdateResponse response = getClient().update(request, RequestOptions.DEFAULT);
        return response.status()== RestStatus.OK;
    }
    public boolean recordsDelete(String indexName,String id) throws IOException {
        DeleteRequest request=new DeleteRequest();
        request.index(indexName).id(id);
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        DeleteResponse response = getClient().delete(request, RequestOptions.DEFAULT);
        return response.status()==RestStatus.OK;
    }
    public GetResponse recordsGet(String indexName,String id) throws IOException {
        GetRequest request=new GetRequest();
        request.index(indexName).id(id);
        GetResponse response = getClient().get(request, RequestOptions.DEFAULT);
        return response;
    }
    public Map<String,Object> recordsGetAsMap(String indexName,String id) throws IOException {
        return recordsGet(indexName,id).getSourceAsMap();
    }
    public String recordsGetAsString(String indexName,String id) throws IOException {
        return recordsGet(indexName,id).getSourceAsString();
    }
    ////////////////////////////////////////////
    public boolean recordsBatchInsertJson(String indexName,Map<String,String> docs) throws IOException {
        BulkRequest request=new BulkRequest();

        for(Map.Entry<String,String> item : docs.entrySet()){
            IndexRequest one=new IndexRequest();
            one.index(indexName)
                    .id(item.getKey())
                    .source(item.getValue(), XContentType.JSON);
            request.add(one);
        }

        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse response = getClient().bulk(request, RequestOptions.DEFAULT);

        return !response.hasFailures();
    }
    public boolean recordsBatchInsertMap(String indexName,Map<String,Map<String,Object>> docs) throws IOException {
        BulkRequest request=new BulkRequest();

        for(Map.Entry<String,Map<String,Object>> item : docs.entrySet()){
            IndexRequest one=new IndexRequest();
            one.index(indexName)
                    .id(item.getKey())
                    .source(item.getValue(), XContentType.JSON);
            request.add(one);
        }
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse response = getClient().bulk(request, RequestOptions.DEFAULT);

        return !response.hasFailures();
    }
    public boolean recordsBatchDelete(String indexName,String ... ids) throws IOException {
        BulkRequest request=new BulkRequest();

        for(String id : ids){
            DeleteRequest one=new DeleteRequest();
            one.index(indexName).id(id);
            request.add(one);
        }
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse response = getClient().bulk(request, RequestOptions.DEFAULT);

        return !response.hasFailures();
    }
    public boolean recordsBatchDelete(String indexName,List<String> ids) throws IOException {
        BulkRequest request=new BulkRequest();

        for(String id : ids){
            DeleteRequest one=new DeleteRequest();
            one.index(indexName).id(id);
            request.add(one);
        }

        BulkResponse response = getClient().bulk(request, RequestOptions.DEFAULT);

        return !response.hasFailures();
    }
    public boolean recordsBatchUpdateJson(String indexName,Map<String,String> docs) throws IOException {
        BulkRequest request=new BulkRequest();

        for(Map.Entry<String,String> item : docs.entrySet()){
            UpdateRequest one=new UpdateRequest();
            one.index(indexName)
                    .id(item.getKey())
                    .doc(item.getValue(), XContentType.JSON);
            request.add(one);
        }
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse response = getClient().bulk(request, RequestOptions.DEFAULT);

        return !response.hasFailures();
    }
    public boolean recordsBatchUpdateMap(String indexName,Map<String,Map<String,Object>> docs) throws IOException {
        BulkRequest request=new BulkRequest();

        for(Map.Entry<String,Map<String,Object>> item : docs.entrySet()){
            UpdateRequest one=new UpdateRequest();
            one.index(indexName)
                    .id(item.getKey())
                    .doc(item.getValue(), XContentType.JSON);
            request.add(one);
        }
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse response = getClient().bulk(request, RequestOptions.DEFAULT);

        return !response.hasFailures();
    }
    ///////////////////////////////////////////
    public SearchResponse search(String indexName,SearchSourceBuilder builder) throws IOException {
        SearchRequest request=new SearchRequest();
        request.indices(indexName);

        request.source(builder);

        SearchResponse response = getClient().search(request, RequestOptions.DEFAULT);
        return response;
    }
    public SearchHits searchHits(String indexName,SearchSourceBuilder builder) throws IOException {
        return search(indexName,builder).getHits();
    }
    public ApiPage<Map<String, Object>> searchAsMap(String indexName,SearchSourceBuilder builder) throws IOException {
        SearchHits hits=searchHits(indexName,builder);
        SearchHit[] data= hits.getHits();
        long total=hits.getTotalHits().value;
        List<Map<String,Object>> list=new ArrayList<>(data.length);
        for(SearchHit item : data){
            list.add(item.getSourceAsMap());
        }
        ApiPage<Map<String, Object>> ret=new ApiPage<>();
        ret.data(total,list);
        return ret;
    }
    public SearchResponse searchAll(String indexName) throws IOException {
        SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        return search(indexName,query);
    }
    public SearchHits searchAllHits(String indexName) throws IOException {
        SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        return searchHits(indexName,query);
    }
    public ApiPage<Map<String, Object>> searchAllAsMap(String indexName) throws IOException {
        SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        return searchAsMap(indexName,query);
    }

    public EsQuery query(){
        return EsQuery.query(this);
    }

    public EsBeanManager beanOps(){
        return new EsBeanManager(this);
    }
}
