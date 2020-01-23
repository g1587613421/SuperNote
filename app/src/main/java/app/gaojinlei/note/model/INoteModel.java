package app.gaojinlei.note.model;

import app.gaojinlei.note.bean.Note;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface INoteModel<T> {
    void initNote(int folderId);

    void loadAllNoteList(LoadDataCallBack<T> callBack);

    void loadPrivacyNoteList(LoadDataCallBack<T> callBack);

    void loadRecycleBinNoteList(LoadDataCallBack<T> callBack);

    void loadNormalNoteList(int folderId,LoadDataCallBack<T> callBack);

    void addNote(T note);

    void deleteNote(Note note);

    void deleteNotes();
}
