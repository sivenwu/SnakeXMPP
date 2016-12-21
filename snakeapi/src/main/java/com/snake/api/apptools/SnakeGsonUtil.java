package com.snake.api.apptools;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.snake.api.exceptions.SnakeRuntimeException;

import java.lang.reflect.Type;

/**
 * Created by chenyk on 2016/12/21.
 */

public class SnakeGsonUtil {

    private static Gson gson = new Gson();

    public static String bean2json(Object bean) {
        return gson.toJson(bean);
    }

    public static <T> T json2bean(String json, Type type) {
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw new SnakeRuntimeException(e.getMessage());
        }
    }
}
