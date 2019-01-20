package com.fuwenjun.projectUtils.ElasticSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 本地测试类
 * 不与svn同步
 */
public class MyTest {
    public static void main(String[] args) throws IOException {
        String query = EsQueryTemplateHelper.load("parse2.json");
        List<String> url=new ArrayList<>();
        url.add("127.0.0.1");
        ElasticSearchClient client=new ElasticSearchClient(url);

        JSONObject json=client.getJSONObjectByElasticSearchRestClient(query, "GET",
                "/url_download_info-*/_search");
        System.out.println(json);
      //  List<Map<String,Object>> jsonTable=getJsonToTable(json,keys);
       // Map<Object,Object> info=ElasticSearchSource.getSource(json, 2);
        //System.out.println(info);
    }
    //template_data的数据结果
    private static JSONObject ParseSQLResult(Map<String, Object> re) {
        String que=(String) re.get("data_sql");
          int start=que.indexOf("{");
          String condition=que.substring(0, start);
          String [] params=condition.split(" ");
          String query=que.substring(start);
          List<String> url=new ArrayList<>();
          url.add("127.0.0.1");
          ElasticSearchClient client=new ElasticSearchClient(url);
         JSONObject json=null;
        try {
            json = client.getJSONObjectByElasticSearchRestClient(query, params[0].trim(),params[1].trim());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    private static List<Map<String, Object>> getJsonToTable(JSONObject json, List<String> keys) {
        // TODO Auto-generated method stub
        List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
        JSONObject aggregations=json.getJSONObject("aggregations");
        JSONArray buckets=getFirstArrayByKey(aggregations,"buckets");
        System.out.println("buckets");
        System.out.println(buckets.size());
        JSONObject row=buckets.getJSONObject(0);
        System.out.println(row);
        JSONArray rowBuckets=getFirstArrayByKey(row,"buckets");
        JSONObject rowjson=rowBuckets.getJSONObject(0);
        List<String>url=getKeys("url.keyword", rowjson);
        System.out.println(url.toString());
        System.out.println(row);

        /* for(int i=0;i<buckets.size();i++){
            JSONObject row=buckets.getJSONObject(i);
            System.out.println();
            Map<String,Object> resultRow=new HashMap<String,Object>();
            resultRow.put(keys.get(0), row.get("key"));
            JSONArray rowBuckets=getFirstArrayByKey(row, "buckets");
            for(int j=0;j<rowBuckets.size();j++){
                JSONObject bucketsRow=rowBuckets.getJSONObject(j);
                //resultRow.put(keys.get(1), rowBuckets.getString(index)("key"));

            }

        }*/
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

    public static List<String> getKeys(String key, JSONObject parseJson) {
        // TODO Auto-generated method stub
        List<String> results=new ArrayList<String>();
        Iterator<String> it=parseJson.keys();
        while(it.hasNext()){
            String nowKey=it.next();
            if(parseJson.get(nowKey) instanceof JSONObject){
                JSONObject json=parseJson.getJSONObject(nowKey);
                if(!json.isNullObject()){
                    if(json.containsKey(key)){
                        if(json.get(key) instanceof JSONArray){
                            JSONArray array=json.getJSONArray(key);
                            for(int i=0;i<array.size();i++){
                                results.add(array.getString(i));
                            }

                        }else{

                            results.add(json.getString(key));
                        }
                    }
                    results.addAll(getKeys(key, json));
                }
            }
            if(parseJson.get(nowKey) instanceof JSONArray){
                results.addAll(getKeysByJSONArray(key,parseJson.getJSONArray(nowKey)));
            }
        }
        return results;
    }

    private static List<String> getKeysByJSONArray(String key, JSONArray jsonArray) {
        // TODO Auto-generated method stub
        List<String> results=new ArrayList<String>();
        for(int i=0;i<jsonArray.size();i++){
            if(jsonArray.get(i) instanceof JSONObject){
                results.addAll(getKeys(key, jsonArray.getJSONObject(i)));
            }
            if(jsonArray.get(i) instanceof JSONArray){
                results.addAll(getKeysByJSONArray(key, jsonArray.getJSONArray(i)));
            }
        }
        return results;
    }

}
