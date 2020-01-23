package app.gaojinlei.note.module.notes.Image;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/07/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface IImagePresenter {

    void getIntentData(Activity activity);

    File getImageFile(Activity activity);

    void saveImage(Activity activity);

    void deleteImage(Activity activity);

    void onRequestPermissionResult(Activity activity,int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
