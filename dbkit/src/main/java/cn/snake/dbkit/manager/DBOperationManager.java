package cn.snake.dbkit.manager;

import com.snake.api.exceptions.SnakeRuntimeException;

import java.util.List;

import cn.snake.dbkit.DBHelper;
import cn.snake.dbkit.bean.ChatInfoModel;
import cn.snake.dbkit.bean.ContactModel;
import cn.snake.dbkit.dao.ChatInfoModelDao;
import cn.snake.dbkit.dao.ContactModelDao;

/**
 * Created by chenyk on 2016/12/15.
 * 数据库操作管理类
 */

public class DBOperationManager {
    private static DBOperationManager mManager;

    public static DBOperationManager get() {
        if (mManager == null) mManager = new DBOperationManager();
        return mManager;
    }

    /**
     * insert one data to db
     *
     * @param object
     */
    public void insert(Object object) {
        if (object instanceof ChatInfoModel) {
            ChatInfoModelDao dao = DBHelper.getDaoSession().getChatInfoModelDao();
            dao.insert((ChatInfoModel) object);
        } else if (object instanceof ContactModel) {
            ContactModelDao dao = DBHelper.getDaoSession().getContactModelDao();
            dao.insert((ContactModel) object);
        }
    }

    /**
     * delete one data from db
     *
     * @param object
     */
    public void delete(Object object) {
        if (object instanceof ChatInfoModel) {
            ChatInfoModelDao dao = DBHelper.getDaoSession().getChatInfoModelDao();
            dao.delete((ChatInfoModel) object);
        } else if (object instanceof ContactModel) {
            ContactModelDao dao = DBHelper.getDaoSession().getContactModelDao();
            dao.delete((ContactModel) object);
        }
    }

    /**
     * update one data to db
     *
     * @param object
     */
    public void update(Object object) {
        if (object instanceof ChatInfoModel) {
            ChatInfoModelDao dao = DBHelper.getDaoSession().getChatInfoModelDao();
            dao.update((ChatInfoModel) object);
        } else if (object instanceof ContactModel) {
            ContactModelDao dao = DBHelper.getDaoSession().getContactModelDao();
            dao.update((ContactModel) object);
        }
    }

    /**
     * query datas from db
     * @param obj
     * @param <T>
     * @return
     */
    public <T extends Object> List<T> queryList(Object obj) {
        if (obj instanceof ChatInfoModel)
            return (List<T>) DBHelper.getDaoSession().getChatInfoModelDao().queryBuilder().list();
        else if (obj instanceof ContactModel)
            return (List<T>) DBHelper.getDaoSession().getContactModelDao().queryBuilder().list();
        else throw new SnakeRuntimeException("The current object does not exist");
    }
}
