package com.sanke.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.snake.kit.SnakeKit;
import com.snake.kit.controllers.PublicController;
import com.snake.kit.interfaces.XmppLoginListener;

public class LoginActivity extends AppCompatActivity {

    private String url = "yyssqe.oicp.net";
    private String name = "siven02";
    private String password = "123";
    private int port = 25196;

    // 来自自己本地分支，测试注释
    private EditText loginEdit, passwordEidt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化snake
        SnakeKit.getKit().init(getApplication(), url, port);

        loginEdit = (EditText) findViewById(R.id.login_account);
        passwordEidt = (EditText) findViewById(R.id.login_password);

        loginEdit.setText(name);
        passwordEidt.setText(password);

        //login
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicController.login(loginEdit.getText().toString(), passwordEidt.getText().toString(), new XmppLoginListener() {
                    @Override
                    public void authenticated() {
                        // 鉴权成功
                        Toast.makeText(LoginActivity.this, "登录成功！！", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(Exception e, String message) {

                    }
                });
            }
        });

    }
}
