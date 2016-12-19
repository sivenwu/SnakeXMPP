package com.snake.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuan on 2016/12/19.
 * Detail 测试入口 可删除
 */

public class SnakeDbKit {

    public Map<String ,Object> getRouterTable(){
        Map<String ,Object> map = new HashMap<>();

        map.put("Test","com.snake.db.Test");

        return map;
    }

}
