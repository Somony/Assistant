package com.example.assistant;

import android.content.Intent;

/**
 * @author 作者：somon
 * @date 创建时间：2017/11/29
 * @desception 辅助功能相关的变量定义
 */

public class Constant {
    
    public static class ViewType {
        public static String textView = "android.widget.TextView";
        public static String button = "android.widget.Button";
    }
    
    public static class InstallerConstant {
        public static String installStart = "安装";
        public static String installComplete = "完成";
        public static String removedStart = "卸载";
        public static String removedConfirm = "确定";
        public static String appAddedAction = Intent.ACTION_PACKAGE_ADDED;
        public static String appRemovedAction = Intent.ACTION_PACKAGE_REMOVED;
    }
    
    /**
     * id可以通过ddms dump view hierarchy功能获得
     */
    public static class QRCodeConstant {
        public static String linkUrlId = "mark.qrcode:id/p";
        public static String linkBtnId = "mark.qrcode:id/q";
    }
    
    public static class NoteInfo {
        public static final int REQ_PICK_NOTE = 200;
    }
}
