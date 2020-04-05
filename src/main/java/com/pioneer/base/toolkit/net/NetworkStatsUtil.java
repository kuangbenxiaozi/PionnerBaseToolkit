package com.pioneer.base.toolkit.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;

/**
 * Created by zyh on 16/3/15.
 */
public class NetworkStatsUtil {
    /**
     * 移动连接
     */
    public static final int MOBILE_CONNECTED = 1;
    /**
     * wifi连接
     */
    public static final int WIFI_CONNECTED = 2;
    /**
     * 网络不可用
     */
    public static final int DISCONNECTED_NETWORK = 0;
    /**
     * 3G网络
     */
    public static final int MOBILE_CONNECTED_UNDER_3G = 3;

    /**
     * 返回网络状态
     *
     * @param context
     * @return
     */
    public static int checkNetStatus(Context context) {
        if (context == null) {
            return MOBILE_CONNECTED;
        }

//        if (Utils.IsAirModeOn(context)) {
//            return DISCONNECTED_NETWORK;
//        }
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileInfo != null) {
            NetworkInfo.State mobile = mobileInfo.getState();
            if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING)
                return MOBILE_CONNECTED;
        }
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null) {
            NetworkInfo.State wifi = wifiInfo.getState();
            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)
                return WIFI_CONNECTED;
        }
        // showTips();
        return DISCONNECTED_NETWORK;
    }

    private static int getAppUid() {
        return android.os.Process.myUid();
    }

    public static String getAppTotalBytes() {
        int uid = getAppUid();
        String appTotalBytes = String.valueOf(TrafficStats.getUidRxBytes(uid) + TrafficStats.getUidTxBytes(uid));
        return appTotalBytes;
    }

    public static boolean isNetWorkAvaliable(Context context) {
        ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null) {
            return false;
        } else {
            int netType = info.getType();
            return netType == ConnectivityManager.TYPE_WIFI ? info.isConnected() : (netType == ConnectivityManager.TYPE_MOBILE && info.isConnected());
        }
    }

    /**
     * 判断网络情况
     *
     * @param context
     * @return
     */
//    public static boolean isGoodNetwork(Context context) {
//        NetworkStatus status = NetMonitor.getInstance(context).getStatus();
//        return status == NetworkStatus.Wifi || status == NetworkStatus.FourG;
//    }
}
