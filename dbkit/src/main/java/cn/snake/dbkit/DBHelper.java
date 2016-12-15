package cn.snake.dbkit;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import cn.snake.dbkit.data.DaoMaster;
import cn.snake.dbkit.data.DaoSession;

/**
 * Created by chenyk on 2016/12/15.
 * db create
 */

public class DBHelper {
    private static final String DB_NAME = "snake";//default db name

    private static DBHelper DB;
    private DaoSession mDaoSession;

    public static DaoSession getDaoSession() {
        return DB.mDaoSession;
    }

    private DBHelper(Application application, String dbName, boolean isEncryption) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application,
                isEncryption ? dbName + "-db-encrypted" : dbName + "-db");
        Database db = isEncryption ? helper.getEncryptedWritableDb(
                "super-secret") : helper.getWritableDb();
        if (DB_NAME.equals(dbName))
            mDaoSession = new DaoMaster(db).newSession();
    }

    /**
     * init db
     *
     * @param application
     */
    public static void init(Application application, boolean isEncryption) {
        if (DB == null) DB = new DBHelper(application, DB_NAME, isEncryption);
    }

}

