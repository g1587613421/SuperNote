package app.gaojinlei.note.module.notes.folder;

import android.content.Intent;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/07/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface IFolderView {

    void hideAddBtn();

    void showAddBtn();

    void showSnackbar();

    void showDeleteDialog();

    void showLoading(String message);

    void unShowLoading();

    void scrollToItem(int position);

    void setActivityResultAndFinish(int resultCode,Intent intent);
}
