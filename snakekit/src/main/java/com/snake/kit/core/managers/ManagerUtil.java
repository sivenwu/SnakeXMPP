package com.snake.kit.core.managers;

import java.util.Observable;

/**
 * Created by Yuan on 2016/12/23.
 * Detail manager 管理器工具，用于更新connection
 */

public class ManagerUtil extends Observable {

    public ManagerUtil() {

    }

    public void register(BaseManager manager){
        addObserver(manager);
    }

    public void notifyChangeData(Object obj) {
        setChanged();
        notifyObservers(obj);
    }

}
