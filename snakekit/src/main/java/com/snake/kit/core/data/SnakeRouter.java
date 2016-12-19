package com.snake.kit.core.data;

import android.content.Context;

import com.snake.db.SnakeDbKit;
import com.snake.db.Test;
import com.snake.kit.core.data.bean.ACTION;
import com.snake.kit.core.data.bean.TargetSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuan on 2016/12/11.
 * Detail snake数据库路由表
 */

public class SnakeRouter {

    private final String TAG = "SnakeDbRouter";

//    private Context mContext;
    private static SnakeRouter mSnakeRouter;

    private Map<ACTION,String> actionKits;
    private Map<ACTION,TargetSupport> actionSupports;

    private Map<String,Object> objCache;

    public SnakeRouter() {
        this.actionSupports = new HashMap<>();
        this.actionKits = new HashMap<>();
        this.objCache = new HashMap<>();
        init();
        displayKit();
    }

    public static SnakeRouter instance(){

        if (mSnakeRouter == null){

            synchronized (SnakeRouter.class){
                if (mSnakeRouter == null){
                    mSnakeRouter = new SnakeRouter();
                    return mSnakeRouter;
                }
            }
        }
        return mSnakeRouter;
    }

    //---- public ---------------------------------------------------------------------------------

    /**
     * 获取对象入口
     * @param a 类型
     * @param name 对象名字
     */
    public Object getObject(ACTION a,String name){

        Object obj = null;

        if (objCache.containsKey(name)){
            obj = objCache.get(name);
            return obj;
        }

        if (actionSupports.containsKey(a)){

            TargetSupport support = actionSupports.get(a);
            obj = support.getObject(name);
            objCache.put(name,obj);
            return obj;
        }

        return obj;

    }

    // 测试方法
    public void test(Context context){
        Test test  = (Test) getObject(ACTION.DB,"Test");
        test.showToast(context);
    }


    //---- private --------------------------------------------------------------------------------

    private void init(){

        // 配置jar 入口
        actionKits.put(ACTION.DB,"com.snake.db.SnakeDbKit");// 数据库引用
    }

    private void displayKit(){
        for (Map.Entry<ACTION,String> entry : actionKits.entrySet()) {

            Object router = getRouter(entry.getKey());
            if (router != null){
                getRouterTable(router);
            }
        }
    }

    // 获取路由引用表
    private void getRouterTable(Object router){
        //.. 待补充

        TargetSupport support = null;

        if (router instanceof SnakeDbKit){
            SnakeDbKit mSnakeDbKit = (SnakeDbKit) router;

            support = new TargetSupport();
            support.putObjectsPath(mSnakeDbKit.getRouterTable());

            actionSupports.put(ACTION.DB,support);
        }

    }

    // 获取路由
    private Object getRouter(ACTION action){

        Object router = null;

        if (actionKits.containsKey(action)){
            if (router == null) {
                try {
                    Class claTmp = Class.forName(actionKits.get(action));
                    router = claTmp.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return router;
        }
        return router;
    }
}
