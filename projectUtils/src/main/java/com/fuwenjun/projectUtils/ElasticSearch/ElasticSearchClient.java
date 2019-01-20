package com.fuwenjun.projectUtils.ElasticSearch;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;



/**
 * elasticsearch rest client 允许实例化
 * 
 * @author lizhesy
 *
 */

public class ElasticSearchClient {

	private static final Logger log = LoggerFactory.getLogger(ElasticSearchClient.class);

	private RestClient client = null;

	private static int clientId = 1;
	
	private List<String> hosts = null;

	public ElasticSearchClient(List<String> hosts) {
		log.info("ElasticSearchRestClient clientId:" + clientId + " start init...");
		this.hosts = hosts;
		if (client == null) {
			initClient();
		}
	}

	private void initClient() {
		Random rand = new Random();
		int rid = rand.nextInt(hosts.size());
		//随机获查询地址
		String ip = hosts.get(rid).split(":")[0];
		String port = hosts.get(rid).split(":")[1];
		client = RestClient.builder(new HttpHost(ip, Integer.parseInt(port))).build(); 
		log.info("ElasticSearchClient:" + clientId + " init complete.");
		clientId++;
	}

	/*public boolean checkClientIsAlive() {
		try {
			// 构造header[]
			Header[] headers = { new BasicHeader("header", "value") };
			if (highLevelRestClient == null || !highLevelRestClient.ping(headers)) {
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
		return true;
	}*/
	
	public void closeClient(){
		try {
			client.close();
		} catch (IOException e) {
			log.info("关闭ES客户端失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据构造好的json实体获取json数据
	 * @param jsonstr
	 * @param method  //GET POST PUT DELETE...
	 * @param endpoint //索引名称及查询类型
	 * @return
	 * @throws IOException 
	 */
	public  JSONObject getJSONObjectByElasticSearchRestClient(String jsonstr,String method, String endpoint) throws IOException{
		if(client==null){
			initClient();
		}		
		HttpEntity entity = new NStringEntity(jsonstr, ContentType.APPLICATION_JSON);
        Response response = client.performRequest(  
                method,  
                endpoint,  
                Collections.<String, String>emptyMap(),
                entity
        );  
        HttpEntity httpClientEntity = response.getEntity();  
        String responseContent = EntityUtils.toString(httpClientEntity);
        JSONObject obj = JSONObject.fromObject(responseContent);
        client.close();
        return obj;
	}

	public static Date UTCStringtODefaultString(String UTCString) {
		try {
			//UTCString = UTCString.replace("Z", " UTC");
			SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			//SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = utcFormat.parse(UTCString);
			return date;
			//return defaultFormat.format(date);
		} catch (ParseException pe) {
			pe.printStackTrace();
			return null;
		}
	}
}
