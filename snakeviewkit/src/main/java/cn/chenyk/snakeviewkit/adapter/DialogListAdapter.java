package cn.chenyk.snakeviewkit.adapter;

import android.content.Context;
import android.widget.TextView;

import cn.chenyk.snakeviewkit.R;
import cn.chenyk.snakeviewkit.base.BaseRecyclerViewAdapter;
import cn.chenyk.snakeviewkit.bean.DialogListModel;
import cn.chenyk.snakeviewkit.view.MyViewHolder;

/**
 * Created by chenyk on 2016/12/30.
 */

public class DialogListAdapter extends BaseRecyclerViewAdapter<DialogListModel> {
    public DialogListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getSMRVItemViewLayoutID(int viewType) {
        return R.layout.item_dialog_list;
    }

    @Override
    public void bindView(int position, DialogListModel itemDdata, MyViewHolder viewHolder) {
        TextView ActionTv = viewHolder.getView(R.id.action_tv);
        ActionTv.setText(itemDdata.action);
    }
}
