package cn.snake.dbkit.manager;

import cn.snake.dbkit.DBHelper;
import cn.snake.dbkit.bean.ChatInfoModel;
import cn.snake.dbkit.data.ChatInfoModelDao;

/**
 * Created by chenyk on 2016/12/15.
 * 数据库操作管理类
 */

public class DBOperationManager {
    ChatInfoModel mChatInfoModel;

    public void getDBObject() {
        mChatInfoModel = DBHelper.getDaoSession().getChatInfoModelDao().queryBuilder().unique();
    }

    public ChatInfoModel getChatInfoModel() {
        if (mChatInfoModel == null) getDBObject();
        return mChatInfoModel;
    }

    /**
     * insert chat message to db
     *
     * @param chatInfoModel
     */
    public void insert(ChatInfoModel chatInfoModel) {
        ChatInfoModelDao dao = DBHelper.getDaoSession().getChatInfoModelDao();
        dao.insert(chatInfoModel);
    }
}
