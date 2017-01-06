package cn.chenyk.snakeviewsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.chenyk.snakeviewkit.activity.SnakeChatActivity;
import cn.chenyk.snakeviewkit.activity.SnakeContactActivity;
import cn.chenyk.snakeviewkit.activity.SnakeSessionActivity;

public class MainActivity extends AppCompatActivity {
    Button enterChatBtn, enterSessionBtn, enterContactBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enterChatBtn = (Button) findViewById(R.id.enter_chat_activity_btn);
        enterSessionBtn = (Button) findViewById(R.id.enter_session_btn);
        enterContactBtn = (Button) findViewById(R.id.enter_contact_btn);
        enterChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SnakeChatActivity.class));
            }
        });
        enterSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SnakeSessionActivity.class));
            }
        });
        enterContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SnakeContactActivity.class));

            }
        });
    }
}
