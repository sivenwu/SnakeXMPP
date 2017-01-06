package cn.chenyk.snakeviewkit.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.chenyk.snakeviewkit.R;
import cn.chenyk.snakeviewkit.base.BaseRecyclerViewAdapter;
import cn.chenyk.snakeviewkit.view.MyViewHolder;
import cn.snake.dbkit.bean.ContactModel;

/**
 * Created by chenyk on 2016/12/29.
 */

public class SnakeSessionAdapter extends BaseRecyclerViewAdapter<ContactModel> {
    private Context mContext;
    public SnakeSessionAdapter(Activity context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getSMRVItemViewLayoutID(int viewType) {
        return R.layout.item_snake_session;
    }

    @Override
    public void bindView(int position, ContactModel itemDdata, MyViewHolder viewHolder) {
        TextView sessionUserName = viewHolder.getView(R.id.session_userName);
        LinearLayout sessionLL = viewHolder.getView(R.id.session_ll);
        sessionUserName.setText(itemDdata.getUserName());

        if (itemDdata.getViewTop() != 0)
            sessionLL.setBackgroundResource(R.drawable.item_viewtop_bg_selector);
        else
            sessionLL.setBackgroundResource(R.drawable.item_recyclerview_bg_selector);
    }

    @Override
    protected boolean isNeedClickChangeBg() {
        return false;
    }
}
