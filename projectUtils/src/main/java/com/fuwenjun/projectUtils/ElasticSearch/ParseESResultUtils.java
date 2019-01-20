package com.fuwenjun.projectUtils.ElasticSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *可能会有用的解析方法 
 *
 */
public class ParseESResultUtils {
    
    
    /**
     * 
     * @param key  :json中定位的可以值,返回包含该key的当层的jsonObject对象
     * @param parseJson  待查询的json数据
     * @return   //
     */
    public static List<JSONObject> getValueWithFirstFloor(String key,JSONObject parseJson){
        List <JSONObject> results=new ArrayList<JSONObject>();
        if(!parseJson.isNullObject()){
            if(parseJson.containsKey(key)){
                //发现key就停止递归方法
               /* if(parseJson.get(key) instanceof JSONArray){
                    JSONArray array=parseJson.getJSONArray(key);
                    for(int i=0;i<array.size();i++){
                        results.add(array.get(i));
                    }
                }else{
                    results.add(parseJson.get(key));
                }*/
                results.add(parseJson);
                return results;
            }
        }
        Iterator<String> it=parseJson.keys();
        while(it.hasNext()){
            String nowKey=it.next();
            if(parseJson.get(nowKey) instanceof JSONObject){
                JSONObject json=parseJson.getJSONObject(nowKey);
                if(!json.isNullObject()){
                    if(json.containsKey(key)){
                        //一旦发现包含该key就停止
                       /* if(json.get(key) instanceof JSONArray){
                            JSONArray array=json.getJSONArray(key);
                            for(int i=0;i<array.size();i++){
                                results.add(array.get(i));
                            }
                        }else{
                            results.add(json.get(key));
                        }*/
                        results.add(json);
                        return results;
                    }
                    results.addAll(getValueWithFirstFloor(key, json));
                }
            }
            if(parseJson.get(nowKey) instanceof JSONArray){
                results.addAll(getValueWithFirstFloorJSONArray(key,parseJson.getJSONArray(nowKey)));
            }
        }
        return results;
    }
    
    
    
    /**
     * 上一方法的配合方法
     * @param key  :json中定位的可以值,返回包含该key的当层的jsonObject对象
     * @param parseJson  待查询的json数据
     * @return   //
     */
    private static List<JSONObject> getValueWithFirstFloorJSONArray(String key,JSONArray jsonArray){
        List<JSONObject> results=new ArrayList<JSONObject>();
        for(int i=0;i<jsonArray.size();i++){
            if(jsonArray.get(i) instanceof JSONObject){
                results.addAll(getValueWithFirstFloor(key, jsonArray.getJSONObject(i)));
            }
            if(jsonArray.get(i) instanceof JSONArray){
                results.addAll(getValueWithFirstFloorJSONArray(key, jsonArray.getJSONArray(i)));
            }
        }
        return results;
    }

    /**
     * 获取所有key值为传入参数的当层对象
     * @param key  :传入的key值
     * @param parseJson 待解析的对象
     * @return
     */
    //获取key值得value,无论是jsonArray或者其他
    public static List<Object> getValuesByKeyJSONObject(String key,JSONObject parseJson){
        List <Object> results=new ArrayList<Object>();
        if(!parseJson.isNullObject()){
            if(parseJson.containsKey(key)){
                if(parseJson.get(key) instanceof JSONArray){
                    JSONArray array=parseJson.getJSONArray(key);
                    for(int i=0;i<array.size();i++){
                        results.add(array.get(i));
                    }
                }else{
                    results.add(parseJson.get(key));
                }
            }
        }
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
                                results.add(array.get(i));
                            }
                        }else{
                            results.add(json.get(key));
                        }
                    }else{
                        results.addAll(getValuesByKeyJSONObject(key, json));
                    }
                }
            }
            if(parseJson.get(nowKey) instanceof JSONArray){
                results.addAll(getValuesByKeyJSONArray(key,parseJson.getJSONArray(nowKey)));
            }
        }
        
        return results;
    }
    /**
     * 上一方法的配合方法
     * @param key
     * @param jsonArray
     * @return
     */
    private static List<Object> getValuesByKeyJSONArray(String key, JSONArray jsonArray) {
        List<Object> results=new ArrayList<Object>();
        for(int i=0;i<jsonArray.size();i++){
            if(jsonArray.get(i) instanceof JSONObject){
                results.addAll(getValuesByKeyJSONObject(key, jsonArray.getJSONObject(i)));
            }
            if(jsonArray.get(i) instanceof JSONArray){
                results.addAll(getValuesByKeyJSONArray(key, jsonArray.getJSONArray(i)));
            }
        }
        return results;
    }
    
    
    /**
     * 获取key值为传入的的value集合
     * @param key :传入的key
     * @param parseJson 待解析的json对象
     * @return
     */
    public static List<String> getKeys(String key, JSONObject parseJson) {
        // TODO Auto-generated method stub
        List<String> results=new ArrayList<String>();
        if(!parseJson.isNullObject()){
            if(parseJson.containsKey(key)){
                if(parseJson.get(key) instanceof JSONArray){
                    JSONArray array=parseJson.getJSONArray(key);
                    for(int i=0;i<array.size();i++){
                        System.out.println(array.getString(i));
                        results.add(array.getString(i));
                    }
                }else{
                    System.out.println(parseJson.getString(key));
                    results.add(parseJson.getString(key));
                }
            }
        }
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
                                System.out.println(array.getString(i));
                                results.add(array.getString(i));
                            }
                        }else{
                            System.out.println(json.getString(key));
                            results.add(json.getString(key));
                        }
                    }else{
                        results.addAll(getKeys(key, json));
                    }
                }
            }
            if(parseJson.get(nowKey) instanceof JSONArray){
                results.addAll(getKeysByJSONArray(key,parseJson.getJSONArray(nowKey)));
            }
        }
        return results;
    }
 
    /**
     * 上一方法的配合使用方法
     * @param key
     * @param jsonArray
     * @return
     */
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
    
    public static List<Map<String, Object>> mergeListMap(List<Map<String, Object>> result,String key ,String keyValue) {
        // TODO Auto-generated method stub
        List<Map<String, Object>> info=new ArrayList<Map<String,Object>>();
        String firstValue="";
        Map<String,Object> nInfo=new HashMap<String, Object>();
        System.out.println(nInfo.isEmpty());
        for(Map<String, Object> in:result){
            
            //当前值
            String tValue=(String) in.get(key);
            //上一个值 firstValue

            //累计值
            String value=(String) nInfo.get(keyValue);
            if(firstValue.equals(tValue)){
                //加入新的值
                value=value+" ; "+(String) in.get(keyValue);
                nInfo.put(keyValue, value);
            }else{
                if(!firstValue.equals("")){
                    firstValue=tValue;
                    //创建新的map
                    nInfo.put(keyValue, value);
                    info.add(nInfo);
                    nInfo=in;
                }else{
                    firstValue=tValue;
                    for (Map.Entry<String, Object> entry : in.entrySet()) { 
                            nInfo.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return info;
    }
}
