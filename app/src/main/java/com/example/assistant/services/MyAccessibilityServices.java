package com.example.assistant.services;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.assistant.Constant;

import java.util.List;

/**
 * @author 作者：Somon
 * @date 创建时间：2017/12/5
 * @desception 辅助功能服务
 */
public class MyAccessibilityServices extends AccessibilityService {
    /**
     * 是否安装成功了
     */
    private boolean isInstalled;
    private BroadcastReceiver mReceiver;
    
    /**
     * 初始化操作
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d("MyAccessibilityServices", "service start");
        
        //动态修改在xml中配置的服务信息
       /* AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes=1;
        setServiceInfo(info);*/
        
        registerBroadReceiver();
    }
    
    /**
     * 接受在配置中配置的接收事件
     * accessibilityEventTypes
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("MyAccessibilityServices", "event.getEventType():" + event.getEventType());
        
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
            autoInstall(event);
        } else if (event.getPackageName().toString().contains("com.sonyericsson.home")) {
            autoRemoved(event);
        }
    }
    
    /**
     * 自动安装
     *
     * @param event
     */
    private void autoInstall(AccessibilityEvent event) {
        
        int eventType = event.getEventType();
        //响应窗口内容变化，窗口状态变化，控件滚动三种事件。
        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                || eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                || eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            install(Constant.InstallerConstant.installStart);
            
            if (isInstalled) {
                install(Constant.InstallerConstant.installComplete);
                isInstalled = false;
            }
        }
        
        //响应窗口内容变化，程序发出宣告
        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                || eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                || eventType == AccessibilityEvent.TYPE_ANNOUNCEMENT) {
            removed();
        }
    }
    
    private void install(String installProcess) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        
        int childCount = nodeInfo.getChildCount();
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo nodeInfoChild = nodeInfo.getChild(i);
            List<AccessibilityNodeInfo> nodeInstallList = nodeInfoChild.findAccessibilityNodeInfosByText(installProcess);
            if (nodeInstallList.size() > 0) {
                for (int j = 0; j < nodeInstallList.size(); j++) {
                    AccessibilityNodeInfo accessibilityNodeInfo = nodeInstallList.get(j);
                    if (accessibilityNodeInfo.getClassName().equals(Constant.ViewType.button)) {
                        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * 自动卸载
     * 点击卸载后包名从home变成installer了
     *
     * @param event
     */
    private void autoRemoved(AccessibilityEvent event) {
        int eventType = event.getEventType();
        //响应窗口内容变化，程序发出宣告
        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                || eventType == AccessibilityEvent.TYPE_ANNOUNCEMENT) {
        }
    }
    
    private void removed() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        int childCount = nodeInfo.getChildCount();
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo nodeChild = nodeInfo.getChild(i);
            List<AccessibilityNodeInfo> nodeInfoList = nodeChild.findAccessibilityNodeInfosByText(Constant.InstallerConstant.removedConfirm);
            if (nodeInfoList.size() > 0) {
                for (AccessibilityNodeInfo accessNodeInfo : nodeInfoList) {
                    if (accessNodeInfo.getClassName().equals(Constant.ViewType.button)) {
                        accessNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * apk被安装完成/卸载完成会收到广播
     * 首先收到广播，然后调用onAccessibilityEvent
     */
    private void registerBroadReceiver() {
        if (mReceiver == null) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(Constant.InstallerConstant.appAddedAction)) {
                        Log.d("MyAccessibilityServices", "app added");
                        isInstalled = true;
                    } else if (intent.getAction().equals(Constant.InstallerConstant.appRemovedAction)) {
                        Log.d("MyAccessibilityServices", "app removed");
                    }
                }
            };
            
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.InstallerConstant.appAddedAction);
            filter.addAction(Constant.InstallerConstant.appRemovedAction);
            filter.addDataScheme("package");
            registerReceiver(mReceiver, filter);
            Log.d("MyAccessibilityServices", "registerReceiver");
        }
    }
    
    @Override
    public void onInterrupt() {
    
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除注册
        unregisterReceiver(mReceiver);
    }
}
