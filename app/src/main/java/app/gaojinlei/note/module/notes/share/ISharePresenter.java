package app.gaojinlei.note.module.notes.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/07/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface ISharePresenter {

    /**
     *   获取图片
     */
    Bitmap getBitmap();
    
    /**
     *   分享图片
     */
    void sendBitmap();

    /**
     *   保存图片
     */
    void saveImage();
    
    /**
     *   保存图片 并获取Uri
     */
    Uri saveImageAndGetUri();
    
    /**
     *   Activity 返回
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);
    
    /**
     *   申请权限返回
     */
    void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    /**
     *   回收图片等操作
     */
    void onDestroy();
}
