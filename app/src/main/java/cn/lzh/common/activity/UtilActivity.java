package cn.lzh.common.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseActivity;
import cn.lzh.common.databinding.ActivityUtilBinding;
import cn.lzh.utils.BitmapUtil;
import cn.lzh.utils.DeviceUtil;
import cn.lzh.utils.DrawableUtil;
import cn.lzh.utils.IntentUtil;
import cn.lzh.utils.SystemUtil;
import cn.lzh.utils.ToastUtil;

public class UtilActivity extends BaseActivity implements View.OnClickListener {

    public static final String ACTION_SHORTCUT_ACTIVITY_UTIL = "cn.lzh.common.UtilActivity";
    public static final String BROWSER_URI = "https://github.com/zhuanglee/CommonLibrary";
    public static final String TEL = "17610170827";
    public static final String EMAIL = "devlzh93@gmail.com";
    public static final String EMAIL_CC = "devlzh@qq.com";
    public static final String EMAIL_BCC = "devlzh@qq.com";
    public static final String EMAIL_SUBJECT = "email subject";
    public static final String EMAIL_TEXT = "test send email";
    public static final String SHORTCUT_NAME = "UtilActivity";
    public static final int REQUEST_CODE_CALL = 0;
    public static final int REQUEST_CODE_INSTALL_SHORTCUT = 1;
    private int radius;
    private ActivityUtilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radius = DeviceUtil.dip2px(this, 5);
        binding = ActivityUtilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initToolbar(true);
        initListener();
    }

    private void initListener() {
        // IntentUtil
        binding.btnCall.setOnClickListener(this);
        binding.btnInstallShortcut.setOnClickListener(this);
        binding.btnOpenAppDetails.setOnClickListener(this);
        binding.btnOpenBrowser.setOnClickListener(this);
        binding.btnOpenContacts.setOnClickListener(this);
        binding.btnOpenHome.setOnClickListener(this);
        binding.btnOpenMap.setOnClickListener(this);
        binding.btnOpenWifiSettings.setOnClickListener(this);
        binding.btnOpenWirelessSettings.setOnClickListener(this);
        binding.btnSendEmail.setOnClickListener(this);
        binding.btnSendSms.setOnClickListener(this);
        binding.btnShare.setOnClickListener(this);
        binding.btnStartApp.setOnClickListener(this);
        binding.btnAddDynamicShortcuts.setOnClickListener(this);
        binding.btnRemoveDynamicShortcuts.setOnClickListener(this);
        // other
        binding.btnRandom.setOnClickListener(this);
        binding.btnSnapShot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (handleAction(id)) {
            return;
        }
        if (id == R.id.btn_random) {
            binding.btnRandom.setBackgroundDrawable(DrawableUtil.getRandomColorSelector(radius));
        } else if (id == R.id.btn_snap_shot) {
            binding.imageView.setImageDrawable(null);
            Bitmap bitmap = DeviceUtil.snapShotWithoutStatusBar(this);
            binding.imageView.setImageDrawable(DrawableUtil.getSelector(bitmap));
            binding.btnSnapShot.setBackgroundDrawable(DrawableUtil.getRandomGradientSelector(radius));
            saveImage(bitmap);
        }
        updateMemoryInfo();
    }

    private boolean handleAction(int id) {
        Intent intent = null;
        if (id == R.id.btn_call) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL);
                return true;
            } else {
                intent = IntentUtil.getCall(TEL);
            }
        } else if (id == R.id.btn_open_call) {
            intent = IntentUtil.getOpenCall(TEL);
        }else if (id == R.id.btn_open_app_details) {
            intent = IntentUtil.getOpenAppDetail(getPackageName());
        } else if (id == R.id.btn_open_browser) {
            intent = IntentUtil.getOpenBrowser(BROWSER_URI);
        } else if (id == R.id.btn_open_contacts) {
            intent = IntentUtil.getOpenContacts();
        } else if (id == R.id.btn_open_home) {
            intent = IntentUtil.getOpenHome();
        } else if (id == R.id.btn_open_map) {
            intent = IntentUtil.getOpenMap(121.46f , 31.28f);
        }  else if (id == R.id.btn_open_wifi_settings) {
            intent = IntentUtil.getOpenWifiSettings();
        } else if (id == R.id.btn_open_wireless_settings) {
            intent = IntentUtil.getOpenWirelessSettings();
        }else if (id == R.id.btn_send_email) {
            intent = IntentUtil.getSendEmail(new String[]{EMAIL},
                    new String[]{EMAIL_CC}, new String[]{EMAIL_BCC},
                    EMAIL_SUBJECT, EMAIL_TEXT);
        } else if (id == R.id.btn_send_sms) {
            intent = IntentUtil.getSendSms(TEL, "test sms body");
        } else if (id == R.id.btn_share) {
            intent = IntentUtil.getShareTextIntent("share subject", "share text", "share title");
        } else if (id == R.id.btn_start_app) {
            intent = IntentUtil.getLaunchIntentForPackage(this, getPackageName());
        } else if (id == R.id.btn_install_shortcut) {
            Intent actionIntent = new Intent(ACTION_SHORTCUT_ACTIVITY_UTIL);
            IntentUtil.installShortcut(this, SHORTCUT_NAME,
                    R.mipmap.ic_launcher, actionIntent, REQUEST_CODE_INSTALL_SHORTCUT);
            return true;
        }  else if (id == R.id.btn_add_dynamic_shortcuts) {
            Intent actionIntent = new Intent(ACTION_SHORTCUT_ACTIVITY_UTIL);
            IntentUtil.addDynamicShortcuts(this, SHORTCUT_NAME,
                    R.mipmap.ic_launcher, actionIntent);
            return true;
        }  else if (id == R.id.btn_remove_dynamic_shortcuts) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                IntentUtil.removeDynamicShortcuts(this, SHORTCUT_NAME);
            }else{
                ToastUtil.show("不支持移除动态快捷方式");
            }
            return true;
        }
        if(intent != null){
            startActivitySafe(intent);
            return true;
        }
        return false;
    }

    private void startActivitySafe(Intent intent) {
        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            startActivity(intent);
        } else {
            ToastUtil.show("未匹配到 Activity");
        }
    }

    private void saveImage(Bitmap bitmap) {
        CharSequence timestamp = DateFormat.format("yyyyMMddHHmmss", Calendar.getInstance());
        File file = new File(getFilesDir(), "screenshot_" + timestamp + ".png");
        BitmapUtil.saveImage(bitmap, file, Bitmap.CompressFormat.PNG);
        IntentUtil.getNotifyMediaScanner(this, file);
    }

    private void updateMemoryInfo() {
        binding.tvMemory.setText(String.format(Locale.CHINESE,
                "%dMB", SystemUtil.getDeviceUsableMemory(this)));
        binding.tvMemory.setTextColor(DrawableUtil.getGradientColor(
                DrawableUtil.getRandomColor(), DrawableUtil.getRandomColor(), 0.5f));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivitySafe(IntentUtil.getCall(TEL));
            } else {
                ToastUtil.show("打电话权限被拒");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_CODE_INSTALL_SHORTCUT == requestCode){
            if(Activity.RESULT_OK == resultCode){
                ToastUtil.show("快捷方式添加成功");
            }else{
                ToastUtil.show("快捷方式添加失败");
            }
        }
    }
}
