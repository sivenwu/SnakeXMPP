package com.sanke.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.snake.api.apptools.LogTool;
import com.snake.api.data.MessageModel;
import com.snake.kit.controllers.PublicController;
import com.snake.kit.controllers.RosterController;
import com.snake.kit.controllers.SessionController;
import com.snake.kit.interfaces.ChatMessageListener;
import com.snake.kit.interfaces.ISnakeRosterListener;

import java.util.List;

import cn.snake.dbkit.bean.ContactModel;

public class MainActivity extends AppCompatActivity {

    //    private String url = "http://siven-pc:9090";
    private String url = "192.168.244.6";
    private String name = "siven02";
    private String password = "123";

    private TextView mainTv;
    private String getMessage;

    public  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000){
                mainTv.setText(getMessage);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取所有好友
        RosterController.getAllRosters(new ISnakeRosterListener() {
            @Override
            public void rosterEntires(List<ContactModel> contactModels) {
                LogTool.e("contactModels " + contactModels.size());
            }
        });


        // 测试加好友
//        RosterController.addRoster("wusy@siven-pc","wusy","group");
        mainTv = (TextView) findViewById(R.id.main_tv);

        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionController.sendMessage("wusy@siven-pc","hi 来自客户端消息");
            }
        });

        findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicController.logout();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });

        createChat();
    }

    private void createChat(){
        if (SessionController.createChat("wusy@siven-pc", new ChatMessageListener() {
            @Override
            public void onRecevie(MessageModel model, String message) {
                getMessage = getMessage + message + "\n";
                handler.sendEmptyMessage(1000);
            }
        })){
            LogTool.i("创建聊天会话成功！");
        }

    }


}
