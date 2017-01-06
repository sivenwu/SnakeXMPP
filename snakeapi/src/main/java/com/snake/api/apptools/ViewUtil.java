package com.snake.api.apptools;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by chenyk on 2017/1/4.
 */

public class ViewUtil {
    private static final float scale = SnakeUtilKit.getSnakeApp().getResources().getDisplayMetrics().density;

    /**
     * 获取屏幕宽度
     */
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     */
    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }


    /**
     * dp转px
     *
     * @param dp
     */
    public static float dp2Px(float dp) {
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 获得状态栏的高度
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
