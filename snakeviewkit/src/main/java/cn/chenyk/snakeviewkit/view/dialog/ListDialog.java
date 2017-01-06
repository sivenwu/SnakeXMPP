package cn.chenyk.snakeviewkit.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import cn.chenyk.snakeviewkit.R;
import cn.chenyk.snakeviewkit.fragment.DialogListFragment;

/**
 * Created by chenyk on 2016/12/30.
 */

public class ListDialog extends DialogFragment {
    @Override
    public void onStart() {
        super.onStart();
        initWindow();
    }

    protected void initWindow() {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        if (window != null) {

            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            window.setLayout((int) (dm.widthPixels * 0.85),
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setLayout((int) (dm.widthPixels * 0.85),
                    600);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return inflater.inflate(R.layout.comm_only_framelayout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getChildFragmentManager().beginTransaction().replace(
                R.id.comm_fragment, new DialogListFragment()).commit();
    }

    /**
     * show对话框
     *
     * @param activity 目标activity
     */
    public void showDialog(AppCompatActivity activity) {
        show(activity.getSupportFragmentManager(), activity.getClass().getSimpleName());
    }
}
