package cn.snake.dbkit.helper;

import com.snake.api.apptools.SnakePref;
import com.snake.api.data.SnakeConstants;

/**
 * Created by chenyk on 2016/12/22.
 * 聊天账号相关辅助类
 */

public class ChatAccountHelper {
    /**
     * get chat userId
     *
     * @return
     */
    public static String getUserId() {
        return SnakePref.getString(SnakeConstants.MESSAGE_USER_ID, "chenyk");
    }

    /**
     * set chat userId
     *
     * @param userId
     */
    public static void setUserId(String userId) {
        SnakePref.putObject(SnakeConstants.MESSAGE_USER_ID, userId);
    }
}
