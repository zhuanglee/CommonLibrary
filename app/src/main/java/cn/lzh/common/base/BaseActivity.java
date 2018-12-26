package cn.lzh.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.lang.reflect.Field;

import cn.lzh.common.R;
import cn.lzh.ui.dialog.WaitingDialog;


/**
 * 1.封装初始化Toolbar的方法；<br/>
 */
public abstract class BaseActivity extends BaseWatermarkActivity {

    protected WaitingDialog mWaitingDialog;

    /**
     * 是否使用转场动画
     */
    protected boolean mUseTransitionAnimation = true;

    protected Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    /**
     * 全屏<br/>
     * 在{@link #initToolbar()}之后调用
     */
    protected void fullscreen() {
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
    }

    protected final Toolbar initToolbar(CharSequence title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            throw new IllegalStateException("not found R.id.toolbar");
        }
        setSupportActionBar(toolbar);
        setTitle(title);
        return toolbar;
    }

    protected final Toolbar initToolbar(int title) {
        return initToolbar(getString(title));
    }

    protected final Toolbar initToolbar() {
        return initToolbar(getTitle());
    }

    /**
     * 初始化Toolbar
     * @param displayHomeAsUpEnabled 是否显示返回按钮
     * @return
     */
    protected final Toolbar initToolbar(boolean displayHomeAsUpEnabled) {
        Toolbar toolbar;
        if(displayHomeAsUpEnabled && getString(R.string.app_name).equals(getTitle())){
            toolbar = initToolbar(getClass().getSimpleName());
        }else{
            toolbar = initToolbar(getTitle());
        }
        setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);
        return toolbar;
    }

    /**
     * 设置是否显示返回按钮
     * @param showHome true显示返回按钮,false不显示
     */
    protected final void setDisplayHomeAsUpEnabled(boolean showHome) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showHome);
        }
    }

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
                actionBar.setTitle("");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // 屏蔽物理"菜单"按键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 禁止子类重写该方法，重写{@link #onMenuItemClicked(MenuItem)}处理菜单点击事件
     * @param item
     * @return
     */
    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
//        hideSoftInput();//点击菜单项，隐藏输入法
        if(item.getItemId() == android.R.id.home){
            if(onClickHomeButton()){
                return true;
            }
        }else if(onMenuItemClicked(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 菜单项被点击时回调
     * @param item
     */
    protected boolean onMenuItemClicked(MenuItem item){
        return false;
    }

    /**
     * 点击Home按钮时回调，默认为销毁当前界面，子类可重写
     * @return 是否处理了，true已处理，false未处理
     */
    protected boolean onClickHomeButton(){
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void startActivity(Intent intent) {
        if(mUseTransitionAnimation){
            this.startActivityForResult(intent, -1);
        }else{
            super.startActivity(intent);
        }
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.startActivity(intent, options);
        }else{
            ActivityCompat.startActivity(this, intent, options);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if(mUseTransitionAnimation){
            Bundle defaultOptions = ActivityOptionsCompat.makeCustomAnimation(this,
                    R.anim.slide_right_in, R.anim.slide_left_out).toBundle();
            ActivityCompat.startActivityForResult(this, intent, requestCode, defaultOptions);
        }else{
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.startActivityForResult(intent, requestCode, options);
        }else{
            ActivityCompat.startActivityForResult(this, intent, requestCode, options);
        }
    }

    @Override
    protected void onDestroy() {
        WaitingDialog.dismissAndRelease(mWaitingDialog);
        mWaitingDialog = null;
        super.onDestroy();
//        fixHuaWeiMemoryLeak();
//        fixInputMethodManagerLeak(this);
    }


    /**
     * 隐藏输入法
     */
    protected void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * 修复华为手机内存的泄露
     */
    public void fixHuaWeiMemoryLeak(){
        //测试
        try {
            Class<?> GestureBoostManagerClass = Class.forName("android.gestureboost.GestureBoostManager");
            Field sGestureBoostManagerField = GestureBoostManagerClass.getDeclaredField("sGestureBoostManager");
            sGestureBoostManagerField.setAccessible(true);
            Object gestureBoostManager = sGestureBoostManagerField.get(GestureBoostManagerClass);
            Field contextField = GestureBoostManagerClass.getDeclaredField("mContext");
            contextField.setAccessible(true);
            if (contextField.get(gestureBoostManager)==this) {
                contextField.set(gestureBoostManager, null);
            }
        }  catch (Throwable t) {
//            t.printStackTrace();
            // 忽略该异常
        }

    }

    /**
     * 修复InputMethodManager导致Activity的内存泄露<br/>
     * 问题链接：http://blog.csdn.net/sodino/article/details/32188809
     * @param destContext
     */
    private void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mLastSrvView","mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            } catch (Throwable t) {
//                t.printStackTrace();
            }
        }
    }

}
