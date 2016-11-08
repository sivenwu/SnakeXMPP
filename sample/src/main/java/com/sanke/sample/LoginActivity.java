package com.sanke.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.snake.kit.apptools.SnakePref;
import com.snake.kit.core.data.SnakeContacts;
import com.snake.kit.core.managers.SnackService;
import com.snake.kit.interfaces.XmppCononectListener;
import com.snake.kit.ui.SnackActivity;

public class LoginActivity extends SnackActivity {

    private SnackService mSnackService;

    private String url = "yyssqe.oicp.net";
    private String name = "siven02";
    private String password = "123";
    private int port = 25196;

    private EditText loginEdit,passwordEidt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEdit = (EditText) findViewById(R.id.login_account);
        passwordEidt = (EditText) findViewById(R.id.login_password);

        loginEdit.setText(name);
        passwordEidt.setText(password);

        SnakePref.putObject(SnakeContacts.SMACK_USER_ACCOUNT,loginEdit.getText().toString());
        SnakePref.putObject(SnakeContacts.SMACK_USER_PASSWORD,passwordEidt.getText().toString());
        SnakePref.putObject(SnakeContacts.SMACK_SERVER,url);
        SnakePref.putObject(SnakeContacts.SMACK_SERVER_PORT,port);

        //login
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackService.connect(loginEdit.getText().toString()
                        , passwordEidt.getText().toString()
                        , url, port, new XmppCononectListener() {
                            @Override
                            public void connected() {

                            }

                            @Override
                            public void authenticated() {
                                // 鉴权成功
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
    public void bindByServiceConnect(SnackService mSnackService) {
        this.mSnackService = mSnackService;
    }

    @Override
    public void bindByServiceDisconnect() {

    }
}
