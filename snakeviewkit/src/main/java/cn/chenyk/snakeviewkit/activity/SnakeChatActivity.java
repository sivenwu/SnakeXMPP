package cn.chenyk.snakeviewkit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.chenyk.snakeviewkit.R;
import cn.chenyk.snakeviewkit.fragment.SnakeChatListFragment;

/**
 * Created by chenyk on 2016/12/28.
 */

public class SnakeChatActivity extends AppCompatActivity {
    private SnakeChatListFragment fragment;
    private FragmentManager fragmentManager;
    private Button mSendMessageBtn;
    private EditText mMessageEt;

    public static void startCurrentActivity(Activity activity) {
        Intent intent = new Intent(activity, SnakeChatActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake_chat);
        mSendMessageBtn = (Button) findViewById(R.id.chat_send_message_btn);
        mMessageEt = (EditText) findViewById(R.id.chat_message_et);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment = new SnakeChatListFragment();
        transaction.add(R.id.comm_fragment, fragment);
        transaction.commit();

        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.addTestDate(mMessageEt.getText().toString().trim());
                // TODO: 2017/1/3 将数据插入数据库
                mMessageEt.setText("");
            }
        });
    }
}
