package com.example.assistant.utils;

import android.content.ClipboardManager;
import android.content.Context;

/**
 * @author 作者：LHC
 * @date 创建时间：2018/1/5
 * @corporation 公司：anerfa
 * @desception
 */

public class ClipUtils {
    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }
    
    /**
     * 实现粘贴功能
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
}
