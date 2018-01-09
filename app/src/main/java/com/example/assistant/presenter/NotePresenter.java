package com.example.assistant.presenter;

import android.content.Intent;

import com.evernote.android.intent.EvernoteIntent;
import com.example.assistant.Constant;
import com.example.assistant.activity.SelectionActivity;
import com.example.assistant.callback.ViewCallBack;

/**
 * @author 作者：somon
 * @date 创建时间：2018/1/8
 * @corporation 公司：anerfa
 * @desception
 */

public class NotePresenter {
    private SelectionActivity mActivity;
    private ViewCallBack context;
    private String mNoteGuid = "";
    
    public NotePresenter(ViewCallBack context) {
        this.context = context;
        mActivity = (SelectionActivity) context;
    }
    
    /**
     * 查询所有的evernote笔记
     */
    public void chooseExist() {
        mActivity.startActivityForResult(EvernoteIntent.pickNote().create(), Constant.NoteInfo.REQ_PICK_NOTE);
    }
    
    /**
     * 获取选择的noteGuid
     *
     * @param noteGuid
     */
    public void getNoteGuid(String noteGuid) {
        mNoteGuid = noteGuid;
    }
    
    /**
     * 跳转到对应Guid的note详情中
     */
    public void viewNote() {
        Intent intent = EvernoteIntent.viewNote()
                .setNoteGuid(mNoteGuid)
                .setFullScreen(true)
                .create();
        
        mActivity.startActivity(intent);
    }
    
    /**
     * 创建新的笔记
     */
    public void createNewNote(String content) {
        Intent intent = EvernoteIntent.createNewNote()
                .setTitle("笔记")
                .setTextPlain(content)
                .create();
        
        mActivity.startActivity(intent);
    }
}
