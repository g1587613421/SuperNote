package app.gaojinlei.note.model;

import app.gaojinlei.note.bean.Note;
import app.gaojinlei.note.bean.NoteFolder;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface INoteFolderModel<T> {

    int initNoteFolderAndGetFolderId();

    void loadNoteFoldersList(LoadDataCallBack<T> callBack);

    void addNoteFolder(T noteFolder);

    void deleteNoteFolder(NoteFolder folder);

    void deleteNoteFolders();

    void addNote2Folder(Note note, NoteFolder folder);

    void addNote2Privacy(Note note,NoteFolder folder);

}
