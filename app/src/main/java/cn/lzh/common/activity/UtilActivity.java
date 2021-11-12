package cn.lzh.common.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cn.lzh.common.R;
import cn.lzh.common.base.BaseActivity;
import cn.lzh.common.databinding.ActivityUtilBinding;
import cn.lzh.common.service.DemoService;
import cn.lzh.utils.DeviceUtil;
import cn.lzh.utils.DrawableUtil;
import cn.lzh.utils.FileUtil;
import cn.lzh.utils.IntentUtil;
import cn.lzh.utils.NetWorkUtil;
import cn.lzh.utils.SystemUtil;
import cn.lzh.utils.recevier.NetworkStateReceiver;

public class UtilActivity extends BaseActivity implements View.OnClickListener, NetworkStateReceiver.NetworkStateListener {

    private static final String TAG = "UtilActivity";

    private ActivityUtilBinding mBinding;
    private int mRadius;
    private NetworkStateReceiver networkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_util);
        initToolbar(true);
        networkStateReceiver = new NetworkStateReceiver(this);
        mRadius = DeviceUtil.dip2px(this, 5);
        updateMemoryInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkStateReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkStateReceiver.unregister(this);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, DemoService.class));
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_start_service){
            startService(new Intent(this, DemoService.class));
            updateMemoryInfo();
        }else if(id == R.id.btn_stop_service){
            SystemUtil.stopRunningService(this, DemoService.class.getName());
            updateMemoryInfo();
        }else if (id == R.id.btn_open_wireless_settings) {
            startActivity(IntentUtil.openWirelessSettings());
        } else if (id == R.id.btn_random) {
            mBinding.btnRandom.setBackgroundDrawable(DrawableUtil.getRandomColorSelector(mRadius));
        } else if (id == R.id.btn_snap_shot) {
            Log.i(TAG, String.format("getStatusBarHeight：1=%d,2=%d",
                DeviceUtil.getStatusBarHeight(getApplicationContext()),
                DeviceUtil.getStatusBarHeight(this)));
            mBinding.imageView.setImageDrawable(null);
            Bitmap bitmap = DeviceUtil.snapShotWithoutStatusBar(this);
            mBinding.imageView
                .setImageDrawable(DrawableUtil.getPressedDrawable(new BitmapDrawable(bitmap)));
            mBinding.btnSnapShot.setBackgroundDrawable(DrawableUtil.getSelector(
                DrawableUtil.getRandomGradientDrawable(mRadius),
                DrawableUtil.getRandomGradientDrawable(mRadius)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, data == null ? "data is null" : data.toString());
    }

    @Override
    public void onConnected() {
        onNetworkStateChanged();
    }

    @Override
    public void onDisconnected() {
        onNetworkStateChanged();
    }

    private void onNetworkStateChanged() {
        mBinding.tvNetworkInfo.setText(String.format("isConnected=%s, isWifi=%s, isFastMobileNetwork=%s, getNetworkType=%s",
                NetWorkUtil.isConnected(this),
                NetWorkUtil.isWifi(this),
                NetWorkUtil.isFastMobileNetwork(this),
                NetWorkUtil.getNetworkType(this)));
    }

    private void updateMemoryInfo() {
        mBinding.tvMemory.setTextColor(DrawableUtil.getGradientColor(
                DrawableUtil.getRandomColor(), DrawableUtil.getRandomColor(), 0.5f));
        mBinding.tvMemory.setText("");
        Context appContext = this.getApplicationContext();
        log("getCpuCoreNumber=" + SystemUtil.getCpuCoreNumber());
        log("getDefaultThreadPoolSize=" + SystemUtil.getDefaultThreadPoolSize(1000));
        log("getSystemVersion=" + SystemUtil.getSystemVersion());
        log("getSDKVersion=" + SystemUtil.getSDKVersion());
        log("getPhoneModel=" + SystemUtil.getPhoneModel());
        log("getCurrentRuntimeValue=" + SystemUtil.getCurrentRuntimeValue());
        log("getDeviceId=" + SystemUtil.getDeviceId(appContext));
        log("getWifiIpAddress=" + SystemUtil.getWifiIpAddress(appContext));
        log("isApplicationInBackground=" + SystemUtil.isApplicationInBackground(appContext));
        log("getTopActivity=" + SystemUtil.getTopActivity(appContext));
        log("isMainProcess=" + SystemUtil.isMainProcess(appContext));
        log("getProcessName=" + SystemUtil.getProcessName(Process.myPid()));
        try {
            log("getUidByPackageName=" + SystemUtil.getUidByPackageName(appContext, appContext.getPackageName()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        log("isRunningService=" + SystemUtil.isRunningService(appContext, DemoService.class.getName()));
        log("getDeviceUsableMemory=" + FileUtil.formatFileSize(this, SystemUtil.getDeviceUsableMemory(this)));
        log("getAllApps=" + SystemUtil.getAllApps(appContext));
        log("getSign=" + SystemUtil.getSign(appContext, appContext.getPackageName()));
    }

    private void log(String str) {
        mBinding.tvMemory.setText(mBinding.tvMemory.getText() + "\n" + str);
    }

}
