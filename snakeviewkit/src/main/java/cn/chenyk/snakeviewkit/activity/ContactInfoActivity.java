package cn.chenyk.snakeviewkit.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.chenyk.snakeviewkit.R;

/**
 * Created by chenyk on 2017/1/6.
 */

public class ContactInfoActivity extends AppCompatActivity {
    Button sendChatMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        sendChatMessage = (Button) findViewById(R.id.send_chat_message);
        sendChatMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnakeChatActivity.startCurrentActivity(ContactInfoActivity.this);
            }
        });
    }
}
