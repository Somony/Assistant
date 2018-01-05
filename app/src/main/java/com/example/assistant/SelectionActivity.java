package com.example.assistant;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.evernote.android.intent.EvernoteIntentResult;
import com.example.assistant.callback.ViewCallBack;
import com.example.assistant.presenter.NotePresenter;
import com.example.assistant.utils.ClipUtils;

/**
 * @author 作者：somon
 * @date 创建时间：2017/12/4
 * @desception 自定义的选择布局会出现在别的app的选择框中
 * 显示的title是app名
 * 点击自定义的布局就会跳到本页面中来，使用的是startaAtivityForResult
 * 那么在返回的时候可以带数据回上个页面
 */
public class SelectionActivity extends AppCompatActivity implements ViewCallBack {
    
    private EditText editText;
    private boolean isReadOnly;
    private CharSequence selectionText = "";
    
    private NotePresenter mPresenter = new NotePresenter(this);
    
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
        editText.setText(selectionText);
        editText.setSelection(selectionText.length());
        
        findViewById(R.id.fab_exist).setOnClickListener(clickListener);
        findViewById(R.id.fab_new).setOnClickListener(clickListener);
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.NoteInfo.REQ_PICK_NOTE:
                if (resultCode == RESULT_OK && data != null) {
                    String noteGuid = EvernoteIntentResult.getNoteGuid(data);
                    mPresenter.getNoteGuid(noteGuid);
                    mPresenter.viewNote();
                    //7a976065-f215-4c31-815b-6bcf282bbb5f
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //复制文字到剪切板
            ClipUtils.copy(editText.getText().toString().trim(), SelectionActivity.this);
            switch (v.getId()) {
                case R.id.fab_exist:
                    mPresenter.chooseExist();
                    break;
                case R.id.fab_new:
                    mPresenter.createNewNote(editText.getText().toString().trim());
                    break;
                default:
                    break;
            }
        }
    };
}
