package com.example.assistant.services;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.example.assistant.callback.ViewCallBack;
import com.example.assistant.presenter.InstallPresenter;

/**
 * @author 作者：Somon
 * @date 创建时间：2017/12/5
 * @desception 辅助功能类服务
 */
public class MyAccessibilityServices extends AccessibilityService implements ViewCallBack {
    private String tag = "MyAccessibilityServices";
    private InstallPresenter mInstallPresenter = new InstallPresenter(this);
    
    /**
     * 初始化操作
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        tag = "BaseAccessibility";
        Log.d(tag, "service start");
        
        //动态修改在xml中配置的服务信息
       /* AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes=1;
        setServiceInfo(info);*/
        
        mInstallPresenter.registerBroadReceiver();
    }
    
    /**
     * 接受在配置中配置的接收事件
     * accessibilityEventTypes
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(tag, "event.getEventType():" + event.getEventType());
        
        /**
         * canRetrieveWindowContent属性和设置它为true
         * @return
         */
        //AccessibilityNodeInfo nodeInfo = event.getSource();
        
        /**
         * 也能获取到nodeInfo独享
         * 有时候两种方式获取的childNode个数不一致
         */
        
        if (event.getPackageName().toString().contains("com.android.packageinstaller") ||
                event.getPackageName().toString().contains("com.google.android.packageinstaller")) {
            mInstallPresenter.autoInstall(event);
        } else if (event.getPackageName().toString().contains("com.sonyericsson.home")) {
            mInstallPresenter.autoRemoved(event);
        }
    }
    
    @Override
    public void onInterrupt() {
    
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除注册
        mInstallPresenter.unregisterBroadReceiver();
    }
}
