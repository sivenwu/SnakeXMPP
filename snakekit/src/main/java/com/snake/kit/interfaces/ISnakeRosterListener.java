package com.snake.kit.interfaces;

import java.util.List;

import cn.snake.dbkit.bean.ContactModel;

/**
 * Created by Yuan on 2016/12/30.
 * Detail 获取好友回调
 */

public interface ISnakeRosterListener {

    public void rosterEntires(List<ContactModel> contactModels);

}
