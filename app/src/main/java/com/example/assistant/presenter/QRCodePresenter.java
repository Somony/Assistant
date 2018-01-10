package com.example.assistant.presenter;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.assistant.Constant;
import com.example.assistant.callback.ViewCallBack;
import com.example.assistant.services.MyAccessibilityServices;

import java.util.List;

/**
 * @author 作者：LHC
 * @date 创建时间：2018/1/10
 * @corporation 公司：anerfa
 * @desception 扫描二维码后自动打开
 * 包名：mark.qrcode
 */

public class QRCodePresenter {
    
    private final String tag = "QRCodePresenter";
    private final ViewCallBack mContext;
    private final MyAccessibilityServices mService;
    
    public QRCodePresenter(ViewCallBack context) {
        mContext = context;
        mService = ((MyAccessibilityServices) context);
    }
    
    public void autoOpenBrowser(AccessibilityEvent event) {
        int eventType = event.getEventType();
        Log.d(tag, "eventType:" + eventType);
        //扫描二维码后的界面变化
        if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED || eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                || eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            openBrowser();
        }
    }
    
    private void openBrowser() {
        AccessibilityNodeInfo rootWindow = mService.getRootInActiveWindow();
        for (int i = 0; i < rootWindow.getChildCount(); i++) {
            AccessibilityNodeInfo rootInfo = rootWindow.getChild(i);
            List<AccessibilityNodeInfo> linkBtnList = rootInfo.findAccessibilityNodeInfosByViewId(Constant.QRCodeConstant.linkBtnId);
            Log.d(tag, "linkBtnList.size():" + linkBtnList.size());
            if (linkBtnList.size() > 0) {
                linkBtnList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }
}
