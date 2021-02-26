package cn.lzh.interview.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.lzh.interview.R;

import static cn.lzh.interview.Constants.ACTION_DEEP_LINK;

/**
 * Created by lzh on 2018/9/25.<br/>
 */
public class RouterActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private EditText etContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router);
        etContent = findViewById(R.id.et_content);
        etContent.setOnEditorActionListener(this);
        handleAppLinks((TextView) findViewById(R.id.tv_content));
    }

    private void handleAppLinks(TextView tv) {
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        tv.setText(String.format("action=%s, data=%s", appLinkAction, appLinkData));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_GO){
            go();
            return true;
        }
        return false;
    }

    public void go() {
        String trim = etContent.getText().toString().trim();
        try {
            startActivity(new Intent(ACTION_DEEP_LINK, Uri.parse(trim)));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "未匹配到Activity", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        if(view.getId() == R.id.btn_go){
            go();
        }
    }
}
