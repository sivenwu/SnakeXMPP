package com.sanke.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.snake.kit.core.managers.SnackService;
import com.snake.kit.interfaces.XmppLoginListener;
import com.snake.kit.ui.SnackActivity;

public class LoginActivity extends SnackActivity {

    private String url = "yyssqe.oicp.net";
    private String name = "siven02";
    private String password = "123";
    private int port = 25196;

    // 来自自己本地分支，测试注释
    private EditText loginEdit,passwordEidt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEdit = (EditText) findViewById(R.id.login_account);
        passwordEidt = (EditText) findViewById(R.id.login_password);

        loginEdit.setText(name);
        passwordEidt.setText(password);

        //login
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackService.login(loginEdit.getText().toString(), passwordEidt.getText().toString(), new XmppLoginListener() {
                    @Override
                    public void authenticated() {
                        // 鉴权成功
                        Toast.makeText(LoginActivity.this,"登录成功！！",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(Exception e, String message) {

                    }
                });
            }
        });
    }

    @Override
    public String getServer() {
        return url;
    }

    @Override
    public int getServerPort() {
        return port;
    }

    @Override
    public void bindByServiceConnect(SnackService mSnackService) {

    }

    @Override
    public void bindByServiceDisconnect() {

    }
}
