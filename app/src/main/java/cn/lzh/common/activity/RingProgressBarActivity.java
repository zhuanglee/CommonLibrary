package cn.lzh.common.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseActivity;
import cn.lzh.common.view.RingProgressBar;

public class RingProgressBarActivity extends BaseActivity {
    private RingProgressBar mRoundProgressBar1;
    private RingProgressBar mRoundProgressBar2;
    private Button mBtnStart;
    private boolean mIsRunning;
    private int progress;
    private boolean mIsIncrement = true;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mIsIncrement) {
                progress++;
            } else {
                progress--;
            }
            if (progress == 100) {
                mIsIncrement = false;
            } else if (progress == 0) {
                mIsIncrement = true;
            }
            mRoundProgressBar1.setProgress(progress);
            mRoundProgressBar2.setProgress(progress);
            handler.sendMessageDelayed(handler.obtainMessage(0), 100);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_progress_bar);
        initToolbar(true);
        mBtnStart = findViewById(R.id.btn_start);
        mRoundProgressBar1 = findViewById(R.id.roundProgressBar1);
        mRoundProgressBar2 = findViewById(R.id.roundProgressBar2);
    }

    public void onClick(View v) {
        mIsRunning = !mIsRunning;
        if (mIsRunning) {
            handler.sendEmptyMessage(0);
            mBtnStart.setText("暂停");
        } else {
            handler.removeCallbacksAndMessages(null);
            mBtnStart.setText("开始");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mIsRunning){
            handler.sendEmptyMessage(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsRunning){
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onDestroy() {
        mIsRunning = false;
        handler = null;
        super.onDestroy();
    }
}
