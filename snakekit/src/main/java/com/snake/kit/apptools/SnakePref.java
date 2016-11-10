package com.snake.kit.apptools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.snake.kit.exceptions.SnakeRuntimeException;

/**
 * Created by Yuan on 2016/11/4.
 * Detail SharePrefrence for Snake
 */
public class SnakePref {

    private static final String PREF_NAME = "SnakePref";
    private static final String EXCEPTION_MESSAGE = "SnakePref Exception,Please check the initialization of SnakePref";

    private final static int SP_MODE = Context.MODE_PRIVATE;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor preferencesEditor;


    public static void init(Application application){
        preferences = application.getSharedPreferences(PREF_NAME,SP_MODE);
        preferencesEditor = preferences.edit();
    }

    public static void init(Activity activity){
        preferences = activity.getApplication().getSharedPreferences(PREF_NAME,SP_MODE);
        preferencesEditor = preferences.edit();
    }

    //--- put value -------------------------------------------------------------------------------

    public static void putObject(String key,Object value){

        if (filterNull()){
            throw  new SnakeRuntimeException(EXCEPTION_MESSAGE);
        }
        putByType(key,value);
    }

    //--- get value -------------------------------------------------------------------------------

    public static String getString(String key,String defValue){
        if (filterNull()){
            throw  new SnakeRuntimeException(EXCEPTION_MESSAGE);
        }
        return preferences.getString(key,defValue);
    }

    public static int getInt(String key,int defValue){
        if (filterNull()){
            throw  new SnakeRuntimeException(EXCEPTION_MESSAGE);
        }
        return preferences.getInt(key,defValue);
    }

    public static boolean getBoolean(String key,boolean defValue){
        if (filterNull()){
            throw  new SnakeRuntimeException(EXCEPTION_MESSAGE);
        }
        return preferences.getBoolean(key,defValue);
    }

    public static float getFloat(String key,float defValue){
        if (filterNull()){
            throw  new SnakeRuntimeException(EXCEPTION_MESSAGE);
        }
        return preferences.getFloat(key,defValue);
    }

    public static long getLong(String key,long defValue){
        if (filterNull()){
            throw  new SnakeRuntimeException(EXCEPTION_MESSAGE);
        }
        return preferences.getLong(key,defValue);
    }

//
//    public static Object getObject(String key,Object defValue){
//
//        if (filterNull()){
//            throw  new SnakeRuntimeException(EXCEPTION_MESSAGE);
//        }
//        return getByType(key,defValue);
//    }

    public static void removeObject(String ...keys){

        if (filterNull()){
            throw  new SnakeRuntimeException(EXCEPTION_MESSAGE);
        }

        for (String key : keys){
            preferencesEditor.remove(key);
        }
        preferencesEditor.commit();
    }

    private static void putByType(String key,Object value){

        if (value instanceof Integer) {
            preferencesEditor.putInt(key, (Integer) value);
        }else if (value instanceof Boolean) {
            preferencesEditor.putBoolean(key, (Boolean) value);
        }else if (value instanceof Float) {
            preferencesEditor.putFloat(key, (Float) value);
        }else if (value instanceof Long) {
            preferencesEditor.putLong(key, (Long) value);
        }else if (value instanceof  String) {
            preferencesEditor.putString(key, (String) value);
        }
        preferencesEditor.commit();
    }

    private static Object getByType(String key,Object value){

        Object result = null;

        if (value instanceof Integer) {
            preferences.getInt(key, (Integer) value);
        }else if (value instanceof Boolean) {
            preferences.getBoolean(key, (Boolean) value);
        }else if (value instanceof Float) {
            preferences.getFloat(key, (Float) value);
        }else if (value instanceof Long) {
            preferences.getLong(key, (Long) value);
        }else if (value instanceof  String) {
            preferences.getString(key, (String) value);
        }

        return result;
    }

    private static boolean filterNull(){
        return (preferences==null || preferencesEditor== null) ? true : false;
    }


}
