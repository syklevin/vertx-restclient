package com.glt.rest.client.impl;

import io.vertx.core.json.JsonObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by levin on 2/16/2015.
 */
public class HttpHelpers {

    public static Map<String, String> fromQuery(String qs) {
        try {
            if (qs == null)
                throw new IllegalArgumentException();
            StringBuffer buf = new StringBuffer();
            Map<String, String> params = new HashMap<>();
            String[] parts = qs.split("&");
            String[] arr = null;
            for (String part : parts) {
                arr = part.split("=");
                if (arr.length >= 2 && !"".equals(arr[0])) {
                    params.put(URLDecoder.decode(arr[0], "UTF-8"), URLDecoder.decode(arr[1], "UTF-8"));
                }
            }
            return params;
        }
        catch(Exception ex){
            return new HashMap<>();
        }
    }

    public static JsonObject fromQueryAsJson(String qs) {
        try {
            if (qs == null)
                throw new IllegalArgumentException();
            StringBuffer buf = new StringBuffer();
            Map<String, Object> params = new HashMap<>();
            String[] parts = qs.split("&");
            String[] arr = null;
            for (String part : parts) {
                arr = part.split("=");
                if (arr.length >= 2 && !"".equals(arr[0])) {
                    params.put(URLDecoder.decode(arr[0], "UTF-8"), URLDecoder.decode(arr[1], "UTF-8"));
                }
            }
            return new JsonObject(params);
        }
        catch(Exception ex){
            return new JsonObject();
        }
    }

    public static String toQuery(JsonObject params) {
        return toQuery(params.getMap());
    }

    public static String toQuery(Map<String, Object> params) {
        try {
            if (params == null)
                throw new IllegalArgumentException();
            StringBuilder buf = new StringBuilder();
            String appender = "";
            for (Map.Entry entry : params.entrySet()) {
                buf.append(appender)
                        .append(URLEncoder.encode(entry.getKey().toString(), "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                appender = "&";
            }
            return buf.toString();
        }
        catch(Exception ex){
            return "";
        }
    }

    public static String appendQuery(String left, String right){
        if(left == null || left.length() == 0){
            return "?" + right;
        }
        else if(left.lastIndexOf("?") >= 0){
            return left + "?" + right;
        }
        else{
            return left + "&" + right;
        }
    }


}
