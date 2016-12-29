package cn.snake.dbsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.snake.api.apptools.SnakeGsonUtil;
import com.snake.api.data.SnakeConstants;

import java.util.List;
import java.util.UUID;

import cn.snake.dbkit.bean.ChatInfoModel;
import cn.snake.dbkit.manager.DBOperationManager;

public class MainActivity extends AppCompatActivity {
    TextView contentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentTv = (TextView) findViewById(R.id.content_tv);
        //测试
        Button insertBtn = (Button) findViewById(R.id.insert_data);
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatInfoModel model = new ChatInfoModel();
                model.setUserId("chenyk");
                model.setJid("chenykJid");
                model.setGroupId(8888);
                model.setMessage("message" + UUID.randomUUID().toString().substring(0, 5));
                DBOperationManager.get().insert(model);
                QueryChatDatas();
            }
        });
        Button deleteBtn = (Button) findViewById(R.id.delete_data);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatInfoModel model = new ChatInfoModel();
                model.setJid("chenykJid");
                model.setGroupId(8888);
                DBOperationManager.get().delete(model);
                QueryChatDatas();
            }
        });
        Button updataBtn = (Button) findViewById(R.id.updata_data);
        updataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatInfoModel model = new ChatInfoModel();
                model.set_id((long) 1);
                model.setJid("chenykJid");
                model.setIsFrom(SnakeConstants.COMMON_STRING_TRUE);
                model.setGroupId(8888);
                DBOperationManager.get().update(model);
                QueryChatDatas();
            }
        });
        Button queryBtn = (Button) findViewById(R.id.query_data);
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryChatDatas();
            }
        });
    }

    /**
     * 查询数据
     */
    private void QueryChatDatas() {
        List<ChatInfoModel> list = DBOperationManager.get().getGroupChatList("8888");
        if (list.size() == 0) {
            contentTv.setText("");
            return;
        }
        contentTv.setText(SnakeGsonUtil.bean2json(list));
    }
}
