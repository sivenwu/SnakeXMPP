package cn.chenyk.snakeviewkit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.chenyk.snakeviewkit.activity.SnakeChatActivity;
import cn.chenyk.snakeviewkit.adapter.SnakeSessionAdapter;
import cn.chenyk.snakeviewkit.base.BaseRecycleViewFragment;
import cn.chenyk.snakeviewkit.base.BaseRecyclerViewAdapter;
import cn.chenyk.snakeviewkit.bean.DialogListModel;
import cn.chenyk.snakeviewkit.view.dialog.CommListDialog;
import cn.snake.dbkit.bean.ContactModel;
import cn.snake.dbkit.manager.DBOperationManager;

/**
 * Created by chenyk on 2016/12/29.
 */

public class SnakeSessionListFragment extends BaseRecycleViewFragment<ContactModel> {
    private List<ContactModel> mlists;
    private List<DialogListModel> dialogListModelList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setPullRefreshEnabled(false);//禁止下拉刷新
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
//        getSessionDates();
        updateDataAndUI(true, mlists, 0);
    }

    @Override
    protected void getListDatas(int pageNum, int pageSize) {
    }

    @Override
    protected BaseRecyclerViewAdapter<ContactModel> createRecycleViewAdapter() {
        return new SnakeSessionAdapter(mActivity);
    }

    @Override
    public void onItemClick(BaseRecyclerViewAdapter<?> adapter, View view, int position) {
        super.onItemClick(adapter, view, position);
        startActivity(new Intent(getActivity(), SnakeChatActivity.class));
    }

    @Override
    public void onItemLongClick(BaseRecyclerViewAdapter<?> adapter, View view, final int position) {
        super.onItemLongClick(adapter, view, position);
//        new ListDialog().showDialog((SnakeSessionActivity) mActivity);
        final ContactModel model = mlists.get(position);
        if (dialogListModelList == null)
            dialogListModelList = new ArrayList<>();
        dialogListModelList.clear();
        if (model.getViewTop() == 1) dialogListModelList.add(new DialogListModel("取消置顶", 100));
        else dialogListModelList.add(new DialogListModel("置顶聊天", 101));
        dialogListModelList.add(new DialogListModel("删除该聊天", 200));
        new CommListDialog.Builder(mActivity).setLists(dialogListModelList)
                .setOnClickListener(new CommListDialog.IClick() {
                    @Override
                    public void onClick(View view, int tag) {
                        switch (tag) {
                            case 100:
                                model.setViewTop(0);
                                mlists.set(position, model);
                                break;
                            case 101:
                                model.setViewTop(1);
                                mlists.set(position, model);
                                break;
                            case 200:
                                mlists.remove(position);
                                break;
                            default:
                                break;
                        }
                        Collections.sort(mlists, new ViewTopComparator());
                        updateDataAndUI(true, mlists, 0);
                    }
                }).create().show();
    }

    /**
     * get session dates
     */
    public void getSessionDates() {
        if (mlists == null)
            mlists = new ArrayList<>();
        mlists.clear();
        mlists = DBOperationManager.get().getSessionList();
        Collections.sort(mlists, new ViewTopComparator());
    }

    /**
     * 测试数据
     */
    private void initDatas() {
        mlists = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ContactModel model = new ContactModel();
            if (i > 15) model.setViewTop(1);
            model.setUserName("昵称" + i);
            mlists.add(model);
        }
        Collections.sort(mlists, new ViewTopComparator());
    }

    class ViewTopComparator implements Comparator<ContactModel> {

        @Override
        public int compare(ContactModel o1, ContactModel o2) {
            if (o1.getViewTop() == 0 && o2.getViewTop() != 0) return 1;
            else if (o1.getViewTop() != 0 && o2.getViewTop() == 0) return -1;
            else return 0;
        }
    }
}
