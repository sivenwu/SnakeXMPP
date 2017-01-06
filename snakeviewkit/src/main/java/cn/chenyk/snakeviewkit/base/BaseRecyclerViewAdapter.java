package cn.chenyk.snakeviewkit.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.chenyk.snakeviewkit.R;
import cn.chenyk.snakeviewkit.view.MyViewHolder;

/**
 * Created by chenyk on 2016/12/29.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {
    private List<T> mListDatas;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public BaseRecyclerViewAdapter(Context context) {
        mContext = context;
        if (mListDatas == null) {
            mListDatas = new ArrayList<>(30);
        }
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<T> getAllListDatas() {
        return mListDatas;
    }

    /**
     * 移除指定位置数据
     *
     * @param position
     */
    public T removeData(int position) {
        if (position < mListDatas.size()) {
            T t = mListDatas.remove(position);
            notifyDataSetChanged();
            return t;
        }
        return null;
    }

    /**
     * 添加数据，从尾部添加
     *
     * @param listDatas
     */
    public void addListDatas2Footer(List<T> listDatas) {
        if (listDatas != null) {
            mListDatas.addAll(listDatas);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加单个数据，从尾部添加
     *
     * @param listData
     */
    public void addListData2Footer(T listData) {
        if (listData != null) {
            mListDatas.add(listData);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据，从头部添加
     *
     * @param listDatas
     */
    public void addListDatas2Header(List<T> listDatas) {
        if (listDatas != null) {
            mListDatas.addAll(0, listDatas);
            notifyDataSetChanged();
        }
    }

    /**
     * 重置整个列表的数据
     *
     * @param listDatas
     */
    public void setListDatas(List<T> listDatas) {
        mListDatas.clear();
        if (listDatas != null) {
            mListDatas.addAll(listDatas);
        }
        notifyDataSetChanged();
    }

    @Override
    public final int getItemViewType(int position) {
        return handlerItemViewType(mListDatas.get(position), position);
    }

    /**
     * 处理item类型
     *
     * @param data
     * @param position
     * @return
     */
    protected int handlerItemViewType(T data, int position) {
        return super.getItemViewType(position);
    }

    /**
     * 设置item点击监听器
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置item长按监听器
     *
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                getSMRVItemViewLayoutID(viewType), parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //点击
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    if (isNeedClickChangeBg())
                        holder.itemView.setBackgroundResource(getItemClickBg());
                    onItemClickListener.onItemClick(BaseRecyclerViewAdapter.this, holder.itemView, position);
                }
            }
        });
        //长按
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null)
                    onItemLongClickListener.onItemLongClick(BaseRecyclerViewAdapter.this, holder.itemView, position);
                return true;
            }
        });
        bindView(position, mListDatas.get(position), holder);
    }

    /**
     * 是否需要点击效果
     *
     * @return
     */
    protected boolean isNeedClickChangeBg() {
        return true;
    }

    @Override
    public int getItemCount() {
        return mListDatas == null ? 0 : mListDatas.size();
    }

    /**
     * 设置item背景颜色 默认 白色，点击浅灰色
     * 子类可重写设置颜色
     *
     * @return
     */
    protected int getItemClickBg() {
        return R.drawable.item_recyclerview_bg_selector;
    }

    /**
     * 获取RecyclerView中的item布局
     *
     * @return
     */
    public abstract int getSMRVItemViewLayoutID(int viewType);

    /**
     * 将业务数据绑定到具体的 tag上
     *
     * @param position
     * @param itemDdata
     * @param viewHolder
     */
    public abstract void bindView(int position, T itemDdata, MyViewHolder viewHolder);

    /**
     * item点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(BaseRecyclerViewAdapter<?> adapter, View view, int position);
    }

    /**
     * item长按事件接口
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(BaseRecyclerViewAdapter<?> adapter, View view, int position);
    }
}
