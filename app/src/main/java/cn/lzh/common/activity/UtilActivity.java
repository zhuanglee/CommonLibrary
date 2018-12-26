package cn.lzh.common.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.Locale;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseActivity;
import cn.lzh.common.databinding.ActivityUtilBinding;
import cn.lzh.utils.recevier.NetworkStateReceiver;
import cn.lzh.utils.DeviceUtil;
import cn.lzh.utils.DrawableUtil;
import cn.lzh.utils.IntentUtil;
import cn.lzh.utils.NetWorkUtil;
import cn.lzh.utils.SystemUtil;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkStateReceiver.register(this);
        updateMemoryInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkStateReceiver.unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_wireless_settings:
                startActivity(IntentUtil.openWirelessSettings());
                break;
            case R.id.btn_random:
                mBinding.btnRandom.setBackgroundDrawable(DrawableUtil.getRandomColorSelector(mRadius));
                break;
            case R.id.btn_snap_shot:
                Log.i(TAG, String.format("getStatusBarHeight：1=%d,2=%d",
                        DeviceUtil.getStatusBarHeight(getApplicationContext()),
                        DeviceUtil.getStatusBarHeight(this)));
                mBinding.imageView.setImageDrawable(null);
                Bitmap bitmap = DeviceUtil.snapShotWithoutStatusBar(this);
                mBinding.imageView.setImageDrawable(DrawableUtil.getPressedDrawable(new BitmapDrawable(bitmap)));
                mBinding.btnSnapShot.setBackgroundDrawable(DrawableUtil.getSelector(
                        DrawableUtil.getRandomGradientDrawable(mRadius),
                        DrawableUtil.getRandomGradientDrawable(mRadius)));
                break;
        }
        updateMemoryInfo();
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
        mBinding.tvMemory.setText(String.format(Locale.CHINESE,
                "%dMB", SystemUtil.getDeviceUsableMemory(this)));
        mBinding.tvMemory.setTextColor(DrawableUtil.getGradientColor(
                DrawableUtil.getRandomColor(), DrawableUtil.getRandomColor(), 0.5f));
    }

}
