package cn.lzh.common.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;

import cn.lzh.utils.NetWorkUtil;
import cn.lzh.utils.recevier.BaseDynamicRegisterReceiver;

/**
 * Created by lzh on 2018/12/25.<br/>
 * 监听网络状态
 */
public class NetworkStateReceiver extends BaseDynamicRegisterReceiver {

    private static final String TAG = "NetworkStateReceiver";
    private NetworkStateListener mListener;
    private boolean mConnected;

    public NetworkStateReceiver() {
        this.mListener = new NetworkStateListener() {
            @Override
            public void onConnected() {
                Log.d(TAG, "onConnected");
            }

            @Override
            public void onDisconnected() {
                Log.d(TAG, "onDisconnected");
            }
        };
    }

    public NetworkStateReceiver(@NonNull NetworkStateListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action == null){
            return;
        }
        switch (action){
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                onWifiStateChanged(intent);
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                onNetworkStateChanged(intent);
                break;
            case ConnectivityManager.CONNECTIVITY_ACTION:
                boolean connected = NetWorkUtil.isConnected(context);
                onNetworkStateChanged(connected);
                break;
        }
    }

    /**
     * 监听wifi开关状态
     * @param intent Intent
     */
    private void onWifiStateChanged(Intent intent) {
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
        switch (wifiState) {
            case WifiManager.WIFI_STATE_DISABLED:
                onNetworkStateChanged(false);
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                onNetworkStateChanged(true);
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                break;
            default:
                break;
        }
    }

    /**
     * 监听网络连接状态
     * @param intent Intent
     */
    private void onNetworkStateChanged(Intent intent) {
        NetworkInfo networkInfo = intent
                .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            boolean isConnected = state == NetworkInfo.State.CONNECTED;
            onNetworkStateChanged(isConnected);
        }
    }

    /**
     * 网络状态改变时回调
     * @param connected boolean
     */
    private void onNetworkStateChanged(boolean connected) {
        Log.d(TAG, "onNetworkStateChanged");
        if(mConnected != connected){// 过滤重复回调
            Log.d(TAG, "connected="+connected);
            mConnected = connected;
            if(connected){
                mListener.onConnected();
            }else{
                mListener.onDisconnected();
            }
        }
    }

    @Override
    protected IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        // 这个监听wifi的打开与关闭，与wifi的连接无关
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        // 这个监听wifi的连接状态即是否连上了一个有效无线路由
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        // 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        return filter;
    }

    public interface NetworkStateListener{
        void onConnected();
        void onDisconnected();
    }
}
