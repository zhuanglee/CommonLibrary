package cn.lzh.common.base;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import cn.lzh.common.R;


/**
 * @see #initToolbar(boolean)
 * @see #fullscreen()
 */
public abstract class BaseActivity extends BaseWatermarkActivity {

    /**
     * 是否全屏
     */
    private boolean isFullScreen;

    /**
     * 初始化Toolbar
     * @param displayHomeAsUpEnabled 是否显示返回按钮
     */
    protected final void initToolbar(boolean displayHomeAsUpEnabled) {
        String title = getIntent().getStringExtra("title");
        initToolbar(displayHomeAsUpEnabled, title == null ? getTitle().toString() : title);
    }

    /**
     * 初始化Toolbar
     * @param displayHomeAsUpEnabled 是否显示返回按钮
     * @param title 标题
     */
    protected final void initToolbar(boolean displayHomeAsUpEnabled, String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            throw new IllegalStateException("not found R.id.toolbar");
        }
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);
        setTitle(title);
        if(isFullScreen){
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
    }

    /**
     * 全屏
     */
    protected final void fullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
        isFullScreen = true;
    }

    @CallSuper
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            TextView tvTitle = findViewById(R.id.tv_title);
            if(tvTitle == null){
                actionBar.setTitle(title);
            }else{
                tvTitle.setText(title);
                actionBar.setTitle(null);
            }
        }
    }

    @CallSuper
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // 屏蔽物理"菜单"按键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @CallSuper
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
