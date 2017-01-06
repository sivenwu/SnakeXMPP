package com.snake.kit.core.data;

import android.content.Context;
import android.util.ArrayMap;

import com.snake.api.apptools.LogTool;
import com.snake.db.Test;
import com.snake.kit.core.data.bean.ACTION;
import com.snake.kit.core.data.bean.TargetSupport;

import java.lang.ref.SoftReference;
import java.util.Map;

import cn.snake.dbkit.manager.DBOperationManager;

/**
 * Created by Yuan on 2016/12/11.
 * Detail snake router
 */

public class SnakeRouter {

    private final String TAG = "SnakeDbRouter";

    //    private Context mContext;
    private static SnakeRouter mSnakeRouter;

    private Map<ACTION,String> actionKits;
    private Map<ACTION,TargetSupport> actionSupports;

    private Map<String,SoftReference<Object>> objCache;

    private SnakeRouter() {
        this.actionSupports = new ArrayMap<>();
        this.actionKits = new ArrayMap<>();
        this.objCache = new ArrayMap<>();
        init();
        initComponentKit();
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
     * Create an entity by the router table of dblbrary
     * @param name
     * @return
     */
    public Object dbObject(String name){
        return getObject(ACTION.DB,name);
    }

    /**
     * Create an entity by the router table of dblbrary
     * @param name
     * @param useCache
     * @return
     */
    public Object dbObject(String name,boolean useCache){
        return getObject(ACTION.DB,name,useCache);
    }

    /**
     * Create a portal entity
     * @param a action for type
     * @param name the name of obj
     */
    public Object getObject(ACTION a,String name){
        return getObject(a,name,true);
    }

    /**
     * Create a portal entity
     * @param a
     * @param name
     * @param useCache
     * @return
     */
    public Object getObject(ACTION a,String name,boolean useCache){

        Object obj = null;

        if (objCache.containsKey(name)){
            obj = objCache.get(name).get();
            return obj;
        }

        if (actionSupports.containsKey(a)){

            TargetSupport support = actionSupports.get(a);
            obj = support.getObject(name);
            if (useCache)
                objCache.put(name,new SoftReference<Object>(obj));
            return obj;
        }

        return obj;

    }

    // 测试方法
    public void test(Context context){
        Test test  = (Test) getObject(ACTION.DB,"Test");
        test.showToast(context);
    }

    //- libray Whether the use of the jar
    public boolean isUseDbLibrary(){
        return actionSupports!=null && actionSupports.containsKey(ACTION.DB);
    }

    public boolean isUseViewLibrary(){
        return actionSupports!=null && actionSupports.containsKey(ACTION.VIEW);
    }

    //- libray get kit
    public DBOperationManager getDbLibarayKit(){
        Object tmp =  dbObject("DBOperationManager");
        if (tmp != null){
            return (DBOperationManager) tmp;
        }
        return null;
    }

    //---- private --------------------------------------------------------------------------------

    private void init(){

        // Configure the jar , default
        actionKits.put(ACTION.DB,"cn.snake.dbkit.manager.DBOperationManager");// 数据库引用
    }

    private void initComponentKit(){
        for (Map.Entry<ACTION,String> entry : actionKits.entrySet()) {

            Object router = getRouter(entry.getKey());
            if (router != null){
                getRouterTable(router);
            }
        }
    }

    // Getting table of router by action kit
    private void getRouterTable(Object router){
        // continuing...

        TargetSupport support = null;

        if (router instanceof DBOperationManager){ // db entry

            LogTool.d("RouterTable from DBOperationManager");

            DBOperationManager mSnakeDbKit = (DBOperationManager) router;

            support = new TargetSupport();
            support.putObjectsPath(mSnakeDbKit.getRouterTable());
            LogTool.d("RouterTable is" + mSnakeDbKit.getRouterTable().toString());

            actionSupports.put(ACTION.DB,support);
            objCache.put("DBOperationManager",new SoftReference<Object>(mSnakeDbKit));// cache this kit
        }

    }

    // Getting the router kit
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
