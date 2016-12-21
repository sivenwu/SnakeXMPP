package cn.snake.dbsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import cn.snake.dbkit.bean.ChatInfoModel;
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
                model.setJid("chenyk_" + UUID.randomUUID().toString().substring(0, 5));
                DBOperationManager.get().insert(model);
            }
        });

        Button queryBtn = (Button) findViewById(R.id.query_data);
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ChatInfoModel> list = DBOperationManager.get().queryChatInfoLists();
                if (list.size() == 0) Toast.makeText(MainActivity.this, "请先添加数据",
                        Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, list.get(list.size() - 1).getJid(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
