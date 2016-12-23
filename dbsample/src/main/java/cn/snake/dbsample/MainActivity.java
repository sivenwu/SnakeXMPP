package cn.snake.dbsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.snake.api.apptools.SnakeGsonUtil;

import java.util.List;
import java.util.UUID;

import cn.snake.dbkit.bean.ChatInfoModel;
import cn.snake.dbkit.bean.MemberInfo;
import cn.snake.dbkit.manager.DBOperationManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //测试
        Button insertBtn = (Button) findViewById(R.id.insert_data);
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatInfoModel model = new ChatInfoModel();
                MemberInfo modeld = new MemberInfo();
                model.setUserId("chenyk");
                model.setJid("testJid");
                model.setGroupId(8888);
                model.setMessage("message" + UUID.randomUUID().toString().substring(0, 5));
                DBOperationManager.get().insert(model);
            }
        });

        Button queryBtn = (Button) findViewById(R.id.query_data);
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ChatInfoModel> list = DBOperationManager.get().getGroupChatList("8888");
                if (list.size() == 0) {
                    Toast.makeText(MainActivity.this, "请先添加数据", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(MainActivity.this, SnakeGsonUtil.bean2json(list),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
