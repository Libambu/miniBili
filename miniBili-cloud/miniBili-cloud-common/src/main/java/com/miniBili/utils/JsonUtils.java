package com.miniBili.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonUtils {
    public static String converObj2Json(Object obj){
        return JSON.toJSONString(obj);
    }
    public static <T>T converJson2obj(String json,Class<T> claszz){
        return JSONObject.parseObject(json,claszz);
    }
    public static <T> List<T> converJsonArray2List(String json, Class<T> clazz){
        return JSONArray.parseArray(json,clazz);
    }
}
