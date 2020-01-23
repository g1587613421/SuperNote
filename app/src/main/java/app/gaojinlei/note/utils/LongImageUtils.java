package app.gaojinlei.note.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.ScrollView;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/07/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class LongImageUtils {

    /**
     * 截取scrollview的屏幕
     **/
    public static Bitmap getScrollViewBitmap(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap

        bitmap = Bitmap.createBitmap(ScreenUtils.getScreenWidth(), h,
                Bitmap.Config.ARGB_4444);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#f2f7fa"));
        scrollView.draw(canvas);
        return bitmap;
    }
}
