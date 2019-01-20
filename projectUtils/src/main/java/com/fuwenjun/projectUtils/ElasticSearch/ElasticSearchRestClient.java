package com.fuwenjun.projectUtils.ElasticSearch;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchRestClient {
	
	private static final Logger log = LoggerFactory.getLogger(ElasticSearchRestClient.class);
	
	private RestHighLevelClient client = null;
	
	private BulkProcessor bulkProcessor = null;
	
	private static int clientId = 1;
	
	private List<String> hosts = null;
	
	public ElasticSearchRestClient(List<String> hosts) {
		log.info("ElasticSearchRestClient clientId:" + clientId + " start init...");
		this.hosts = hosts;
		if (client == null) {
			initClient();
		}
	}
	
	public RestHighLevelClient getClient(){
		if (client == null) {
			initClient();
		}
		return client;
	}
	
	public int getClientId(){
		return clientId;
	}
	
	private void initClient() {
		Random rand = new Random();
		int rid = rand.nextInt(hosts.size());
		//随机获查询地址
		String ip = hosts.get(rid).split(":")[0];
		String port = hosts.get(rid).split(":")[1];
		/*String ip =  "172.22.5.14";
        String port = "9203";*/
		RestClientBuilder builder = RestClient.builder(new HttpHost(ip, Integer.parseInt(port))); 

		builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
			@Override
			public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
				requestConfigBuilder.setConnectTimeout(ElasticSearchConstant.REQUEST_CONNECT_TIMEOUT);
				requestConfigBuilder.setSocketTimeout(ElasticSearchConstant.REQUEST_SOCKET_TIMEOUT);
				requestConfigBuilder.setConnectionRequestTimeout(ElasticSearchConstant.CONNECTION_REQUEST_TIMEOUT);
				return requestConfigBuilder;
			}
		});
		builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
			@Override
			public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
				return httpClientBuilder.setDefaultIOReactorConfig(
						IOReactorConfig.custom().setIoThreadCount(ElasticSearchConstant.HTTPCLIENT_IO_THREADCOUNT)// 线程数配置
								.setConnectTimeout(ElasticSearchConstant.HTTPCLIENT_CONNECT_TIMEOUT)
								.setSoTimeout(ElasticSearchConstant.HTTPCLIENT_SO_TIMEOUT).build());
			}
		});
		// 设置超时
		builder.setMaxRetryTimeoutMillis(ElasticSearchConstant.MAX_RETRY_TIMEOUT);
		// 构建high level client
		client = new RestHighLevelClient(builder);
		log.info("ElasticSearchClient:" + clientId + " init complete.");
		clientId++;
	}
	
	/**
     * 初始化elasticsearch批量接口
     */
    public void initBulk(BulkProcessor.Listener listener){
    	if(bulkProcessor==null){
    		bulkProcessor = BulkProcessor.builder(
    				client::bulkAsync,listener)  
		        .setBulkActions(ElasticSearchConstant.BULK_ACTIONS)   
		        .setBulkSize(new ByteSizeValue(ElasticSearchConstant.BULK_SIZE, ByteSizeUnit.MB))   
		        .setFlushInterval(TimeValue.timeValueSeconds(5))   
		        .setConcurrentRequests(ElasticSearchConstant.CONCURRENT_REQUESTS)
		        .setBackoffPolicy(
		            BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)) 
		        .build();
    	}
    }
	
	public void bulk(Map<String, Object> map, String index, String type) {
		IndexRequest indexRequest = new IndexRequest(index, type).source(map);
		bulkProcessor.add(indexRequest);
	}
	
	public void closeBulkAndClient() {
		try {
			// 阻塞至所有的请求线程处理完毕后，断开连接资源
			while (!bulkProcessor.awaitClose(30, TimeUnit.SECONDS)) {
				log.debug("bulk processer is closing,wait 30 seconds.");
			}
			client.close();
		} catch (InterruptedException e) {
			log.error("close bulk processer exception.");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("close elasticsearch restclient exception.");
			e.printStackTrace();
		} finally {
			log.info("bulk processer closed.");
		}
	}
	
	public void closeClient(){
		try {
			client.close();
		} catch (IOException e) {
			log.info("关闭ES客户端失败");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		SearchRequest searchRequest = new SearchRequest("globalcontentextract-*").types("info");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchPhraseQuery("taskId", "1084"));
		searchSourceBuilder.query(qb);
		searchSourceBuilder.size(10);
		searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
		searchRequest.source(searchSourceBuilder);
		String[] hosts = {"172.22.5.14:9201","172.22.5.14:9202"};
		ElasticSearchRestClient client = new ElasticSearchRestClient(Arrays.asList(hosts));
		SearchResponse searchResponse = client.getClient().search(searchRequest);
		SearchHit[] hits = searchResponse.getHits().getHits();
		/*for (SearchHit searchHit : hits) {
			Map<String, Object> rs = searchHit.getSourceAsMap();
			for (String key : rs.keySet()) {
				System.out.println("key: "+key);
				System.out.println("value: "+rs.get(key));
			}
		}*/
		System.out.println(searchResponse.getHits().getTotalHits());
		for(SearchHit hit:hits) {
			 System.out.println(hit.getSourceAsString());
		}
		client.closeClient();
	}
}
