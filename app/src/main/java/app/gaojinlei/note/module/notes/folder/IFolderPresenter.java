package app.gaojinlei.note.module.notes.folder;

import android.content.Intent;
import android.view.MenuItem;

import app.gaojinlei.note.adapter.RvEditFolderAdapter;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/07/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface IFolderPresenter {

    void setAdapter(RvEditFolderAdapter adapter);

    void getIntentData(Intent intent);

    void getData();

    void choiceItem(int position);

    void editFolder(int position);

    void addFolder();

    void judgeToDelete();

    void deleteMoreFolders();

    void setMenuAlpha(MenuItem menuItem);

    void setResult();

}
