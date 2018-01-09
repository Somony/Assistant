package com.example.assistant.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.assistant.R;
import com.example.assistant.callback.ViewCallBack;
import com.example.assistant.services.MyAccessibilityServices;
import com.example.assistant.utils.AssistUtils;

/**
 * @author 作者：Somon
 * @date 创建时间：2017/12/5
 * @desception
 */
public class MainActivity extends AppCompatActivity implements ViewCallBack {
    
    private String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent() != null) {
            String action = getIntent().getAction();
            if (!TextUtils.isEmpty(action)) {
                switch (action) {
                    case Intent.ACTION_ASSIST:
                        redirectToBrowser();
                        killSelf();
                        break;
                    case Intent.ACTION_MAIN:
                        startService(new Intent(this, MyAccessibilityServices.class));
                        jumpToSetting(MyAccessibilityServices.class.getCanonicalName());
                        break;
                    default:
                        break;
                }
            }
            
        }
    }
    
    /**
     * 跳转到浏览器
     */
    private void redirectToBrowser() {
        Uri uri = Uri.parse("about:blank");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    
    /**
     * 跳到设置界面打开辅助功能
     */
    private void jumpToSetting(String accessibilityServiceName) {
        if (!AssistUtils.isAccessibilitySettingsOn(accessibilityServiceName, this)) {
            String action = Settings.ACTION_ACCESSIBILITY_SETTINGS;
            Intent intent = new Intent(action);
            startActivity(intent);
        }
    }
    
    private void killSelf() {
        finish();
    }
}
