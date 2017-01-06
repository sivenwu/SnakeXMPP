package cn.chenyk.snakeviewkit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.chenyk.snakeviewkit.adapter.DialogListAdapter;
import cn.chenyk.snakeviewkit.base.BaseRecycleViewFragment;
import cn.chenyk.snakeviewkit.base.BaseRecyclerViewAdapter;
import cn.chenyk.snakeviewkit.bean.DialogListModel;

/**
 * Created by chenyk on 2016/12/30.
 */

public class DialogListFragment extends BaseRecycleViewFragment<DialogListModel> {
    private List<DialogListModel> mlists;

    private String[] strings = {"标为未读", "标为已读", "置顶聊天", "取消置顶", "删除该聊天"};

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListDates();
        updateDataAndUI(true, mlists, 0);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setPullRefreshEnabled(false);//禁止下拉刷新
    }

    @Override
    protected void getListDatas(int pageNum, int pageSize) {

    }

    @Override
    protected BaseRecyclerViewAdapter<DialogListModel> createRecycleViewAdapter() {
        return new DialogListAdapter(mActivity);
    }

    /**
     * 初始化列表数据
     */
    private void initListDates() {
        mlists = new ArrayList<>();
        for (int position = 0; position < strings.length; position++) {
            DialogListModel model = new DialogListModel();
            model.action = strings[position];
            model.tag = position;
            mlists.add(model);
        }
    }

    @Override
    public void onItemClick(BaseRecyclerViewAdapter<?> adapter, View view, int position) {
        super.onItemClick(adapter, view, position);
        if (mlists.get(position).tag == 0) {

        } else if (mlists.get(position).tag == 1) {
        } else if (mlists.get(position).tag == 2) {
        } else if (mlists.get(position).tag == 3) {
        } else if (mlists.get(position).tag == 4) {
        }
    }
}
