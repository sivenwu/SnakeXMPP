package cn.chenyk.snakeviewkit.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import java.util.List;

import cn.chenyk.snakeviewkit.R;
import cn.chenyk.snakeviewkit.adapter.DialogListAdapter;
import cn.chenyk.snakeviewkit.base.BaseRecyclerViewAdapter;
import cn.chenyk.snakeviewkit.bean.DialogListModel;
import cn.chenyk.snakeviewkit.view.RecyclerViewItemDecoration;

/**
 * Created by chenyk on 2017/1/4.
 */

public class CommListDialog extends Dialog {
    private RecyclerView mRecyclerView;
    private Context context;
    private DialogListAdapter adapter;
    private List<DialogListModel> mlists;
    private IClick iClick;

    public CommListDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CommListDialog(Context context, List<DialogListModel> lists, IClick iClick) {
        super(context);
        this.context = context;
        this.mlists = lists;
        this.iClick = iClick;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        if (window != null) {
            // TODO: 2017/1/5 need handle the dialog height
//            window.getAttributes().height = 400;
//            window.getAttributes().width = ViewUtil.getWidth(context) - (int) ViewUtil.dp2Px(40);
//            window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_comm_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(context,
                RecyclerViewItemDecoration.VERTICAL_LIST));//分割线
        adapter = new DialogListAdapter(context);
        mRecyclerView.setAdapter(adapter);
        if (mlists != null) adapter.setListDatas(mlists);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter<?> adapter, View view, int position) {
                dismiss();
                if (iClick != null) iClick.onClick(view, mlists.get(position).tag);
            }
        });
    }

    /**
     * builder配置类
     */
    public static class Builder {
        private Context context;
        private List<DialogListModel> lists;
        private IClick iClick;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setLists(List<DialogListModel> lists) {
            this.lists = lists;
            return this;
        }

        public Builder setOnClickListener(IClick iClick) {
            this.iClick = iClick;
            return this;
        }

        public CommListDialog create() {
            return new CommListDialog(context, lists, iClick);
        }
    }

    public interface IClick {
        void onClick(View view, int tag);
    }


}
