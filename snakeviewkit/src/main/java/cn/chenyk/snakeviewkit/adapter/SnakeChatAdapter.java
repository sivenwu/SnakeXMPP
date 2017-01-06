package cn.chenyk.snakeviewkit.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snake.api.data.SnakeConstants;

import java.util.ArrayList;
import java.util.List;

import cn.chenyk.snakeviewkit.R;
import cn.chenyk.snakeviewkit.base.BaseRecyclerViewAdapter;
import cn.chenyk.snakeviewkit.bean.DialogListModel;
import cn.chenyk.snakeviewkit.fragment.SnakeChatListFragment;
import cn.chenyk.snakeviewkit.view.MyViewHolder;
import cn.chenyk.snakeviewkit.view.dialog.CommListDialog;
import cn.snake.dbkit.bean.ChatInfoModel;

/**
 * Created by chenyk on 2016/12/30.
 */

public class SnakeChatAdapter extends BaseRecyclerViewAdapter<ChatInfoModel> {
    private List<DialogListModel> dialogListModelList;
    private Context context;
    private SnakeChatListFragment fragment;

    public SnakeChatAdapter(Context context, SnakeChatListFragment fragment) {
        super(context);
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    protected int handlerItemViewType(ChatInfoModel data, int position) {
        if (SnakeConstants.COMMON_STRING_TRUE.equals(data.getIsFrom())) return 0;
        else return 1;
    }

    @Override
    public int getSMRVItemViewLayoutID(int viewType) {
        if (0 == viewType) return R.layout.item_snake_chat_left;
        else return R.layout.item_snake_chat_right;
    }

    @Override
    public void bindView(int position, final ChatInfoModel itemDdata, MyViewHolder viewHolder) {
        TextView contentTv = viewHolder.getView(R.id.chat_content);
        ImageView chatAvatarImg = viewHolder.getView(R.id.chat_avatar_img);
        RelativeLayout chatContainerRL = viewHolder.getView(R.id.chat_container_rl);
        contentTv.setText(itemDdata.getMessage());
        chatContainerRL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (dialogListModelList == null)
                    dialogListModelList = new ArrayList<>();
                dialogListModelList.clear();
                if (ChatInfoModel.TYPE_TEXT.equals(itemDdata.getType()))
                    dialogListModelList.add(new DialogListModel("复制", 1000));
                dialogListModelList.add(new DialogListModel("发送给朋友", 1001));
                dialogListModelList.add(new DialogListModel("收藏", 1002));
                dialogListModelList.add(new DialogListModel("删除", 1003));
                dialogListModelList.add(new DialogListModel("更多", 1004));
                new CommListDialog.Builder(context).setLists(dialogListModelList)
                        .setOnClickListener(new CommListDialog.IClick() {
                            @Override
                            public void onClick(View view, int tag) {
                                switch (tag) {
                                    case 1000:
                                        break;
                                    case 1001:
                                        break;
                                    case 1002:
                                        break;
                                    case 1003:
                                        break;
                                    case 1004:
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).create().show();
                return true;
            }
        });
        chatAvatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.startContactInfoActivity();
            }
        });
    }

    @Override
    protected boolean isNeedClickChangeBg() {
        return false;
    }
}
