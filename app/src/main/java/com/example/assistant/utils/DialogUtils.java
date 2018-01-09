package com.example.assistant.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assistant.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 作者：Somon
 * @date 创建时间：2017/8/10
 * @desception 弹框工具类
 */

public class DialogUtils {
    
    private static List<Dialog> dialogList = new ArrayList<>();
    
    /**
     * 原生弹框
     *
     * @param context
     * @param textArray    标题和内容，第一个是title，第二个是content ，长度为1就是显示content
     * @param btnTextArray 要是传入长度为1就只会有一个按钮，index=0为左
     * @param listener     按钮点击事件回调
     */
    public static void showSimpleDialog(Context context, String[] textArray, String[] btnTextArray, final OnBtnClickListener listener) {
        AlertDialog simpleDialog = new AlertDialog.Builder(context, R.style.my_dialog_style).create();
        dialogList.add(simpleDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_simple_layout, null, false);
        
        Button btnSimpleDialogLeft = ((Button) view.findViewById(R.id.btn_left));
        Button btnSimpleDialogRight = ((Button) view.findViewById(R.id.btn_right));
        btnSimpleDialogLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBtnClick(v);
            }
        });
        btnSimpleDialogRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBtnClick(v);
            }
        });
        
        TextView tvSimpleDialogTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvSimpleDialogContent = (TextView) view.findViewById(R.id.tv_content);
        
        if (textArray.length == 1) {
            tvSimpleDialogTitle.setText(textArray[0]);
            tvSimpleDialogTitle.setPadding(0, DisplayUtil.dp2px(context, 13), 0, DisplayUtil.dp2px(context, 13));
            tvSimpleDialogContent.setVisibility(View.GONE);
        } else if (textArray.length == 2) {
            tvSimpleDialogTitle.setText(textArray[0]);
            
            tvSimpleDialogContent.setText(textArray[1]);
        } else {
            throw new IllegalArgumentException("传入的参数数量错误");
        }
        
        if (btnTextArray.length == 2) {
            btnSimpleDialogLeft.setText(btnTextArray[0]);
            btnSimpleDialogRight.setText(btnTextArray[1]);
        } else if (btnTextArray.length == 1) {
            btnSimpleDialogLeft.setText(btnTextArray[0]);
            btnSimpleDialogRight.setVisibility(View.GONE);
            view.findViewById(R.id.v_line_center).setVisibility(View.GONE);
        } else {
            throw new IllegalArgumentException("传入的参数数量错误");
        }
        simpleDialog.setCanceledOnTouchOutside(false);
        
        simpleDialog.show();
        Window window = simpleDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = DisplayUtil.dp2px(context, 270);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        
        simpleDialog.setContentView(view);
    }
    
    /**
     * 类似toast的弹框，3秒自动消失
     */
    public static void showToastDialog(Context context, String title, int iconId) {
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(context).inflate(R.layout.view_tips_toast, null, false);
        ((TextView) view.findViewById(R.id.dialog_tips_msg)).setText(title);
        ((ImageView) view.findViewById(R.id.dialog_tips_icon)).setImageResource(iconId);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    
    public static void dismiss() {
        List<Dialog> removedDialogList = new ArrayList<>();
        
        for (Dialog dialog : dialogList) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                removedDialogList.add(dialog);
            }
        }
        
        dialogList.removeAll(removedDialogList);
    }
    
    public interface OnBtnClickListener {
        /**
         * 点击事件
         * R.id.btn_left
         * R.id.btn_right
         *
         * @param v
         */
        void onBtnClick(View v);
    }
}
