package com.example.assistant.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.assistant.Constant;
import com.example.assistant.callback.ViewCallBack;
import com.example.assistant.services.MyAccessibilityServices;

import java.util.List;

/**
 * @author 作者：LHC
 * @date 创建时间：2018/1/8
 * @corporation 公司：anerfa
 * @desception 辅助服务安装和卸载
 * 包名：com.android.packageinstaller,com.google.android.packageinstaller,com.sonyericsson.home
 */

public class InstallPresenter {
    
    private String tag = "InstallPresenter";
    private ViewCallBack mContext;
    private MyAccessibilityServices mService;
    /**
     * 是否安装成功了
     */
    private boolean isInstalled;
    private BroadcastReceiver mReceiver;
    
    public InstallPresenter(ViewCallBack context) {
        mContext = context;
        mService = ((MyAccessibilityServices) context);
    }
    
    /**
     * 自动安装
     *
     * @param event
     */
    public void autoInstall(AccessibilityEvent event) {
        
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
        AccessibilityNodeInfo nodeInfo = mService.getRootInActiveWindow();
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
    public void autoRemoved(AccessibilityEvent event) {
        int eventType = event.getEventType();
        //响应窗口内容变化，程序发出宣告
        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                || eventType == AccessibilityEvent.TYPE_ANNOUNCEMENT) {
        }
    }
    
    private void removed() {
        AccessibilityNodeInfo nodeInfo = mService.getRootInActiveWindow();
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
    public void registerBroadReceiver() {
        if (mReceiver == null) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(Constant.InstallerConstant.appAddedAction)) {
                        Log.d(tag, "app added");
                        isInstalled = true;
                    } else if (intent.getAction().equals(Constant.InstallerConstant.appRemovedAction)) {
                        Log.d(tag, "app removed");
                    }
                }
            };
            
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.InstallerConstant.appAddedAction);
            filter.addAction(Constant.InstallerConstant.appRemovedAction);
            filter.addDataScheme("package");
            mService.registerReceiver(mReceiver, filter);
            Log.d(tag, "registerReceiver");
        }
    }
    
    public void unregisterBroadReceiver() {
        if (mReceiver != null) {
            mService.unregisterReceiver(mReceiver);
        }
    }
}
