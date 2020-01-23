package app.gaojinlei.note.module.notes.folderList;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface IFolderListView<N> {


    public void choiceItemAll();

    public void choiceItemPrimary();

    public void choiceItemRecycleBin();

    public void unChoiceItemAll();

    public void unChoiceItemPrimary();

    public void unChoiceItemRecycleBin();

    void setAllNoteCount(int count);

}
