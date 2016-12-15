package com.snake.kit.apptools;

import android.util.Log;

/**
 * Created by Yuan on 2016/11/4.
 * Detail LogTool..
 */

public class LogTool {

    // 获取当前类名
    private static String getClassName() {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        String className = thisMethodStack.getClassName();
        int lastIndex = className.lastIndexOf(".");
        return String.format("[ %s ] ", className.substring(lastIndex + 1, className.length()));
    }

    public static void i(String message) {
        i(getClassName(), message);
    }

    public static void i(String tag, String message) {
        if (SnakeUtilKit.isDebug()) {
            Log.i(tag, message);
        }
    }

    public static void d(String message) {
        d(getClassName(), message);
    }

    public static void d(String tag, String message) {
        if (SnakeUtilKit.isDebug()) {
            Log.d(tag, message);
        }
    }

    public static void e(String message) {
        e(getClassName(), message);
    }

    public static void e(String tag, String message) {
        if (SnakeUtilKit.isDebug()) {
            Log.e(tag, message);
        }
    }

    public static void v(String message) {
        v(getClassName(), message);
    }

    public static void v(String tag, String message) {
        if (SnakeUtilKit.isDebug()) {
            Log.v(tag, message);
        }
    }

}
