package cn.lzh.interview.receiver;

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import android.os.Handler
import android.os.Looper

/**
 * Created by lzh on 2019/1/16.
 *
 * 监听网络连接
 */
open class ConnectivityReceiver(private val callback: Callback) : BaseDynamicReceiver() {
    interface Callback {
        fun onAvailable()
        fun onUnavailable()
    }

    private var networkCallback: NetworkCallback? = null

    private val mainHandler: Handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            MSG_WHAT_AVAILABLE -> {
                callback.onAvailable()
            }
            MSG_WHAT_UNAVAILABLE -> {
                callback.onUnavailable()
            }
        }
        true
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            initNetworkCallback()
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun initNetworkCallback() {
        networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                mainHandler.obtainMessage(MSG_WHAT_AVAILABLE).sendToTarget()
            }

            override fun onUnavailable() {
                super.onUnavailable()
                mainHandler.obtainMessage(MSG_WHAT_UNAVAILABLE).sendToTarget()
            }
        }
    }

    override fun getIntentFilter(): IntentFilter {
        return IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            var networkInfo = intent.getParcelableExtra<NetworkInfo>(ConnectivityManager.EXTRA_NETWORK_INFO)
            if (networkInfo == null) {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                networkInfo = cm.activeNetworkInfo
            }
            if (networkInfo != null && networkInfo.isConnected && networkInfo.isAvailable) {
                mainHandler.obtainMessage(MSG_WHAT_AVAILABLE).sendToTarget()
            } else {
                mainHandler.obtainMessage(MSG_WHAT_UNAVAILABLE).sendToTarget()
            }
        }
    }

    override fun register(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkCallback?.let {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                cm.registerDefaultNetworkCallback(it)
            }
        }
        super.register(context)
    }

    override fun unregister(context: Context) {
        mainHandler.removeCallbacksAndMessages(null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkCallback?.let {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                cm.unregisterNetworkCallback(it)
            }
        }
        super.unregister(context)
    }

    companion object {
        private const val MSG_WHAT_AVAILABLE = 1
        private const val MSG_WHAT_UNAVAILABLE = 2
    }

}