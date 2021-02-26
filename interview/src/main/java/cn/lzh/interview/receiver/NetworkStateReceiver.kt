package cn.lzh.interview.receiver

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import com.shhh.base.receiver.ConnectivityReceiver

/**
 * Created by lzh on 2019/10/22.
 *
 * 网络状态（网络连接状态 和 WIFI 状态）监听
 */
abstract class NetworkStateReceiver(callback: Callback) : ConnectivityReceiver(callback) {

    override fun getIntentFilter(): IntentFilter {
        val filter = super.getIntentFilter()
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        return filter
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (WifiManager.WIFI_STATE_CHANGED_ACTION == intent.action) {
            val state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
            onWifiStateChanged(state)
        } else {
            super.onReceive(context, intent)
        }
    }

    /**
     * 当 WIFI 状态发生改变时回调
     *
     * @param state int
     * @see android.net.wifi.WifiManager.EXTRA_WIFI_STATE
     */
    protected abstract fun onWifiStateChanged(state: Int)
}