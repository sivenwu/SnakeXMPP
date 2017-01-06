package cn.chenyk.snakeviewkit.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chenyk on 2016/12/29.
 */
public class MyViewHolder extends RecyclerView.ViewHolder {

    public MyViewHolder(View view) {
        super(view);
    }

    /**
     * 获取itemView中控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        return (T) itemView.findViewById(viewId);
    }

}