package com.fuwenjun.projectUtils.ElasticSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.HandlerBase;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *es查询实例
 */
public class TestElasticSearch {

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws IOException {

        String query = EsQueryTemplateHelper.load("parse.json");
        //GET /url_download_info-*/_search 
        JSONObject parseJson=JSONObject.fromObject(query);
        JSONObject aggs=parseJson.getJSONObject("ag1gs");
        List<String> keys=new ArrayList<String>();
        //  System.out.println(parseJson.get("aggs") instanceof JSONObject) ;
        //  parseJson.getJSONObject("size");
        keys=getKeys("field", parseJson);
        keys.add("url.keyword");
        System.out.println(keys.toString());
        // keys=getKeys("field",parseJson);
        //System.out.println( parseJson.getJSONObject("size"));
      
        /**
         *获取查询结果 
         */
        String[] hosts = {"172.22.5.14:9203"};
        ElasticSearchClient client=new ElasticSearchClient(Arrays.asList(hosts));
//
        JSONObject json=client.getJSONObjectByElasticSearchRestClient(query, "GET",
                "/url_download_info-*/_search");
//        System.out.println(json);
//        List<Map<String,Object>> jsonTable=getJsonToTable(json,keys);

        //获取 _source
        getEsResult(json);
    
    }

    public static  List<Map<String,Object>> getEsResult(JSONObject json) {
        JSONObject aggregations = json.getJSONObject("aggregations");
        ElasticSearchSource elasticSearchSource = new ElasticSearchSource();
        System.out.println("result : " + elasticSearchSource.getSource(aggregations, 2));
        Map<Object,Object> result=elasticSearchSource.getSource(aggregations, 2);
        List<Map<String,Object>> answer=new ArrayList<>();
        Set<Object> resSet=result.keySet();
        for(Object k:resSet){
            Map<Object,Object> res=(Map<Object, Object>) result.get(k);
            Set<Object> rk=res.keySet();
            for(Object r:rk){
                Map<Object,Object> url= ( Map<Object,Object>) res.get(r);
                Map<String,Object> ans=new HashMap<>();
                ans.put("taskId", k.toString());
                ans.put("taskInstanceId.keyword", rk.toString());
                ans.put("url.keyword", url.get("_source").toString());
                answer.add(ans);
            }
            //System.out.println(res.keySet());
        }
        System.err.println(answer.toString());
        return answer;
    }        

    private static List<Map<String, Object>> getJsonToTable (JSONObject json, List<String> keys) {

//        List<Map<String,Object>> result = new ArrayList<>();
        JSONObject aggregations = json.getJSONObject("aggregations");
//        System.out.println(getDataMap(aggregations));

        JSONArray buckets = getFirstArrayByKey(aggregations,"buckets");
        System.out.println("buckets size : " + buckets.size());

        List<Object> result = new ArrayList<>();
        for (int i = 0; i < buckets.size(); i++) {

            JSONObject buckets_item = buckets.getJSONObject(i);
            result.add(buckets_item.get("key"));
            JSONArray rowBuckets = getFirstArrayByKey(buckets_item, "buckets");
            for(int j = 0; j < rowBuckets.size(); j++){
                JSONObject bucketsRow = rowBuckets.getJSONObject(j);
                //resultRow.put(keys.get(1), rowBuckets.getString(index)("key"));
            }
                    
        }

        return null;
    }

    private static JSONArray getFirstArrayByKey(JSONObject aggregations, String key) {
        // TODO Auto-generated method stub
        JSONArray result=null;
        Iterator<String> it=aggregations.keys();
        while(it.hasNext()){
            String nowKey=it.next();
            if(key.equals(nowKey)){
                System.out.println(aggregations.get(nowKey));
                if(aggregations.get(nowKey) instanceof JSONArray){
                    result=(JSONArray) aggregations.get(nowKey);
                    return result;
                }
            }
            if(aggregations.get(nowKey) instanceof JSONObject)
                result=getFirstArrayByKey(aggregations.getJSONObject(nowKey),key);
        }
        return result;
    }

    private static List<String> getKeys(String key, JSONObject parseJson) {
        // TODO Auto-generated method stub
        List<String> results=new ArrayList<String>();
        Iterator<String> it=parseJson.keys();
        while(it.hasNext()){
            String nowKey=it.next();
            if(parseJson.get(nowKey) instanceof JSONObject){
                JSONObject json=parseJson.getJSONObject(nowKey);
                if(json.containsKey(key)){
                    results.add(json.getString(key));
                }
                results.addAll(getKeys(key, json));
            }
        }
        return results;
    }

   /* private static JSONObject getBottomJson(String key, JSONObject parseJson) {
        // TODO Auto-generated method stub
        JSONObject result=parseJson.getJSONObject(key);
        while(!result.isNullObject()){
            result=result.getJSONObject(key);
        }
        return result;
    }
*/
}
