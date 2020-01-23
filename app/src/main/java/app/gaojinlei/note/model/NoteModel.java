package app.gaojinlei.note.model;

import com.app.gaojinlei.note.R;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;
import java.util.UUID;

import app.gaojinlei.note.bean.Note;

import static org.litepal.crud.DataSupport.where;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class NoteModel implements INoteModel<Note> {

    @Override
    public void initNote(int folderId) {

        long years = (long)12 * 30 * 24 * 60 * 60 * 1000;
        long month =(long) 24 * 60 * 60 * 1000 * 30;
        long days = (long)24 * 60 * 60 * 1000;
        long m=(long)60*1000;

        long time = TimeUtils.getNowMills();
        Note note = new Note();
        note.setCreatedTime(time );
        note.setModifiedTime(time );
        note.setNoteFolderId(folderId);
        note.setNoteContent(Utils.getContext().getResources().getString(R.string.database_content_one));
        note.setIsPrivacy(0);
        note.setInRecycleBin(0);
        note.setNoteId(UUID.randomUUID().toString());
        note.save();


        Note note1 = new Note();
        note1.setCreatedTime(time );
        note1.setModifiedTime(time );
        note1.setNoteFolderId(folderId);
        note1.setNoteContent(Utils.getContext().getResources().getString(R.string.database_content_two));
        note1.setIsPrivacy(0);
        note1.setInRecycleBin(0);
        note1.setNoteId(UUID.randomUUID().toString());
        note1.save();


    }

    @Override
    public void loadAllNoteList(LoadDataCallBack<Note> callBack) {//显示所有便签列表
        List<Note> data = where("isPrivacy = ? and inRecycleBin = ?", "0", "0").order("createdTime desc").find(Note.class);
        callBack.onSuccedd(data);
    }

    @Override
    public void loadPrivacyNoteList(LoadDataCallBack<Note> callBack) {
        List<Note> data = where("isPrivacy = ? and inRecycleBin = ?", "1", "0").order("createdTime desc").find(Note.class);
        callBack.onSuccedd(data);
    }

    @Override
    public void loadRecycleBinNoteList(LoadDataCallBack<Note> callBack) {
        List<Note> data = DataSupport.where("inRecycleBin = ?", "1").order("createdTime desc").find(Note.class);
        callBack.onSuccedd(data);
    }

    @Override
    public void loadNormalNoteList(int folderId, LoadDataCallBack<Note> callBack) {
        List<Note> data = where("noteFolderId = ? and isPrivacy = ? and inRecycleBin = ?", folderId + "", "0", "0").order("createdTime desc").find(Note.class);
        callBack.onSuccedd(data);
    }

    @Override
    public void addNote(Note note) {
        note.save();
    }

    @Override
    public void deleteNote(Note note) {
        String noteId=note.getNoteId();
        deleteNoteFile(noteId);
        note.delete();
    }

    @Override
    public void deleteNotes() {

    }

    private void deleteNoteFile(String noteId){
        File file=Utils.getContext().getExternalFilesDir(noteId);
        if(file.exists()){
            FileUtils.deleteDir(file);
        }
    }
}
