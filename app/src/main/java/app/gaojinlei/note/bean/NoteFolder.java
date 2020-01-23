package app.gaojinlei.note.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by miaoyongyong on 2017/1/4.
 */

public class NoteFolder extends DataSupport {//便签夹类
    private int id;//便签夹的id
    private String folderName;//便签夹的名称
    private int noteCount;//便签夹内便签的总数目

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(int noteCount) {//设置便签夹中便签数量
        this.noteCount = noteCount;
    }
}
