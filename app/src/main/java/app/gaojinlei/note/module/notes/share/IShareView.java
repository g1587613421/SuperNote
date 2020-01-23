package app.gaojinlei.note.module.notes.share;

import android.app.Activity;
import android.net.Uri;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/07/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface IShareView {

    Activity getActivity();

    /**
     *   显示分享Dialog
     */
    void showShareDialog(Uri uri);
    
    /**
     *   前往应用设置Dialog
     */
    void showToAppSettingDialog();

    void showLoadingDialog(String message);

    void unShowLoadingDialog();
}
