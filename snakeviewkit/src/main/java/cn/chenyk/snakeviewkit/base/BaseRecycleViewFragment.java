package cn.chenyk.snakeviewkit.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener;

import java.util.List;

import cn.chenyk.snakeviewkit.R;
import cn.chenyk.snakeviewkit.base.BaseRecyclerViewAdapter.OnItemClickListener;
import cn.chenyk.snakeviewkit.view.RecyclerViewItemDecoration;

/**
 * Created by chenyk on 2016/12/29.
 */

public abstract class BaseRecycleViewFragment<T> extends BaseFragment implements LoadingListener, OnItemClickListener, BaseRecyclerViewAdapter.OnItemLongClickListener {

    private BaseRecyclerViewAdapter<T> mAdapter;

    protected XRecyclerView mRecyclerView;
    private TextView tvEmptyTips;
    private RelativeLayout EmptyRL;
    private int mPageNum;//当前页码
    private int mPageSize;//每页获取条数

    private Activity mActivty;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_comm_recycleview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivty = getActivity();
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerview);
        tvEmptyTips = (TextView) view.findViewById(R.id.comm_empty_tips);
        EmptyRL = (RelativeLayout) view.findViewById(R.id.comm_empty_rl);

        mPageNum = defaultPageNum();
        mPageSize = defaultPageSize();
        tvEmptyTips.setText(setEmptyTipsStr());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivty));
        mAdapter = createRecycleViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setRefreshProgressStyle(getLoadingStyle());
        mRecyclerView.setLoadingMoreProgressStyle(getLoadingStyle());
        if (isNeedItemDecoration())
            mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(mActivty,
                    RecyclerViewItemDecoration.VERTICAL_LIST));//分割线
        onCreatedHeadView(mRecyclerView);
    }

    /**
     * 是否需要分割线，默认需要
     *
     * @return
     */
    protected boolean isNeedItemDecoration() {
        return true;
    }

    /**
     * 添加头部,子类重写
     *
     * @param view
     */
    protected void onCreatedHeadView(XRecyclerView view) {
        //默认空
    }

    /**
     * 加载样式,更改样式子类重写
     *
     * @return
     */
    protected int getLoadingStyle() {
        return ProgressStyle.Pacman;
    }

    /**
     * 刷新数据
     */
    protected void onRefreshDates() {
        mRecyclerView.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        mPageNum = defaultPageNum();
        getListDatas(mPageNum, mPageSize);
    }

    @Override
    public void onLoadMore() {
        mPageNum++;
        getListDatas(mPageNum, mPageSize);
    }

    /**
     * 无数据提示
     *
     * @return
     */
    protected String setEmptyTipsStr() {
        return "暂无数据";
    }

    /**
     * 更新数据和UI,数据获取之后调用该方法
     *
     * @param isSuccess
     * @param datas
     * @param totalCount 加载数据条数
     */
    public void updateDataAndUI(boolean isSuccess, List<T> datas, int totalCount) {
        if (mAdapter.getItemCount() >= totalCount)
            mRecyclerView.setLoadingMoreEnabled(false);
        else
            mRecyclerView.setLoadingMoreEnabled(true);
        if (!isSuccess) {
            mPageNum--;//不成功则页数减一
            return;
        }
        if (mPageNum > 1) {
            mAdapter.addListDatas2Footer(datas);
            mRecyclerView.loadMoreComplete();
        } else {
            mAdapter.setListDatas(datas);
            mRecyclerView.refreshComplete();
        }
        resetEmptyView();
    }

    /**
     * 获取数据长度，默认20
     *
     * @return
     */
    protected int defaultPageSize() {
        return 20;
    }

    /**
     * 页码，默认1
     *
     * @return
     */
    protected int defaultPageNum() {
        return 1;
    }

    /**
     * 获取数据
     *
     * @param pageNum
     * @param pageSize
     */
    protected abstract void getListDatas(int pageNum, int pageSize);

    /**
     * 获取适配器
     *
     * @return
     */
    protected abstract BaseRecyclerViewAdapter<T> createRecycleViewAdapter();


    /**
     * 空数据页面提示
     */
    private void resetEmptyView() {
        if (mAdapter.getItemCount() <= 0) {
            EmptyRL.setVisibility(View.VISIBLE);
        } else if (mAdapter.getItemCount() > 0) {
            EmptyRL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(BaseRecyclerViewAdapter<?> adapter, View view, int position) {
        //子类重写即可
    }

    @Override
    public void onItemLongClick(BaseRecyclerViewAdapter<?> adapter, View view, int position) {
        //子类重写即可
    }


}
