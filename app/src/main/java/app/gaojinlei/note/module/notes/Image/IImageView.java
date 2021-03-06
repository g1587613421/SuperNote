package app.gaojinlei.note.module.notes.Image;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/07/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface IImageView {

    void showLoading(String message);

    void unShowLoading();

    void showToAppSettingDialog();

    void setResultAndFinish();

}
