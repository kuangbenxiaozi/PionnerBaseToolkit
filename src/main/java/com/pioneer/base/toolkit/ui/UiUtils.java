package com.pioneer.base.toolkit.ui;

import android.content.Context;
import android.view.View;

public class UiUtils {
    /**
     * dip/dp转像素
     *
     * @param context  context
     * @param dipValue dip或 dp大小
     * @return 像素值
     */
    public static int dip2px(Context context,float dipValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * px转位dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕到view顶部的距离, 包括状态栏高度
     *
     * @param view
     * @return
     */
    public static int findViewTopOnScreen(View view) {
        int[] locOnSrc = new int[2];
        view.getLocationOnScreen(locOnSrc);
        return locOnSrc[1];
    }

    /**
     * 获取屏幕到view底部的距离, 包括状态栏高度
     *
     * @param view
     * @return
     */
    public static int findViewBottomOnScreen(View view) {
        int[] locOnSrc = new int[2];
        view.getLocationOnScreen(locOnSrc);
        int viewBtmLoc = locOnSrc[1] + view.getHeight();
        return viewBtmLoc;
    }

    /**
     * 获取屏幕到view左边的距离
     *
     * @param view
     * @return
     */
    public static int findViewLeftOnScreen(View view) {
        int[] locOnSrc = new int[2];
        view.getLocationOnScreen(locOnSrc);
        return locOnSrc[0];
    }

    /**
     * 获取屏幕到view右边边的距离
     *
     * @param view
     * @return
     */
    public static int findViewRightOnScreen(View view) {
        int[] locOnSrc = new int[2];
        view.getLocationOnScreen(locOnSrc);
        return locOnSrc[0] + view.getWidth();
    }
}
