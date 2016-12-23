package cn.snake.dbkit;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import cn.snake.dbkit.dao.DaoMaster;

/**
 * Created by chenyk on 2016/12/22.
 */

public class SnakeOpenHelper extends DaoMaster.OpenHelper {

    public SnakeOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
