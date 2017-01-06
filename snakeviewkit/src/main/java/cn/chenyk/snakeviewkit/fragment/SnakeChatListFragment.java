package cn.chenyk.snakeviewkit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.snake.api.data.SnakeConstants;

import java.util.ArrayList;
import java.util.List;

import cn.chenyk.snakeviewkit.activity.ContactInfoActivity;
import cn.chenyk.snakeviewkit.adapter.SnakeChatAdapter;
import cn.chenyk.snakeviewkit.base.BaseRecycleViewFragment;
import cn.chenyk.snakeviewkit.base.BaseRecyclerViewAdapter;
import cn.snake.dbkit.bean.ChatInfoModel;

/**
 * Created by chenyk on 2016/12/30.
 */

public class SnakeChatListFragment extends BaseRecycleViewFragment<ChatInfoModel> {
    private List<ChatInfoModel> mlists;

    @Override
    protected boolean isNeedItemDecoration() {
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setPullRefreshEnabled(false);//禁止下拉刷新
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
        updateDataAndUI(true, mlists, 0);
        mRecyclerView.smoothScrollToPosition(mlists.size());
    }

    @Override
    protected void getListDatas(int pageNum, int pageSize) {

    }

    @Override
    protected BaseRecyclerViewAdapter<ChatInfoModel> createRecycleViewAdapter() {
        return new SnakeChatAdapter(mActivity, this);
    }

    public void startContactInfoActivity() {
        startActivity(new Intent(mActivity, ContactInfoActivity.class));
    }

    /**
     * 测试数据
     */
    private void initDatas() {
        mlists = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ChatInfoModel model = new ChatInfoModel();
            model.setMessage("今天天晴" + i);
            if (i % 3 == 0) model.setIsFrom(SnakeConstants.COMMON_STRING_TRUE);
            mlists.add(model);
        }
    }

    public void addTestDate(String message) {
        ChatInfoModel model = new ChatInfoModel();
        model.setMessage(message);
        model.setIsFrom(SnakeConstants.COMMON_STRING_FALSE);
        mlists.add(model);
        updateDataAndUI(true, mlists, 0);
        mRecyclerView.smoothScrollToPosition(mlists.size());
    }
}
