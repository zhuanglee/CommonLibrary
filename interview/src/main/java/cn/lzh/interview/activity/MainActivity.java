package cn.lzh.interview.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.lzh.interview.R;
import cn.lzh.interview.service.DownloadIntentService;
import cn.lzh.interview.service.MusicService;

import static cn.lzh.interview.Constants.ACTION_DEEP_LINK;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ServiceConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, MusicService.class));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            bindMusicService();
        }
    }

    private void bindMusicService() {
        if (conn == null) {
            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.i(TAG, "onServiceConnected");
                    if (service instanceof MusicService.MusicControlBinder) {
                        MusicService.MusicControlBinder binder = (MusicService.MusicControlBinder) service;
                        binder.start();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.i(TAG, "onServiceDisconnected");
                }
            };
        }
        bindService(new Intent(MusicService.INTENT_FILTER_ACTION), conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_service) {
            Intent service = new Intent(this, DownloadIntentService.class);
            service.putExtra("url", "test");
            startService(service);
        } else if (id == R.id.btn_app_links) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://zhuanglee.github.io/interview/router"));
            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "未匹配到 Activity", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.btn_deep_link) {
            Intent intent = new Intent(ACTION_DEEP_LINK, Uri.parse("app://lzh.cn/interview/router"));
            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "未匹配到 Activity", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
