package com.fuwenjun.projectUtils.ElasticSearch;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticSearchSource {

    private JSONObject jsonObject;

    public ElasticSearchSource() {
    }

    public  Map<Object, Object> getSource(JSONObject jsonObject, int index) {

        JSONArray jsonArray;
        Map<Object, Object> result = new HashMap<>();
        if (jsonObject.containsKey(String.valueOf(index))) {
            jsonObject = jsonObject.getJSONObject(String.valueOf(index));
        }
        if (jsonObject.containsKey("buckets")) {
            jsonArray = jsonObject.getJSONArray("buckets");
//            System.out.println("获取jsonObject中含有buckets的jsonArray : " + jsonArray);
            for (int i = 0; i < jsonArray.size(); i++) {
                result.put(jsonArray.getJSONObject(i).get("key"), getSource(jsonArray.getJSONObject(i), index+1));
            }
        } else {
            JSONArray hits = jsonObject.getJSONObject("1").getJSONObject("hits").getJSONArray("hits");
            int length = hits.size();
            List<Object> resultList = new ArrayList<>();
            for (int i = 0; i < length; i++) {
//                System.out.println("value : " + hits.getJSONObject(i).getJSONObject("_source").isNullObject());
                if (!hits.getJSONObject(i).getJSONObject("_source").isNullObject()) {
                    resultList.add(hits.getJSONObject(i).getJSONObject("_source").get("url")); //测试用.get("url")，正式服删除
                } else {
                    resultList.add(null);
                }
            }
            result.put("_source", resultList);
        }
        return result;

    }

}
