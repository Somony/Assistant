package com.example.assistant;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * @author 作者：somon
 * @date 创建时间：2017/12/4
 * @desception 自定义的选择布局会出现在别的app的选择框中
 * 显示的title是app名
 * 点击自定义的布局就会跳到本页面中来，使用的是startaAtivityForResult
 * 那么在返回的时候可以带数据会上个页面
 */
public class SelectionActivity extends AppCompatActivity {
    
    private EditText editText;
    private boolean isReadOnly;
    CharSequence selectionText = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取选中的文字
            selectionText = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            //判断是否是可读可写的
            isReadOnly = getIntent().getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);
        }
        
        editText = findViewById(R.id.et_get_selection_text);
        editText.setSelection(selectionText.length());
        editText.setText(selectionText);
    }
    
    public void click(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isReadOnly) {
                String s = editText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_PROCESS_TEXT, s);
                setResult(RESULT_OK, intent);
            }
        }
        finish();
    }
}
