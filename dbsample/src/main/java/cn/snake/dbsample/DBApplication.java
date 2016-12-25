package cn.snake.dbsample;

import android.app.Application;

import com.snake.api.apptools.SnakePref;

import cn.snake.dbkit.DBHelper;

/**
 * Created by chenyk on 2016/12/15.
 */

public class DBApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.init(this, false);//db init
        SnakePref.init(this);
    }
}
