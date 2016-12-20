package com.snake.kit.core.data.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuan on 2016/12/19.
 * Detail 临时存储应用对象映射路径
 */

public class TargetSupport {

    private final String TAG = "TargetSupport";

    // 临时存储
    private Map<String,Object> data;

    public TargetSupport() {
        data = new HashMap<>();
    }

    public String getObjectPath(String key){
        if (data.containsKey(key)){
            return (String) data.get(key);
        }
        return "";
    }

    public void putObjectsPath(Map<String,Object> tmp){
        data.clear();
        data.putAll(tmp);
    }

    public void addObjectsPath(Map<String,Object> tmp){
        data.putAll(tmp);
    }

    public void clealAll(){
        data.clear();
    }

    public Object getObject(String key){

        Object obj = null;

        if (data.containsKey(key)){

            String path = (String) data.get(key);
            try {
                Class claTmp = Class.forName(path);
                obj = claTmp.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return obj;
    }
}
