package com.sanke.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.snake.kit.controllers.RosterController;

public class MainActivity extends AppCompatActivity {

    //    private String url = "http://siven-pc:9090";
    private String url = "192.168.244.6";
    private String name = "siven02";
    private String password = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取所有好友
        RosterController.getAllRosters();

        // 测试加好友
        RosterController.addRoster("wusy@siven-pc","wusy","group");

    }


}
