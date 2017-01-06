package cn.chenyk.snakeviewkit.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import cn.chenyk.snakeviewkit.R;
import cn.chenyk.snakeviewkit.fragment.SnakeSessionListFragment;

/**
 * Created by chenyk on 2016/12/29.
 */

public class SnakeSessionActivity extends AppCompatActivity {
    SnakeSessionListFragment fragment;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake_session);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment = new SnakeSessionListFragment();
        transaction.add(R.id.frame_fragment, fragment);
        transaction.commit();
    }
}
