package app.gaojinlei.note.bean;

import org.litepal.crud.DataSupport;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2019/04/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Note extends DataSupport {
    private int id;
    private String noteId;
    private long createdTime;
    private long modifiedTime;
    private String noteContent;
    private int noteFolderId;//处于的便签夹的id
    private String noteEditlog;//增加便签日志功能--2019-4-12
    public String getNoteEditlog() {
        return noteEditlog==null?"":noteEditlog;
    }

    public void setNoteEditlog(String noteEditlog) {
        this.noteEditlog = noteEditlog;
    }

    //    是否是私密便签  1是 0不是
    private int isPrivacy;
    //    是否是废纸篓中便签，1是，0不是
    private int inRecycleBin;

    public int getIsPrivacy() {
        return isPrivacy;
    }

    public void setIsPrivacy(int isPrivacy) {
        this.isPrivacy = isPrivacy;
    }

    public int getInRecycleBin() {
        return inRecycleBin;
    }

    public void setInRecycleBin(int inRecycleBin) {
        this.inRecycleBin = inRecycleBin;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteFolderId() {
        return noteFolderId;
    }

    public void setNoteFolderId(int noteFolderId) {
        this.noteFolderId = noteFolderId;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

}
